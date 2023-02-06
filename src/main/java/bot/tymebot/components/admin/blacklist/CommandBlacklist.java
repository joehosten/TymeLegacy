package bot.tymebot.components.admin.blacklist;

import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "blacklist", description = "Blacklist a user or guild.")
public class CommandBlacklist extends SlashCommand {
    public CommandBlacklist() {
        addSubCommands();
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
    }
}
