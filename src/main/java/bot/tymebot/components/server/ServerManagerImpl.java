package bot.tymebot.components.server;

import bot.tymebot.Bot;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import games.negative.framework.discord.runnable.RepeatingRunnable;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;

public class ServerManagerImpl implements ServerManager {

    private final Map<String, DiscordServer> servers;
    private final RepeatingRunnable autoSave;

    public ServerManagerImpl(Bot bot) {
        this.servers = loadServers();
        this.autoSave = bot.getScheduler().run(new DiscordServerSaveRunnable(), 1000L * 15, 1000L * 60 * 30);
    }

    private Map<String, DiscordServer> loadServers() {
        File dir = new File("servers");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0)
            return Maps.newHashMap();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        Map<String, DiscordServer> servers = Maps.newHashMap();
        for (File file : files) {
            if (!file.getName().endsWith(".data")) continue;

            try (Reader reader = new FileReader(file)) {
                DiscordServer server = gson.fromJson(reader, DiscordServerImpl.class);
                servers.put(server.getServerID(), server);
            } catch (IOException e) {
                System.out.println("Could not load data from file " + file.getName());
                throw new RuntimeException(e);
            }
        }
        System.out.println("Loaded " + servers.size() + " servers");
        return servers;
    }

    @Override
    public Map<String, DiscordServer> getServers() {
        return servers;
    }

    @Nullable
    @Override
    public DiscordServer getServer(@NotNull String id) {
        return servers.getOrDefault(id, null);
    }

    @Override
    public DiscordServer addServer(@NotNull Guild guild) {
        File dir = new File("servers");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, guild.getId() + ".data");
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
                DiscordServer server = gson.fromJson(reader, DiscordServerImpl.class);
                servers.put(server.getServerID(), server);
                return server;
            } catch (IOException e) {
                System.out.println("Could not load data from file " + file.getName());
                throw new RuntimeException(e);
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create file " + file.getName());
                throw new RuntimeException(e);
            }

            DiscordServer server = new DiscordServerImpl(guild.getId(), System.currentTimeMillis());
            servers.put(server.getServerID(), server);

            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            try (Writer writer = new FileWriter(file)) {
                gson.toJson(server, writer);
            } catch (IOException e) {
                System.out.println("Could not save data to file " + file.getName());
                throw new RuntimeException(e);
            }

            return server;
        }
    }

    @Override
    public void removeServer(@NotNull String id) {
        File dir = new File("servers");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, id + ".data");
        if (file.exists()) {
            file.delete();
        }

        servers.remove(id);
    }

    private class DiscordServerSaveRunnable implements RepeatingRunnable{

        @Override
        public void execute() {
            File dir = new File("servers");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
            for (DiscordServer server : servers.values()) {
                File file = new File(dir, server.getServerID() + ".data");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.out.println("Could not create file " + file.getName());
                        throw new RuntimeException(e);
                    }
                }

                try (Writer writer = new FileWriter(file)) {
                    gson.toJson(server, writer);
                } catch (IOException e) {
                    System.out.println("Could not save data to file " + file.getName());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
