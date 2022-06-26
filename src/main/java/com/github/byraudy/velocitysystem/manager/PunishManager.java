package com.github.byraudy.velocitysystem.manager;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.utils.uuid.UUIDFetcher;
import com.velocitypowered.api.proxy.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class PunishManager {

    public void banPlayer(UUID uuid, String playername, String reason, long seconds, Player banner) {
        long end = calculateEnd(seconds);

        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            if (player.hasPermission("velocitysystem.ban.see")) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ban_message", playername, banner.getUsername(), reason, getReamainingTime(end)));
            }
        });
        VelocitySystem.getVelocitySystem().getMySQLManager().updateDatabase("INSERT INTO bannedPlayerData (playerName, playerUuid, time, reason, bannersName) VALUES ('" + playername + "','" + uuid + "','" + end + "','" + reason + "','" + banner.getUsername() + "')");
    }

    @SuppressWarnings("deprecation")
    public void mutePlayer(UUID uuid, String playername, String reason, long seconds, Player muter) {
        long end = calculateEnd(seconds);
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            if (player.hasPermission("velocitysystem.ban.see")) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_message", playername, muter.getUsername(), reason, getReamainingTime(end)));
            }
        });

        VelocitySystem.getVelocitySystem().getMySQLManager().updateDatabase("INSERT INTO mutedPlayerData (playerName, playerUuid, time, reason, bannersName) VALUES ('" + playername + "','" + uuid + "','" + end + "','" + reason + "','" + muter.getUsername() + "')");
    }

    public void unbanPlayer(UUID uuid, Player player) {
        VelocitySystem.getVelocitySystem().getMySQLManager().updateDatabase("DELETE FROM bannedPlayerData WHERE playerUuid='" + uuid + "'");

        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(players -> {
            if (players.hasPermission("velocitysystem.ban.see")) {
                players.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("unban_message", UUIDFetcher.getName(uuid), player != null ? player.getUsername() : "SYSTEM"));
            }
        });
    }

    public void unmutePlayer(UUID uuid, Player player) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(players -> {
            if (players.hasPermission("velocitysystem.ban.see")) {
                players.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("unmute_message", UUIDFetcher.getName(uuid), player != null ? player.getUsername() : "SYSTEM"));
            }
        });

        VelocitySystem.getVelocitySystem().getMySQLManager().updateDatabase("DELETE FROM mutedPlayerData WHERE playerUuid='" + uuid + "'");
    }

    public boolean isBanned(UUID uuid) {
        try (ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT time FROM bannedPlayerData WHERE playerUuid='" + uuid + "'")) {
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public boolean isMuted(UUID uuid) {
        try (ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT time FROM mutedPlayerData WHERE playerUuid='" + uuid + "'")) {
            return resultSet.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public String getBanReasonFromPlayersUuid(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM bannedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getMuteReasonFromPlayersUuid(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM mutedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getBannersNameFromPlayersUuid(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM bannedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getString("bannersName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getMuterFromPlayersUuid(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM mutedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getString("bannersName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public Long getBanEnding(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM bannedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Long getMuteEnding(UUID uuid) {
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM mutedPlayerData WHERE playerUuid='" + uuid + "'");
        try {
            if (resultSet.next()) {
                return resultSet.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<String> getbannedPlayerDataFromPlayer(String name) {
        ArrayList<String> list = new ArrayList<>();
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM bannedPlayerData WHERE bannersName='" + name + "'");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString("bannersName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getmutedPlayerDataFromPlayer(String name) {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM mutedPlayerData WHERE bannersName='" + name + "'");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString("bannersName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getBannedPlayerData() {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM bannedPlayerData");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString("playerName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List<String> getmutedPlayerData() {
        ArrayList<String> list = new ArrayList<String>();
        ResultSet resultSet = VelocitySystem.getVelocitySystem().getMySQLManager().getDatabaseResult("SELECT * FROM mutedPlayerData");
        try {
            while (resultSet.next()) {
                list.add(resultSet.getString("playerName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private long calculateEnd(long seconds) {
        if (seconds == -1) {
            return -1;
        } else {
            long current = System.currentTimeMillis();
            long millis = seconds * 1000;
            return current + millis;
        }
    }

    public String getReamainingTime(long time) {
        long current = System.currentTimeMillis();
        long end = time;
        if (end == -1) {
            return "§c§lPERMANENT";
        }
        long millis = end - current;
        long seconds = 0;
        long minutes = 0;
        long hours = 0;
        long days = 0;
        long weeks = 0;
        while (millis > 1000) {
            millis -= 1000;
            ++seconds;
        }
        while (seconds > 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes > 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours > 24) {
            hours -= 24;
            ++days;
        }
        while (days > 7) {
            days -= 7;
            ++weeks;
        }
        return "§4§l" + weeks + " §7Wochen§8, §4§l" + days + " §7Tage§8, §4§l" + hours + " §7Stunden und §4§l" + minutes + " §7Minuten";
    }
}
