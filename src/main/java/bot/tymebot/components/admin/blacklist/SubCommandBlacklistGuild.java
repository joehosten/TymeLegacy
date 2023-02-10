package bot.tymebot.components.admin.blacklist;

import bot.tymebot.Bot;
import bot.tymebot.core.util.UtilsUser;
import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

@SlashInfo(name = "guild", description = "Blacklist a guild ID.")
public class SubCommandBlacklistGuild extends SlashSubCommand {
    private Bot bot;

    public SubCommandBlacklistGuild(Bot bot) {
        this.bot = bot;
        setData(data -> {
            data.addOption(OptionType.STRING, "id", "The ID of the guild.", true);
        });
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        if (!UtilsUser.isDev(slashCommandInteractionEvent.getUser().getId())) {
            slashCommandInteractionEvent.reply("This command is limited to Developers only.");
            return;
        }

        String id = Objects.requireNonNull(slashCommandInteractionEvent.getOption("id")).getAsString();
        if (bot.getJda().getGuildById(id) == null) {
            slashCommandInteractionEvent.reply("This is not cached by Tyme's JDA.").setEphemeral(true).queue();
            return;
        }
        ArrayList<String> guilds = bot.getConfig().getBlacklistedGuildIds();
        guilds.add(id);
        bot.getConfig().setBlacklistedGuildIds(guilds);
        bot.saveConfig();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("You blacklisted a guild!");
        eb.setDescription("You blacklisted `%guild%`\n\nThey will not be able to commands or interact with the bot.".replace("%guild%", Objects.requireNonNull(bot.getJda().getGuildById(id)).getName()));
        eb.setThumbnail(Objects.requireNonNull(bot.getJda().getGuildById(id)).getIconUrl() == null ? null : Objects.requireNonNull(bot.getJda().getGuildById(id)).getIconUrl());
        eb.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
        eb.setColor(Color.orange);
        slashCommandInteractionEvent.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
