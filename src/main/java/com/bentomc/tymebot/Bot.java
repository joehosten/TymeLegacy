package com.bentomc.tymebot;

import com.bentomc.tymebot.components.admin.CommandListGuilds;
import com.bentomc.tymebot.components.admin.CommandListUsers;
import com.bentomc.tymebot.components.admin.CommandMaintenance;
import com.bentomc.tymebot.components.admin.CommandReload;
import com.bentomc.tymebot.components.admin.blacklist.CommandBlacklist;
import com.bentomc.tymebot.components.admin.blacklist.CommandUnBlacklist;
import com.bentomc.tymebot.components.guild.ServerJoinListener;
import com.bentomc.tymebot.components.guild.ServerLeaveListener;
import com.bentomc.tymebot.components.misc.BotMentionListener;
import com.bentomc.tymebot.components.misc.CommandDiscord;
import com.bentomc.tymebot.components.misc.CommandInfo;
import com.bentomc.tymebot.components.misc.CommandInvite;
import com.bentomc.tymebot.components.misc.help.CommandHelp;
import com.bentomc.tymebot.components.server.ServerManager;
import com.bentomc.tymebot.components.server.ServerManagerImpl;
import com.bentomc.tymebot.components.status.DiscordStatusRunnable;
import com.bentomc.tymebot.config.TymeConfig;
import com.bentomc.tymebot.core.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.negative.framework.discord.DiscordBot;
import games.negative.framework.discord.runnable.RepeatingRunnable;
import games.negative.framework.discord.runnable.Scheduler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.Presence;
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
    private final ServerManager serverManager;
    @Getter
    private final String version = "1.0";
    private final String databasePassword;
    File file;
    //    @Getter
//    Database database;
    @Getter
    private TymeConfig config = null;

    @SneakyThrows
    public Bot() {
        instance = this;

        // Config loader
        file = new File("config", "main.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        if (!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();

            this.config = new TymeConfig();
            saveJson(file, gson);
        }

        try (FileReader reader = new FileReader(file)) {
            config = gson.fromJson(reader, TymeConfig.class);
            saveJson(file, gson);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bot builder
        JDABuilder builder = create(config.getBotToken(), List.of(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES));

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("over the server!"));

        // commands
        registerGlobalCommand(new CommandListGuilds(this));
        registerGlobalCommand(new CommandInfo(this));
        registerGlobalCommand(new CommandListUsers());
        registerGlobalCommand(new CommandHelp(this));
        registerGlobalCommand(new CommandDiscord());
        registerGlobalCommand(new CommandInvite());

        String parentServer = config.getParentServer();
        if (parentServer != null) {
            registerServerCommand(parentServer, new CommandReload(this));
            registerServerCommand(parentServer, new CommandBlacklist(this));
            registerServerCommand(parentServer, new CommandUnBlacklist(this));
            registerServerCommand(parentServer, new CommandMaintenance(this));
        }

        // listeners
        builder.addEventListeners(new ServerJoinListener(this), new ServerLeaveListener(), new BotMentionListener(this));

        jda = builder.build().awaitReady();
        initializeCommands(jda);
        serverManager = new ServerManagerImpl(this);

        // will change this when mongo is connected. If config value is set to "TymeBeta" etc, it will load the correct database
        // and status correctly.
        Presence presence = jda.getPresence();
        switch (getJda().getSelfUser().getName()) {
            case "TymeBeta" -> presence.setStatus(OnlineStatus.DO_NOT_DISTURB);
            case "Tyme" -> presence.setStatus(OnlineStatus.ONLINE);
            case "TymeCanary" -> presence.setStatus(OnlineStatus.IDLE);
            default -> throw new IllegalStateException("Unexpected value: " + getJda().getSelfUser().getName());
        }


        // init config
        config.setDevIds(new String[]{"462296411141177364", "452520883194429440"});
        saveJson(file, gson);
        devIds = config.getDevIds();
        databasePassword = config.getPassword();

        // database init
//        buildDatabase();

        Scheduler scheduler = getScheduler();
        scheduler.run(new DiscordStatusRunnable(this), 1000L, 1000L * config.getDiscordStatusInterval());
        scheduler.run(new CacheDataRunnable(), 0L, 1000L * 60L);
    }

    @SneakyThrows
    private void saveJson(File file, Gson gson) {
        Writer writer = new FileWriter(file, false);
        gson.toJson(this.config, writer);
        writer.flush();
        writer.close();
    }

    @SneakyThrows
    public void saveConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        Writer writer = new FileWriter(new File("config", "main.json"), false);
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

    public LimitedStatus isLimited(String userId, String guildId) {
        // Check if bot is in maintenance mode
        if (config.isMaintenanceMode()) return LimitedStatus.MAINTENANCE;

        // Check if user is blacklisted
        User user = jda.getUserById(userId);
        if ((user != null) && (config.getBlacklistedUserIds() != null) && config.getBlacklistedUserIds().contains(userId))
            return LimitedStatus.USERBLACKLIST;

        // Check if guild is blacklisted
        Guild guild = jda.getGuildById(guildId);
        if ((guild != null) && (config.getBlacklistedGuildIds() != null) && config.getBlacklistedGuildIds().contains(guildId))
            return LimitedStatus.GUILDBLACKLIST;

        return LimitedStatus.NOT_LIMITED;
    }

    public String getUptime() {
        long totalSeconds = System.currentTimeMillis() - startTime;
        return Utils.makePrettyTime(totalSeconds);
    }

//    private void buildDatabase() {
//        database = new Database("jp.hypews.com", 3306, "u63_HCPw9nc4dx", databasePassword, "s63_tymebeta");
//        database.connect();
//
//        // Users table
//        try {
//            if (!database.tableExists("users")) {
//                // create the columns
//                List<Column> columns = new ArrayList<>();
//                Column id = new Column(ColumnType.VARCHAR, "id");
//                Column balance = new Column(ColumnType.INT, "balance");
//                Column tokens = new Column(ColumnType.INT, "tokens");
//                Column job = new Column(ColumnType.VARCHAR, "job");
//                Column titles = new Column(ColumnType.VARCHAR, "titles");
//                columns.add(id);
//                columns.add(balance);
//                columns.add(tokens);
//                columns.add(job);
//                columns.add(titles);
//
//                // create the table based on the columns
//                TableBuilder userTableBuilder = new TableBuilder("users", columns);
//                try {
//                    database.createTable(userTableBuilder);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        // Jobs table
//        // Eco table
//        // Titles table
//        // Inventories table
//    }

    @RequiredArgsConstructor
    public enum LimitedStatus {
        MAINTENANCE("Tyme is currently in maintenance mode. You cannot use this command."), USERBLACKLIST("You are blacklisted from using Tyme. Please join the `/discord` for more information."), GUILDBLACKLIST("This guild is blacklisted from using Tyme. Please ask a server administrator to join the `/discord` for more information."), NOT_LIMITED(null);

        public final String message;
    }

    private static class CacheDataRunnable implements RepeatingRunnable {
        @Override
        public void execute() {
            System.out.println("Caching configuration and servers..");
            Bot.getInstance().reloadConfig();
        }
    }

}
