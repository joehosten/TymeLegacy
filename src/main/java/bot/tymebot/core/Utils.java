package bot.tymebot.core;

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


}
