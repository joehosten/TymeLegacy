package com.bentomc.tymebot.components.admin;

import com.bentomc.tymebot.Bot;
import com.bentomc.tymebot.core.util.HasteBin;
import com.bentomc.tymebot.core.util.UtilsUser;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SlashInfo(name = "listguilds", description = "Lists the amount of guilds the bot is in")
public class CommandListGuilds extends SlashCommand {

    private Bot bot;

    public CommandListGuilds(Bot bot) {
        this.bot = bot;
    }

    @SneakyThrows
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }
        int guilds = slashCommandInteractionEvent.getJDA().getGuilds().size();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Guild Information");
        eb.setDescription("Tyme is in " + guilds + (guilds == 1 ? " guild." : " guilds.\n You can view a list of guilds by going to [this link](" + guildsOutput(slashCommandInteractionEvent.getJDA().getGuilds()) + ")"));
        eb.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
        eb.setColor(Color.CYAN);
        slashCommandInteractionEvent.replyEmbeds(eb.build()).setEphemeral(true).queue();


    }

    private String guildsOutput(List<Guild> guilds) throws IOException {
        StringBuilder sb = new StringBuilder();
        guilds.forEach(k -> sb.append(k.getName()).append(" - ").append(k.getMembers().size()).append(" members.").append("\n"));
        String[] guildss = new String[]{sb.toString()};
        StringBuilder urlBuilder = new StringBuilder();
        Arrays.stream(guildss).forEach(line -> urlBuilder.append(line).append("\n"));
        return new HasteBin().post(urlBuilder.toString(), false);
    }
}
