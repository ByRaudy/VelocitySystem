package dev.jxnnik.velocitysystem.manager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
public class Config {

    private final File file;
    private final Gson gson;
    private JsonObject json;

    public Config() {
        this.file = new File("plugins/VelocitySystem/config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.initFile();
    }

    private void initFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(file)) {
                initProperties();
                writer.print(gson.toJson(json));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
                this.json = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void initProperties() {
        json = new JsonObject();
        json.addProperty("maintenance", true);
        json.addProperty("maintenance_protocol", "§4Wartungsarbeiten");
        json.addProperty("maintenance_message", "§f§lDeinServer.net §8| §7Minecraft Netzwerk \n \n §8» §cDas Netzwerk befindet sich derzeit in Wartungsarbeiten§8.");
        json.addProperty("ping_info", true);
        json.addProperty("ping_info_message", "\n §f§lDeinServer.net §8| §7Minecraft Netzwerk \n \n §8» §fdc.DeinServer.net \n §8» §f@DeinServerNET \n");
        json.addProperty("gradients", false);
        json.addProperty("server_name", "DeinServer.net");
        json.addProperty("motd", true);
        json.addProperty("motd.line1", "§f§lDeinServer.net §8| §7Minecraft Netzwerk");
        json.addProperty("motd.line2", "  §8» §7Editiere die MOTD in der config.json§8!");
        json.addProperty("tablist", true);
        json.addProperty("tablist_header", "\n §f§lDeinServer.net §8| §7Minecraft Netzwerk \n §7Server §8» §f%server% \n §7Spieler §8» §f%online_players%§8/§f%max_players% \n");
        json.addProperty("tablist_footer", "\n §7Discord §8» §fdc.DeinServer.net \n §7Twitter §8» §f@DeinServerNET §7\n §8» §f§lVelocitySystem by ByRaudy \n");
        json.addProperty("lobby_name", "Lobby-1");
        json.addProperty("hub_command_enabled", true);
    }

    public void save() {
        Executors.newCachedThreadPool().execute(() -> {
            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8)) {
                gson.toJson(json, writer);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public String getString(String key) {
        return json.get(key).getAsString();
    }

    public boolean getBoolean(String key) {
        return json.get(key).getAsBoolean();
    }
}