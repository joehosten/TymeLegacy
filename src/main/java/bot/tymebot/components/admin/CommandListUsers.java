package bot.tymebot.components.admin;

import bot.tymebot.core.UtilsUser;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "listusers", description = "Lists the amount of users the bot can see")
public class CommandListUsers extends SlashCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }
        int users = slashCommandInteractionEvent.getJDA().getUsers().size();
        slashCommandInteractionEvent.reply("I am currently used by " + users + (users == 1 ? " user." : " users.")).setEphemeral(true).queue();
    }
}
