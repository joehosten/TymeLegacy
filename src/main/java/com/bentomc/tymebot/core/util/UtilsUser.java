package com.bentomc.tymebot.core.util;

import com.bentomc.tymebot.Bot;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@UtilityClass
public class UtilsUser {

    public boolean isDev(String id) {
        return Arrays.stream(Bot.getDevIds()).toList().contains(id);
    }

    public void initUser(String id) {

    }

}
