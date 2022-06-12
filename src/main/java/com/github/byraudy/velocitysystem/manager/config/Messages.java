package com.github.byraudy.velocitysystem.manager.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy

Class by NachGecodet
 */
public class Messages {

    private final File file;
    private final Gson gson;
    private final ExecutorService pool;
    private JsonObject json;

    public Messages() {
        this.file = new File("plugins/VelocitySystem/messages.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.pool = Executors.newFixedThreadPool(2);
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
            try {
                json = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initProperties() {
        json = new JsonObject();
        json.addProperty("prefix", "§8[§f§lVelocitySystem§8] §7");
        json.addProperty("maintenance_joinmessage", "§cDas Netzwerk ist derzeit in Wartungsarbeiten§8.");
        json.addProperty("ping_message", "§7Dein Ping beträgt zurzeit §f§l{0}ms§8.");
        json.addProperty("hub_message", "§7Es wird versucht§8, §7dich mit {0} zu verbinden§8!");
        json.addProperty("hub_message_complete", "§7Du wurdest erfolgreich mit §f§l{0} §7verbunde§8.");
        json.addProperty("hub_message_error", "§7Du bist bereit mit einem §f§lLobby-Server §7verbunden§8.");
        json.addProperty("end_message_help", "§7Nutze§8: §7/end <instant/after> <secounds>");
        json.addProperty("message_end", "§7Der Proxy wird in kürze §f§lstoppen§8...");
        json.addProperty("message_end_screen", "§cDer Proxy wurde gestoppt§8!");
        json.addProperty("message_end_secounds", "§7Bitte wähle eine §f§lgültige §7Sekundenzahl§8!");
        json.addProperty("end_title", "§7Der Proxy stoppt in §c§l{0} §7Sekunden§8.");
        json.addProperty("kick_help_message", "§7Nutze§8: §7/kick <Spieler> <Grund>");
        json.addProperty("kick_cant_player_message", "§7Du darfst diesen Spieler nicht kicken§8!");
        json.addProperty("kick_player_message", "§c{0} §7wurde von §4{1} §7für §e{2} §7gekickt§8!");
        json.addProperty("kick_screen", "§8« {0} §8- §7KickSystem §8» \n §7Du wurdest vom {0} §7Netzwerk §cgekickt§8! \n §8§m                                §r\n \n §7Grund§8: §c{1} \n \n §8§m                                §r");
        json.addProperty("ban_help_message", "§7Nutze§8: §7/ban <Spieler> <Grund>");
        json.addProperty("ban_cant_player_message", "§7Du darfst diesen Spieler nicht bannen§8!");
        json.addProperty("ban_screen_message", "§8« {0} §8- §7BanSystem §8» \n §7Du wurdest vom {0} §7Netzwerk §cgebannt§8! \n §8§m                                §r\n \n §7Grund§8: §c{1} \n §7Dauer§8: §c{2} \n \n §8§m                                §r");
        json.addProperty("ban_message", "§c{0} §7wurde von §4{1} §7wegen §b{2} §7für §b{3} §7gebannt§8!");
        json.addProperty("ban_is_already_banned_message", "§cDieser Spieler ist bereits §cgebannt§8!");
        json.addProperty("mute_help_message", "§7Nutze§8: §7/mute <Spieler> <Grund>");
        json.addProperty("mute_message", "§c{0} §7wurde von §4{1} §7wegen §b{2} §7für §b{3} §7gemutet§8!");
        json.addProperty("mute_is_already_muted_message", "§cDieser Spieler ist bereits §cgemutet§8!");
        json.addProperty("mute_player_message", "§7Du wurdest wegen §c{0} §7für §4{1} §7aus dem Chat gebannt§8!");
        json.addProperty("unban_help_message", "§7Nutze§8: §7/unban <Spieler>");
        json.addProperty("unmute_help_message", "§7Nutze§8: §7/unmute <Spieler>");
        json.addProperty("unban_player_is_not_banned_message", "§7Dieser Spieler ist §cnicht §7gebannt§8.");
        json.addProperty("unmute_player_is_not_muted_message", "§7Dieser Spieler ist §cnicht §7gemuted§8.");
        json.addProperty("unban_message", "§c{0} §7wurde von §4{1} §7enbannt§8!");
        json.addProperty("unmute_message", "§c{0} §7wurde von §4{1} §7entmutet§8!");
        json.addProperty("check_player_loading_message", "§7Spieler wird gecheckt§8, §7bitte warten§8...");
        json.addProperty("check_player_ban_yes_message", "§c{0} §7ist von §4{1} §7wegen §e{2} §7für §b{3} §7gebannt§8.");
        json.addProperty("check_player_ban_no_message", "§c{0} §7ist derzeit nicht gebannt§8.");
        json.addProperty("check_player_mute_yes_message", "§c{0} §7ist von §4{1} §7wegen §e{2} §7für §b{3} §7gemutet§8.");
        json.addProperty("check_player_mute_no_message", "§c{0} §7ist derzeit nicht gemutet§8.");
    }

    public String getPrefix() {
        return json.get("prefix").getAsString();
    }

    public Component getMessage(String message, Object... arguments) {
        return Component.text(getPrefix() + new MessageFormat(json.get(message).getAsString()).format(arguments));
    }

    public Component getMessageWithoutPrefix(String message, Object... arguments) {
        return Component.text(new MessageFormat(json.get(message).getAsString()).format(arguments));
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
}