package com.bentomc.tymebot.components.admin;

import com.bentomc.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.Objects;

@SlashInfo(name = "maintenance", description = "Put the bot into maintenance mode. ")
public class CommandMaintenance extends SlashCommand {

    private Bot bot;

    public CommandMaintenance(Bot bot) {
        this.bot = bot;
        setData(data -> {
            data.addOption(OptionType.BOOLEAN, "toggle", "Toggle maintenance true/false.", true);
        });
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        boolean toggle = Objects.requireNonNull(slashCommandInteractionEvent.getOption("toggle")).getAsBoolean();
        boolean configStatus = bot.getConfig().isMaintenanceMode();

        if (toggle == configStatus) {
            slashCommandInteractionEvent.reply("The bot is already " + (toggle ? "in" : "out off") + " maintenance mode!").setEphemeral(true).queue();
            return;
        }

        bot.getConfig().setMaintenanceMode(toggle);
        bot.saveConfig();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Maintenance mode " + (toggle ? "on" : "off"));
        eb.setDescription("You have put Tyme " + (toggle ? "into" : "out of") + " maintenance mode.");
        eb.setColor(toggle ? Color.orange : Color.green);
        eb.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
        slashCommandInteractionEvent.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
