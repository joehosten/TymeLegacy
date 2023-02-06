package bot.tymebot.components.admin;

import bot.tymebot.Bot;
import bot.tymebot.core.UtilsUser;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.atomic.AtomicBoolean;

@SlashInfo(name = "logout", description = "Log the bot out from JDA and shutdown.")
public class CommandLogout extends SlashCommand {
    private static final AtomicBoolean EXITING = new AtomicBoolean(false);
    Bot bot;

    public CommandLogout(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }
        if (EXITING.get()) {
            slashCommandInteractionEvent.reply("Tyme is already shutting down!").setEphemeral(true).queue();
            return;
        }
        EXITING.set(true);
        slashCommandInteractionEvent.reply("Tyme is shutting down!").setEphemeral(true).queue();
        bot.getServerManager().reCacheServers();
        bot.getJda().shutdown();
    }
}


