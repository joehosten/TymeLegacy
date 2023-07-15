package com.bentomc.tymebot.components.misc;

import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "invite", description = "Get a link to invite Tyme.")
public class CommandInvite extends SlashCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {

    }
}
