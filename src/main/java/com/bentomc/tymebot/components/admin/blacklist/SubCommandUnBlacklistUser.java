package com.bentomc.tymebot.components.admin.blacklist;

import com.bentomc.tymebot.Bot;
import com.bentomc.tymebot.core.util.UtilsUser;
import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@SlashInfo(name = "user", description = "Unblacklist a user ID.")
public class SubCommandUnBlacklistUser extends SlashSubCommand {
    private Bot bot;

    public SubCommandUnBlacklistUser(Bot bot) {
        this.bot = bot;
        setData(data -> {
            data.addOption(OptionType.STRING, "id", "The ID of the user.", true);
        });
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }

        String id = Objects.requireNonNull(slashCommandInteractionEvent.getOption("id")).getAsString();
        if (bot.getJda().getUserById(id) == null) {
            slashCommandInteractionEvent.reply("This is not cached by Tyme's JDA.").setEphemeral(true).queue();
            return;
        }
        ArrayList<String> guilds = bot.getConfig().getBlacklistedUserIds();

        guilds.remove(id);
        bot.getConfig().setBlacklistedUserIds(guilds);
        bot.saveConfig();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("You unblacklisted a user!");
        eb.setDescription("You unblacklisted `%user%`\n\nThey now have access to the bot.".replace("%user%", Objects.requireNonNull(bot.getJda().getUserById(id)).getName()));
        eb.setThumbnail(Objects.requireNonNull(bot.getJda().getUserById(id)).getAvatarUrl() == null ? null : Objects.requireNonNull(bot.getJda().getUserById(id)).getAvatarUrl());
        eb.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
        eb.setColor(Color.yellow);
        slashCommandInteractionEvent.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}