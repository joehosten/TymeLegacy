package bot.tymebot.core;

import bot.tymebot.Bot;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public String getUptime() {
        long uptime = System.currentTimeMillis() - Bot.getStartTime();
        long uptimeSeconds = uptime / 1000;
        long uptimeMinutes = uptimeSeconds / 60;
        long uptimeHours = uptimeMinutes / 60;
        long uptimeDays = uptimeHours / 24;
        uptimeSeconds %= 60;
        uptimeMinutes %= 60;
        uptimeHours %= 24;
        return String.format("%d days, %d hours, %d minutes, %d seconds", uptimeDays, uptimeHours, uptimeMinutes, uptimeSeconds);
    }
}
