package dev.fabled.astra.lang.console;

import com.google.common.collect.ImmutableList;

public class ConsoleLang {

    /*
            Error Messages
     */

    /** Error Message */
    public static final String
            PLAYER_ONLY = "Please run this command as a player!",
            SELECT_PLAYER = "Select a player!",
            /** Error Message<br>Append a player's name */
            INVALID_PLAYER = "Invalid player: ";

    /*
            Admin Messages
    */

    /** Admin Message */
    public static final ImmutableList<String>
            ADMIN_HELP = ImmutableList.of(
                    "Astra Commands:",
                    "| 'astra' - Shows plugin information",
                    "| 'astra help' - Shows this information",
                    "| 'astra reload' - Reloads plugin configuration"
            );


    public static final String
            OMNI_TOOL_GIVE = "You gave a new omni-tool to ";

    public static final ImmutableList<String>
            OMNI_TOOL_ADMIN_HELP = ImmutableList.of(
                    ""
            );


    public static final String INVALID_ENCHANTMENT = "Invalid enchantment specified: ";
    public static final String OMNI_TOOL_ENCHANT = "Successfully enchanted the tool for ";
    public static final String MISSING_ENCHANT_ARGS = "Missing arguments for enchant command.";

    public static final String GIVE_COMMAND_FROM_CONSOLE = "Missing arguments for omnitool command.";
    public static final String OMNI_TOOL_GIVEN_TO = "Successfully give omnitool to ";
    public static final String GIVE_COMMAND_MISSING_ARGUMENT = "Missing arguments for omnitool command.";
    public static final String ARMOR_ADMIN_HELP = "Missing arguments for omnitool command.";
}
