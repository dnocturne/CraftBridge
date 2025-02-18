package me.dnoc.craftBridge;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;

public final class CraftBridge extends JavaPlugin {
    private static final int DEFAULT_PORT = 4567;
    private final Gson gson = new Gson();
    private String apiKey;
    private int port;

    @Override
    public void onEnable() {
        // Load config
        saveDefaultConfig();
        loadConfiguration();

        // Configure Spark
        Spark.port(port);
        Spark.threadPool(8);

        // Configure Spark to use System.out instead of slf4j
        Spark.before((request, response) -> {
            org.eclipse.jetty.util.log.Log.setLog(new org.eclipse.jetty.util.log.JavaUtilLog());
        });

        // Add CORS headers - Move this before authentication middleware
        Spark.options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Max-Age", "3600");
            return "OK";
        });

        Spark.before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            res.header("Access-Control-Allow-Headers", "*");
            if (req.requestMethod().equals("OPTIONS")) {
                Spark.halt(200);
            }
        });

        // Add authentication middleware
        Spark.before((req, res) -> {
            if (req.requestMethod().equals("OPTIONS")) {
                return;
            }
            String providedKey = req.headers("X-API-Key");
            if (!apiKey.equals(providedKey)) {
                Spark.halt(401, gson.toJson(new Response("Unauthorized")));
            }
        });
        
        // Define endpoints
        setupEndpoints();

        getLogger().info("CraftBridge API started on port " + port);
    }

    private void loadConfiguration() {
        FileConfiguration config = getConfig();
        config.addDefault("port", DEFAULT_PORT);
        config.addDefault("apiKey", "change-this-key");
        config.options().copyDefaults(true);
        saveConfig();

        port = config.getInt("port");
        apiKey = config.getString("apiKey");
    }

    private void setupEndpoints() {
        // Server status endpoint
        Spark.get("/status", (req, res) -> {
            res.type("application/json");
            return gson.toJson(new ServerStatus(
                getServer().getOnlinePlayers().size(),
                getServer().getMaxPlayers(),
                getServer().getVersion()
            ));
        });

        // List online players
        Spark.get("/players", (req, res) -> {
            res.type("application/json");
            return gson.toJson(getServer().getOnlinePlayers().stream()
                .map(player -> new PlayerInfo(
                    player.getName(),
                    player.getUniqueId().toString(),
                    player.getHealth(),
                    player.getLevel(),
                    player.getLocation().getWorld().getName()
                ))
                .collect(Collectors.toList()));
        });

        // Execute console command
        Spark.post("/execute", (req, res) -> {
            res.type("application/json");
            String command = gson.fromJson(req.body(), Command.class).command;
            
            if (command == null || command.isEmpty()) {
                res.status(400);
                return gson.toJson(new Response("Command cannot be empty"));
            }

            getServer().getScheduler().runTask(this, () -> 
                getServer().dispatchCommand(getServer().getConsoleSender(), command)
            );

            return gson.toJson(new Response("Command executed successfully"));
        });

        // Broadcast message
        Spark.post("/broadcast", (req, res) -> {
            res.type("application/json");
            String message = gson.fromJson(req.body(), Message.class).message;
            
            if (message == null || message.isEmpty()) {
                res.status(400);
                return gson.toJson(new Response("Message cannot be empty"));
            }

            getServer().broadcast(net.kyori.adventure.text.Component.text(message));
            return gson.toJson(new Response("Message broadcast successfully"));
        });
    }

    @Override
    public void onDisable() {
        Spark.stop();
        getLogger().info("CraftBridge API stopped");
    }

    // Data classes
    private static class ServerStatus {
        public final int onlinePlayers;
        public final int maxPlayers;
        public final String version;
        
        public ServerStatus(int onlinePlayers, int maxPlayers, String version) {
            this.onlinePlayers = onlinePlayers;
            this.maxPlayers = maxPlayers;
            this.version = version;
        }
    }

    private static class PlayerInfo {
        public final String name;
        public final String uuid;
        public final double health;
        public final int level;
        public final String world;

        public PlayerInfo(String name, String uuid, double health, int level, String world) {
            this.name = name;
            this.uuid = uuid;
            this.health = health;
            this.level = level;
            this.world = world;
        }
    }

    private static class Command {
        private String command;
    }

    private static class Message {
        private String message;
    }

    private static class Response {
        private final String message;
        
        public Response(String message) {
            this.message = message;
        }
    }
}
