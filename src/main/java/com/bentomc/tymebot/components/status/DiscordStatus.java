package com.bentomc.tymebot.components.status;

import lombok.Data;
import net.dv8tion.jda.api.entities.Activity;

// made this a Data class since a record makes the fields final and jda doesnt like final ActivityType's

@Data
public class DiscordStatus {
    public Activity.ActivityType type;
    public String text;

    public DiscordStatus(Activity.ActivityType type, String text) {
        this.type = type;
        this.text = text;
    }
}