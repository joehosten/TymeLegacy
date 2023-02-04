package bot.tymebot.components.misc;

import bot.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

@SlashInfo(name = "botinfo", description = "Shows information about the bot")
public class CommandInfo extends SlashCommand {
    private final Bot bot;

    public CommandInfo(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        System.out.println(slashCommandInteractionEvent.getUser().getId() + " - - " + Objects.requireNonNull(slashCommandInteractionEvent.getGuild()).getId());
        if (bot.isBlacklisted(slashCommandInteractionEvent.getUser().getId(), Objects.requireNonNull(slashCommandInteractionEvent.getGuild()).getId())) {
            slashCommandInteractionEvent.reply("You are blacklisted from using Tyme!").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tyme Bot Info");
        eb.setDescription("""
                This bot is made by the Discord.jar Team!
                Uptime: `%uptime%`
                Servers: `%totalguilds%`
                """.replace("%uptime%", bot.getUptime()).replace("%totalguilds%", String.valueOf(slashCommandInteractionEvent.getJDA().getGuilds().size())));
        eb.setThumbnail(slashCommandInteractionEvent.getJDA().getSelfUser().getAvatarUrl());
        slashCommandInteractionEvent.replyEmbeds(eb.build()).queue();
    }
}
