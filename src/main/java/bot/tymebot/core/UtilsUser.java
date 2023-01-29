package bot.tymebot.core;

import bot.tymebot.Bot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@UtilityClass
public class UtilsUser {

    public boolean isDev(String id) {
        return Arrays.stream(Bot.getDevIds()).toList().contains(id);
    }

}
