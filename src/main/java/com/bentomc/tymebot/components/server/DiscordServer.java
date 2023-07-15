package com.bentomc.tymebot.components.server;

import com.bentomc.tymebot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

public interface DiscordServer {

    String getServerID();

    @Nullable
    default Guild getGuild() {
        return Bot.getInstance().getJda().getGuildById(getServerID());
    }

    long getJoinDate();
}
