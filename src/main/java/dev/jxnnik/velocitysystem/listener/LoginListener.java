package dev.jxnnik.velocitysystem.listener;

import dev.jxnnik.velocitysystem.VelocitySystem;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Instagram: @byraudy
 */
public class LoginListener {

    @Subscribe
    public void handleLogin(LoginEvent event) {
        Player player = event.getPlayer();
        if (VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getBoolean("maintenance") && !player.hasPermission("velocitysystem.maintenancejoin"))
            player.disconnect(Component.text(VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("maintenance_message")));
        if (VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getBoolean("maintenance") && player.hasPermission("velocitysystem.admin"))
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("maintenance_joinmessage"));
        if (!VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().contains(player) && player.hasPermission("velocitysystem.team"))
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_loggedout", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));

        if (VelocitySystem.getVelocitySystem().getPunishManager().isBanned(player.getUniqueId())) {
            long current = System.currentTimeMillis();
            long end = VelocitySystem.getVelocitySystem().getPunishManager().getBanEnding(player.getUniqueId());
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                player.disconnect(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("ban_screen_message", VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("server_name"), VelocitySystem.getVelocitySystem().getPunishManager().getBanReasonFromPlayersUuid(player.getUniqueId()), VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(VelocitySystem.getVelocitySystem().getPunishManager().getBanEnding(player.getUniqueId()))));
            } else {
                VelocitySystem.getVelocitySystem().getPunishManager().unbanPlayer(player.getUniqueId(), null);
            }
        }
    }
}
