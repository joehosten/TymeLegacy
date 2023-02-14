package bot.tymebot.core.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class Utils {

    private final DecimalFormat df;

    static {
        df = new DecimalFormat("###,###,###,###.##");
    }

    public String decimalFormat(double number) {
        return df.format(number);
    }

    public String decimalFormat(long number) {
        return df.format(number);
    }

    public String decimalFormat(int number) {
        return df.format(number);
    }

    public String decimalFormat(float number) {
        return df.format(number);
    }

    public String decimalFormat(short number) {
        return df.format(number);
    }

    public String makePrettyTime(long ms) {
        int totalSeconds = (int) (ms / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = (totalSeconds / 3600) % 24;
        int days = totalSeconds / 86400;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(days > 1 ? " days " : " day ");
        }
        if (hours > 0 || sb.length() > 0) {
            sb.append(hours).append(hours > 1 ? " hours " : " hour ");
        }
        if (minutes > 0 || sb.length() > 0) {
            sb.append(minutes).append(minutes > 1 ? " minutes " : " minute ");
        }
        sb.append(seconds).append(seconds > 1 ? " seconds" : " second");

        return sb.toString();
    }
}
