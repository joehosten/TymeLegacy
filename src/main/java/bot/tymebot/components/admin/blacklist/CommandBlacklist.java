package bot.tymebot.components.admin.blacklist;

import bot.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "blacklist", description = "Blacklist a user or guild.")
public class CommandBlacklist extends SlashCommand {
    private Bot bot;
    public CommandBlacklist(Bot bot) {
        this.bot = bot;
        addSubCommands(new SubCommandBlacklistGuild(this.bot));
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
    }
}
