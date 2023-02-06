package bot.tymebot;

import bot.tymebot.components.admin.CommandListGuilds;
import bot.tymebot.components.admin.CommandListUsers;
import bot.tymebot.components.admin.CommandLogout;
import bot.tymebot.components.admin.CommandReload;
import bot.tymebot.components.guild.ServerJoinListener;
import bot.tymebot.components.guild.ServerLeaveListener;
import bot.tymebot.components.misc.CommandInfo;
import bot.tymebot.components.server.ServerManager;
import bot.tymebot.components.server.ServerManagerImpl;
import bot.tymebot.components.status.DiscordStatusRunnable;
import bot.tymebot.config.TymeConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.negative.framework.discord.DiscordBot;
import games.negative.framework.discord.runnable.RepeatingRunnable;
import games.negative.framework.discord.runnable.Scheduler;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class Bot extends DiscordBot {


    @Getter
    private static final long startTime = System.currentTimeMillis();
    @Getter
    private static Bot instance;
    @Getter
    private static String[] devIds;
    private final JDA jda;
    @Getter
    private final ServerManager serverManager;
    @Getter
    private TymeConfig config = null;

    @SneakyThrows
    public Bot() {
        instance = this;

        // Config loader
        File file = new File("config", "main.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();

            this.config = new TymeConfig();
            saveConfig(file, gson);
        }

        try (FileReader reader = new FileReader(file)) {
            config = gson.fromJson(reader, TymeConfig.class);
            saveConfig(file, gson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bot builder
        JDABuilder builder = create(config.getBotToken(), List.of(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES));

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("over the server!"));

        // commands
        registerGlobalCommand(new CommandListGuilds());
        registerGlobalCommand(new CommandInfo(this));
        registerGlobalCommand(new CommandListUsers());

        String parentServer = config.getParentServer();
        if (parentServer != null) {
            registerServerCommand(parentServer, new CommandReload(this));
            registerServerCommand(parentServer, new CommandLogout(this));
        }

        // listeners
        builder.addEventListeners(new ServerJoinListener(this), new ServerLeaveListener(this));

        jda = builder.build().awaitReady();
        initializeCommands(jda);
        serverManager = new ServerManagerImpl(this);

        // init config
        config.setDevIds(new String[]{"462296411141177364", "452520883194429440"});
        saveConfig(file, gson);
        devIds = config.getDevIds();

        Scheduler scheduler = getScheduler();
        scheduler.run(new DiscordStatusRunnable(this), 1000L, 1000L * config.getDiscordStatusInterval());
        scheduler.run(new CacheDataRunnable(), 0L, 1000L * 60L);
    }

    @SneakyThrows
    private void saveConfig(File file, Gson gson) {
        Writer writer = new FileWriter(file, false);
        gson.toJson(this.config, writer);
        writer.flush();
        writer.close();
    }

    public void reloadConfig() {
        File file = new File("config", "main.json");
        // Should not be possible but whatever.
        if (!file.exists()) return;

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try (FileReader reader = new FileReader(file)) {
            config = gson.fromJson(reader, TymeConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBlacklisted(String userId, String guildId) {

        // check if user is blacklisted
        if (config.getBlacklistedUserIds() != null) {
            System.out.println(1);
            System.out.println(Arrays.toString(config.getBlacklistedUserIds()));
            return jda.getUserById(userId) != null &&
                    Arrays.asList(config.getBlacklistedUserIds()).contains(userId);
        }

        // if user is not blacklisted, then check if the guild is blacklisted
        Guild guild = jda.getGuildById(guildId);
        if (guild != null) {
            System.out.println(2);
            System.out.println(serverManager.getServer(guild));
            return Objects.requireNonNull(serverManager.getServer(guildId)).isBlacklisted();
        }

        return false;
    }

    public String getUptime() {
        long totalSeconds = (System.currentTimeMillis() - startTime) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = (totalSeconds / 3600);
        return (hours < 10 ? "0" + hours : hours) + "h " + (minutes < 10 ? "0" + minutes : minutes) + "m " + (seconds < 10 ? "0" + seconds : seconds) + "s";
    }

    private static class CacheDataRunnable implements RepeatingRunnable {
        @Override
        public void execute() {
            System.out.println("Caching configuration and servers..");
            Bot.getInstance().getServerManager().reCacheServers();
            Bot.getInstance().reloadConfig();
        }
    }


}
