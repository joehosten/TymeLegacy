package bot.tymebot.config;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TymeConfig {

    @SerializedName("token")
    private String botToken;

    // Represents the Discord Server ID of the "Parent Server", you can
    // consider this an "admin server"
    @SerializedName("parent-server-id")
    private String parentServer;

    @SerializedName("devIds")
    private String[] devIds;

    @SerializedName("blacklistedUserIds")
    private String[] blacklistedUserIds;

    @SerializedName("blacklistedGuildIds")
    private String[] blacklistedGuildIds;
}
