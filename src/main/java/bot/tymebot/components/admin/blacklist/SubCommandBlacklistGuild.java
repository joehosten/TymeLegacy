package bot.tymebot.components.admin.blacklist;

import bot.tymebot.core.UtilsUser;
import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "guild", description = "Blacklist a guild ID.")
public class SubCommandBlacklistGuild extends SlashSubCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }
    }
}
