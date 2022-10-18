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
public class MySQL {

    private final File file;
    private final Gson gson;
    private JsonObject json;

    public MySQL() {
        this.file = new File("plugins/VelocitySystem/mysql.json");
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
        json.addProperty("mysql_host", "127.0.0.1");
        json.addProperty("mysql_port", "3306");
        json.addProperty("mysql_database", "proxysystem");
        json.addProperty("mysql_user", "user");
        json.addProperty("mysql_password", "password");
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

    public int getInt(String key) {
        return json.get(key).getAsInt();
    }
}