package dev.fabled.astra;

import com.github.retrooper.packetevents.PacketEvents;
import dev.fabled.astra.commands.*;
import dev.fabled.astra.lang.LocaleManager;
import dev.fabled.astra.lang.impl.AstraAdminLang;
import dev.fabled.astra.lang.impl.ErrorLang;
import dev.fabled.astra.lang.interfaces.LangKeys;
import dev.fabled.astra.listener.*;
import dev.fabled.astra.listeners.MenuListener;
import dev.fabled.astra.listeners.MinePanelListener;
import dev.fabled.astra.listeners.PacketAdapter;
import dev.fabled.astra.listeners.PacketEventsListener;
import dev.fabled.astra.mines.wand.MineWand;
import dev.fabled.astra.modules.ModuleManager;
import dev.fabled.astra.modules.impl.MinesModule;
import dev.fabled.astra.modules.impl.OmniToolModule;
import dev.fabled.astra.omnitool.OmniToolItem;
import dev.fabled.astra.omnitool.menu.OmnitoolMenu;
import dev.fabled.astra.packet.BombsPacketHandler;
import dev.fabled.astra.utils.MineData;
import dev.fabled.astra.utils.MineReader;
import dev.fabled.astra.utils.configuration.YamlConfig;
import dev.fabled.astra.utils.logger.AstraLog;
import dev.fabled.astra.utils.logger.AstraLogLevel;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AstraPlugin extends JavaPlugin implements AstraUtilities {

    private static AstraPlugin instance;

    private YamlConfig configYml;

    private final Map<Enchantment, Integer> enchantments = new ConcurrentHashMap<>();
    private final List<Listener> listeners = List.of(
            new MenuListener(),
            new MineWand(),
            new PacketAdapter(),
            new MinePanelListener(),
            new OmniToolListener(),
            new OmnitoolMenu(),
            new EnchantsTriggerEventListener(),
            //new VirtualBlockBreak(),
            new BombsPacketHandler()
    );
    NamespacedKey tokenfinder = new NamespacedKey("astra", "tokenfinder");
    NamespacedKey shockwave = new NamespacedKey("astra", "shockwave");
    NamespacedKey fortune = new NamespacedKey("astra", "fortune");

    private LocaleManager localeManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private @NotNull YamlConfiguration rpgConfig;
    private EnchantsTriggerEventListener enchantsTriggerEventListener;
    private FileConfiguration config;
    private FileConfiguration rpgConfigFile;
    private final Map<String, Boolean> rarities = new HashMap<>();
    private EnchantMenuListener enchantMenuListener;

    private final List<LangKeys> lang = List.of(
            new AstraAdminLang(),
            new ErrorLang()
    );
    private NamespacedKey WAND_NAMESPACED_KEY;

    private NamespacedKey OMNITOOL_NAMESPACED_KEY;


    public static NamespacedKey getWandNamespacedKey() {
        return instance.WAND_NAMESPACED_KEY;
    }

    public static AstraPlugin getInstance() {
        return instance;
    }

    public void onReload() {
        configYml.save();
        configYml.reload();
        AstraLog.onReload();
        moduleManager.onReload();
    }

    @Override
    public void onDisable() {
        moduleManager.onDisable();
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison disabled!");
    }

    @Override
    public YamlConfig getConfigYml() {
        return configYml;
    }

    @Override
    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    @Override
    public void onLoad() {
        instance = this;
        Astra.onLoad(this);
        configYml = new YamlConfig("config");
        AstraLog.onLoad();

        LocaleManager.onLoad();
        lang.forEach(LocaleManager.getInstance()::registerLanguageKeys);

        moduleManager = new ModuleManager();
        new MinesModule();
        new OmniToolModule();
        moduleManager.onLoad();
    }

    @Override
    public void onEnable() {
        commandManager = new CommandManager();
        commandManager.register(new AstraCommand(this));
        commandManager.register(new OmniToolCommand());
        commandManager.register(new ExplosivesCommand());
        commandManager.register(new RpgCommand());
        moduleManager.onEnable();
        saveDefaultConfig();

        loadConfig();
        loadRPGConfig();

        // ENCHANTS




        listeners.forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));


        getServer().getPluginManager().registerEvents(new PumpkinLauncher(this, rarities, getRPGConfig()), this);

        // PacketEvents
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(true)
                .bStats(true);
        PacketEvents.getAPI().load();
        PacketEvents.getAPI().init();

        //PacketEvents.getAPI().getEventManager().registerListener(new PacketInteractListener(Astra.getPlugin()));
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEventsListener());
        PacketEvents.getAPI().getEventManager().registerListener(new BombsPacketHandler());

        enchantMenuListener = new EnchantMenuListener();

        // Register events
        getServer().getPluginManager().registerEvents(enchantMenuListener, this);
        enchantsTriggerEventListener = new EnchantsTriggerEventListener();
        enchantsTriggerEventListener.loadEnchantments();
        getServer().getPluginManager().registerEvents(new VirtualBlockBreak(enchantsTriggerEventListener), this);

        // Namespace
        WAND_NAMESPACED_KEY = new NamespacedKey(this, "astra-mine-wand");
        MineWand.initialize(WAND_NAMESPACED_KEY);
        OMNITOOL_NAMESPACED_KEY = new NamespacedKey(this, "astra-omnitool");
        OmniToolItem.initialize(OMNITOOL_NAMESPACED_KEY);

        AstraLog.divider();
        AstraLog.log("",
                "+++++++  ++++++  +++++++  +++++++  +++++++",
                "+|   ++  +|         ++    +|   ++  +|   ++",
                "+++++++  ++++++     ++    +++++++  +++++++",
                "+|   ++      ++     ++    +| ++    +|   ++",
                "+|   ++  ++++++     ++    +|   ++  +|   ++",
                ""
        );
        AstraLog.log(AstraLogLevel.SUCCESS, "AstraPrison enabled!");
        AstraLog.log(
                "Version: " + getPluginMeta().getVersion(),
                "Developed by Mantice and DrDivx2k",
                ""
        );
        AstraLog.divider();


        printLoadedMinesCount();
    }

    public void printLoadedMinesCount() {
        Map<String, MineData> mineMap = MineReader.getMineMap();
        int loadedMinesCount = mineMap.size();
        getLogger().info("Loaded " + loadedMinesCount + " public mines!");
    }

    public void loadConfig() {
        File configFile = new File(getDataFolder(), "explosives/rpgconfig.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("explosives/rpgconfig.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defConfigStream = getResource("explosives/rpgconfig.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
        if (config.contains("thresholds")) {
            Set<String> keys = config.getConfigurationSection("thresholds").getKeys(false);
            for (String key : keys) {
                rarities.put(key, true);
            }
        } else {
            getLogger().warning("Der Abschnitt 'thresholds' wurde in der Konfigurationsdatei nicht gefunden.");
        }
    }

    private void loadRPGConfig() {
        File rpgConfigFile = new File(getDataFolder(), "explosives/rpgconfig.yml");
        if (!rpgConfigFile.exists()) {
            rpgConfigFile.getParentFile().mkdirs();
            saveResource("explosives/rpgconfig.yml", false);
        }

        rpgConfig = YamlConfiguration.loadConfiguration(rpgConfigFile);
        getLogger().info("RPG-Konfigurationsdatei erfolgreich geladen.");
    }


    public FileConfiguration getRPGConfig() {
        return rpgConfig;
    }
}
