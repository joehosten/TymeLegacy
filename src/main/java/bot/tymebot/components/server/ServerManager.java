package bot.tymebot.components.server;

import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ServerManager {

    Map<String, DiscordServer> getServers();

    @Nullable
    DiscordServer getServer(@NotNull String id);

    default @Nullable DiscordServer getServer(@NotNull Guild guild) {
        return getServer(guild.getId());
    }

    DiscordServer addServer(@NotNull Guild guild);

    void removeServer(@NotNull String id);

    default void removeServer(@NotNull Guild guild) {
        removeServer(guild.getId());
    }
}
