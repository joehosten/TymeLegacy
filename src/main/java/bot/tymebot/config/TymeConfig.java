package bot.tymebot.config;


import bot.tymebot.components.status.DiscordStatus;
import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;

@Data
public class TymeConfig {

    @SerializedName("token")
    private String botToken;

    // Represents the Discord Server ID of the "Parent Server", you can
    // consider this an "admin server"
    @SerializedName("parent-server-id")
    private String parentServer;

    // Determines how long (in seconds) the current discord status
    // should show for before changing to the next one
    @SerializedName("discord-status-interval")
    private int discordStatusInterval = 10;

    // A list of statuses to cycle through
    @SerializedName("statuses")
    private List<DiscordStatus> statuses = Lists.newArrayList(
            new DiscordStatus(Activity.ActivityType.WATCHING, "over the server"),
            new DiscordStatus(Activity.ActivityType.WATCHING, "%users% users"),
            new DiscordStatus(Activity.ActivityType.WATCHING, "%guilds% guilds")
    );

    @SerializedName("devIds")
    private String[] devIds;

    @SerializedName("blacklistedUserIds")
    private String[] blacklistedUserIds;

    @SerializedName("blacklistedGuildIds")
    private String[] blacklistedGuildIds;
}
