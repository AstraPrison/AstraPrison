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

    /*
            Omni-Tool Messages
    */

    /** Omni-Tool Message */
    public static final String
            OMNI_TOOL_GIVE = "You gave a new omni-tool to ";

    /** Omni-Tool Message */
    public static final ImmutableList<String>
            OMNI_TOOL_ADMIN_HELP = ImmutableList.of(
                    ""
            );

}
