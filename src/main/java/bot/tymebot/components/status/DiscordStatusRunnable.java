package bot.tymebot.components.status;

import bot.tymebot.Bot;
import bot.tymebot.config.TymeConfig;
import bot.tymebot.core.util.TextBuilder;
import bot.tymebot.core.util.Utils;
import games.negative.framework.discord.runnable.RepeatingRunnable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.managers.Presence;

import java.util.List;

public class DiscordStatusRunnable implements RepeatingRunnable {

    private final Bot bot;
    private int count = 0;

    public DiscordStatusRunnable(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void execute() {
        JDA jda = bot.getJda();
        TymeConfig config = bot.getConfig();

        List<DiscordStatus> statuses = config.getStatuses();

        DiscordStatus status = statuses.get(count);

        String text = status.getText();
        String formatted = new TextBuilder(text)
                .replace("%users%", Utils.decimalFormat(jda.getUsers().size()))
                .replace("%guilds%", Utils.decimalFormat(jda.getGuilds().size())).build();

        Presence presence = jda.getPresence();
        presence.setActivity(Activity.of(status.getType(), formatted));


        count++;

        if (count >= statuses.size())
            count = 0;
    }


}
