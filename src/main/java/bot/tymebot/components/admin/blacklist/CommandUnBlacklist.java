package bot.tymebot.components.admin.blacklist;

import bot.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "unblacklist", description = "Unblacklist a user or guild.")
public class CommandUnBlacklist extends SlashCommand {
    public CommandUnBlacklist(Bot bot) {
        addSubCommands(new SubCommandUnBlacklistGuild(bot), new SubCommandUnBlacklistUser(bot));
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
    }
}
