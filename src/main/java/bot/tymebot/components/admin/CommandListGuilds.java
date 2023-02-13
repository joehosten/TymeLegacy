package bot.tymebot.components.admin;

import bot.tymebot.core.util.UtilsUser;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "listguilds", description = "Lists the amount of guilds the bot is in")
public class CommandListGuilds extends SlashCommand {

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }
        int guilds = slashCommandInteractionEvent.getJDA().getGuilds().size();
        slashCommandInteractionEvent.reply("I am in " + guilds + (guilds == 1 ? " guild." : " guilds.")).setEphemeral(true).queue();
    }
}
