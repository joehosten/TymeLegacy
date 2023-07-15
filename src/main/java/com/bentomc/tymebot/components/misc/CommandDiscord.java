package com.bentomc.tymebot.components.misc;

import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "discord", description = "Sends the support discord link.")
public class CommandDiscord extends SlashCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {

    }
}
