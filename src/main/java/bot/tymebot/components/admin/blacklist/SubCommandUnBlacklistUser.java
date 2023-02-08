package bot.tymebot.components.admin.blacklist;

import bot.tymebot.Bot;
import bot.tymebot.core.util.UtilsUser;
import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

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
        slashCommandInteractionEvent.reply("Removed `%user%` from the blacklist.".replace("%user%", Objects.requireNonNull(bot.getJda().getUserById(id)).getName())).setEphemeral(true).queue();
        bot.getConfig().setBlacklistedUserIds(guilds);
        bot.saveConfig();
    }
}
