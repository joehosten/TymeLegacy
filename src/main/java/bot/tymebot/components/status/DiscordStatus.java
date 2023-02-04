package bot.tymebot.components.status;

import net.dv8tion.jda.api.entities.Activity;

public record DiscordStatus(Activity.ActivityType type, String text) {
}
