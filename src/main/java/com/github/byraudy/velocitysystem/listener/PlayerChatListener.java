package com.github.byraudy.velocitysystem.listener;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class PlayerChatListener {

    @Subscribe
    public void handlePlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("velocitysystem.teamchat") && event.getMessage().startsWith("@Team ")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            if (!VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().contains(player)) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_loggedout", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
                return;
            }
            VelocitySystem.getVelocitySystem().getTeamChatManager().sendTeamChatMessage(event);
            return;
        } else if (player.hasPermission("velocitysystem.teamchat") && event.getMessage().startsWith("@Team")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            if (!VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().contains(player)) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_loggedout", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
                return;
            }
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_error", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
            return;
        }

        if (VelocitySystem.getVelocitySystem().getPunishManager().isMuted(player.getUniqueId())) {
            long current = System.currentTimeMillis();
            long end = VelocitySystem.getVelocitySystem().getPunishManager().getMuteEnding(player.getUniqueId());
            if (((current < end ? 1 : 0) | (end == -1L ? 1 : 0)) != 0) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_player_message", VelocitySystem.getVelocitySystem().getPunishManager().getMuteReasonFromPlayersUuid(player.getUniqueId()), VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(VelocitySystem.getVelocitySystem().getPunishManager().getMuteEnding(player.getUniqueId()))));
                event.setResult(PlayerChatEvent.ChatResult.denied());
            } else {
                VelocitySystem.getVelocitySystem().getPunishManager().unmutePlayer(player.getUniqueId(), null);
            }
        }
    }
}
