package bot.tymebot.config;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class TymeConfig {

    @SerializedName("token")
    private String botToken;

    @SerializedName("devIds")
    private String[] devIds;

    @SerializedName("blacklistedUserIds")
    private String[] blacklistedUserIds;

    @SerializedName("blacklistedGuildIds")
    private String[] blacklistedGuildIds;
}
