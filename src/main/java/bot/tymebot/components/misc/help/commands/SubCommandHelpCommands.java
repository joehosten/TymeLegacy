package bot.tymebot.components.misc.help.commands;

import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "commands", description = "Show the help info for commands.")
public class SubCommandHelpCommands extends SlashSubCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tyme Commands Page 1");
        eb.setDescription("Use the command `/help commands <page>` to navigate throughout the different commands available with Tyme Bot.");
        eb.addField("Miscellaneous Commands"
                , """
                        • `/help [module] [page]` - Sends the commands and help information about Tyme.
                        • `/botsuggestion` - Send the developers of Tyme suggestions for the bot.
                        • `/discord` - Send the support discord link.
                        • `/invite` - Get an invite link to add Tyme to your server.
                        • `/info` - Get diagnostic information about Tyme.
                        • `/rules` - Get the rules for Tyme.
                        """, false);
    }
}
