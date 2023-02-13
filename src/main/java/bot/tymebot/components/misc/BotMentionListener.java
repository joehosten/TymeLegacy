package bot.tymebot.components.misc;

import bot.tymebot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotMentionListener extends ListenerAdapter {

    private final Bot bot;

    public BotMentionListener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String raw = e.getMessage().getContentRaw();
        if (raw.contains("<@1068309555915788288>")) {
            EmbedBuilder helpEmbed = new EmbedBuilder();
            helpEmbed.setTitle("Tyme Help Menu");
            helpEmbed.setDescription("Use this menu to navigate throughout the various help menus for Tyme.\n\n*Some commands may not be visible depending on your level and/or status.*");
            helpEmbed.addField("Basic Information"
                    , """
                            __*Rules*__
                            In order to use Tyme, you must agree to our `/rules`.
                            Tyme is intended to be used to participate with members between servers and to do that, we need to ensure there is a safe environment for everyone.
                                                    
                            __*Features*__
                            Tyme is a multi-functioning bot owned by Negative Games and created by `joee#1107`. It has features for server administration, communities and the main purpose: *a cross-server trading game*.
                            With lots of commands and modules built into Tyme, it is one of the most versatile bots out there.
                                                    
                            __*Cross-server events*__
                            From time-to-time, the Admins of Tyme will host real-time events which anyone across Discord can participate in. Make sure you watch your DM's and configured bot-channels for these events.
                            """, false);
            helpEmbed.addField("Help Menu"
                    , """
                            __*How to use the Help Menu*__
                            > You can use the `/help` command to view all the different menus and go to them directly.
                                                    
                            We have tried to make Tyme as user friendly as possible. If you have any suggestions, please join the `/discord` or use the `/botsuggestion` command.
                            """, false);
            helpEmbed.setThumbnail(e.getMessage().getAuthor().getAvatarUrl());
            helpEmbed.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
            e.getMessage().replyEmbeds(helpEmbed.build()).queue();

        }
    }
}
