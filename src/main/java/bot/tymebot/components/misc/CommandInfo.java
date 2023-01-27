package bot.tymebot.components.misc;

import bot.tymebot.core.Utils;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "botinfo", description = "Shows information about the bot")
public class CommandInfo extends SlashCommand {
    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tyme Bot Info");
        eb.setDescription("""
                This bot is made by the Discord.jar Team!
                Uptime: `%uptime%`
                Servers: `%totalguilds%`
                """.replace("%uptime%", Utils.getUptime()).replace("%totalguilds%", String.valueOf(slashCommandInteractionEvent.getJDA().getGuilds().size())));
        eb.setThumbnail(slashCommandInteractionEvent.getJDA().getSelfUser().getAvatarUrl());
        slashCommandInteractionEvent.replyEmbeds(eb.build()).queue();
    }
}
