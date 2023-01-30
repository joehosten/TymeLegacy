package bot.tymebot.components.guild;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildLeave(GuildLeaveEvent e) {
        Guild guild = e.getGuild();

        System.out.println("Tyme has left %guild% (%guildid%) - %memberCount% members."
                .replace("%guild%", guild.getName())
                .replace("%guildid%", guild.getId()
                        .replace("%memberCount%", String.valueOf(guild.getMembers().size()))));
    }
}
