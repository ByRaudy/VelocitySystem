package com.github.byraudy.velocitysystem.manager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class MySQL {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public MySQL() {
        this.file = new File("plugins/VelocitySystem/mysql.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pool = Executors.newFixedThreadPool(2);
        this.initFile();
    }

    private void initFile() {
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (final PrintWriter writer = new PrintWriter(file)) {
                writer.print(gson.toJson(json = new JsonObject()));
                initProperties();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                json = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initProperties() {
        json.addProperty("mysql_host", "127.0.0.1");
        json.addProperty("mysql_port", "3306");
        json.addProperty("mysql_database", "proxysystem");
        json.addProperty("mysql_user", "user");
        json.addProperty("mysql_password", "password");
        save();
    }

    public void save() {
        pool.execute(() -> {
            try (final PrintWriter writer = new PrintWriter(file)) {
                writer.print(gson.toJson(json));
                writer.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
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