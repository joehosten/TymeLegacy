package bot.tymebot.components.server;

import bot.tymebot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

public interface DiscordServer {

    String getServerID();

    @Nullable
    default Guild getGuild() {
        return Bot.getInstance().getJda().getGuildById(getServerID());
    }

    long getJoinDate();

    boolean isBlacklisted();

    void setBlacklisted(boolean blacklisted);
}
