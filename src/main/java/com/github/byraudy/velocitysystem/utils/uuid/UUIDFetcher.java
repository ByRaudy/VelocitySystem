package com.github.byraudy.velocitysystem.utils.uuid;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class UUIDFetcher {

    private static final LoadingCache<String, UUID> uuidCache;
    private static final LoadingCache<UUID, String> nameCache;
    private static final Gson gson;

    static {
        uuidCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(10000).build(new CacheLoader<>() {
            @Override
            public UUID load(String key) {
                return cacheUuid(key);
            }
        });

        nameCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(10000).build(new CacheLoader<>() {
            @Override
            public String load(UUID key) {
                return cacheName(key);
            }
        });

        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * Returns the player name
     *
     * @param uuid The uuid of the player
     * @return The player name
     */
    public static String getName(UUID uuid) {
        try {
            return nameCache.get(uuid);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "Name not found";
    }

    /**
     * Returns the player uuid
     *
     * @param name The name of the player
     * @return The player uuid
     */
    public static UUID getUUID(String name) {
        try {
            return uuidCache.get(name);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns the name of the player if there found
     *
     * @param uuid The uuid of the player
     * @return The name result
     */
    private static String cacheName(UUID uuid) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.minetools.eu/uuid/" + uuid.toString().replaceAll("-", "")))
                    .GET()
                    .timeout(Duration.ofMinutes(5))
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(response.body(), JsonObject.class).get("name").getAsString();
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return "Name not found";
    }

    /**
     * Returns the uuid of the player if there found
     *
     * @param name The name of the player
     * @return The uuid result
     */
    private static UUID cacheUuid(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.minetools.eu/uuid/" + name))
                    .GET()
                    .timeout(Duration.ofMinutes(5))
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

            return UUIDTypeAdapter.fromString(gson.fromJson(response.body(), JsonObject.class).get("id").getAsString());
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
