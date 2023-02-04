package bot.tymebot.components.guild;

import bot.tymebot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class ServerJoinListener extends ListenerAdapter {

    private final Bot bot;

    public ServerJoinListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e) {
        Guild guild = e.getGuild();
        Objects.requireNonNull(guild.getDefaultChannel()).asTextChannel().sendMessage("Thank you for inviting Tyme!");

        System.out.println("Tyme has joined %guild% (%guildid%) - %memberCount% members.".replace("%guild%", guild.getName()).replace("%guildid%", guild.getId().replace("%memberCount%", String.valueOf(guild.getMembers().size()))));

        bot.getServerManager().addServer(guild);
    }
}
