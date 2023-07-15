package com.bentomc.tymebot.components.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DiscordServerImpl implements DiscordServer {

    @SerializedName("server-id")
    private final String id;

    @SerializedName("join-date")
    private final long joinDate;

    @Override
    public String getServerID() {
        return id;
    }

    @Override
    public long getJoinDate() {
        return joinDate;
    }
}
