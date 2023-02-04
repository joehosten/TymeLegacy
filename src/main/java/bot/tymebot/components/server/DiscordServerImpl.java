package bot.tymebot.components.server;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class DiscordServerImpl implements DiscordServer {

    @SerializedName("server-id")
    private final String id;

    @SerializedName("join-date")
    private final long joinDate;

    @SerializedName("blacklisted")
    private boolean blacklisted;

    @Override
    public String getServerID() {
        return id;
    }

    @Override
    public long getJoinDate() {
        return joinDate;
    }

    @Override
    public boolean isBlacklisted() {
        return blacklisted;
    }

    @Override
    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }
}
