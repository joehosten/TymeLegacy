package bot.tymebot.components.admin;

import bot.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;

@SlashInfo(
        name = "reload",
        description = "Reloads the bot configuration."
)
public class CommandReload extends SlashCommand {

    private final Bot bot;

    public CommandReload(Bot bot) {
        this.bot = bot;
        setData(data -> {
            data.setGuildOnly(true);
            // Only visible to server administrators
            data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        });
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        if (guild == null)
            return;
        long start = System.currentTimeMillis();

        bot.reloadConfig();

        long end = System.currentTimeMillis();

        long diff = Math.abs(end - start);
        event.reply("Reloaded bot configuration & servers in " + diff + "ms.")
                .setEphemeral(true).queue();
    }
}
