package bot.tymebot;

import bot.tymebot.components.admin.CommandListGuilds;
import bot.tymebot.components.admin.CommandListUsers;
import bot.tymebot.components.admin.CommandReload;
import bot.tymebot.components.guild.ServerJoinListener;
import bot.tymebot.components.misc.CommandInfo;
import bot.tymebot.components.server.ServerManager;
import bot.tymebot.components.server.ServerManagerImpl;
import bot.tymebot.components.status.DiscordStatusRunnable;
import bot.tymebot.config.TymeConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.negative.framework.discord.DiscordBot;
import games.negative.framework.discord.runnable.Scheduler;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

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
    private TymeConfig config = null;

    @Getter
    private final ServerManager serverManager;

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
        registerGlobalCommand(new CommandInfo());
        registerGlobalCommand(new CommandListUsers());

        String parentServer = config.getParentServer();
        if (parentServer != null) {
            registerServerCommand(parentServer, new CommandReload(this));
        }
        // listeners
        builder.addEventListeners(new ServerJoinListener());

        jda = builder.build().awaitReady();
        initializeCommands(jda);
        serverManager = new ServerManagerImpl(this);

        // init config
        config.setDevIds(new String[]{"462296411141177364", "452520883194429440"});
        saveConfig(file, gson);
        devIds = config.getDevIds();

        Scheduler scheduler = getScheduler();
        scheduler.run(new DiscordStatusRunnable(this), 1000L, 1000L * config.getDiscordStatusInterval());
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
        if (!file.exists())
            return;

        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        try (FileReader reader = new FileReader(file)) {
            config = gson.fromJson(reader, TymeConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
