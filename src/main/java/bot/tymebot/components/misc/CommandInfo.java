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
        Bot.LimitedStatus status = bot.isLimited(slashCommandInteractionEvent.getUser().getId(),
                Objects.requireNonNull(slashCommandInteractionEvent.getGuild()).getId());
        if (status != Bot.LimitedStatus.NOT_LIMITED) {
            slashCommandInteractionEvent.reply(status.message).setEphemeral(true).queue();
            return;
        }
        int totalGuilds = slashCommandInteractionEvent.getJDA().getGuilds().size();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tyme Bot Info");
        eb.setDescription("This bot is made by the Negative Games Team!\nUptime: `" + bot.getUptime() + "`\nServers: `" + totalGuilds + "`");
        eb.setThumbnail(slashCommandInteractionEvent.getJDA().getSelfUser().getAvatarUrl());
        slashCommandInteractionEvent.replyEmbeds(eb.build()).queue();
    }
}