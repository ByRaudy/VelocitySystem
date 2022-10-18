package dev.jxnnik.velocitysystem.manager;

import dev.jxnnik.velocitysystem.VelocitySystem;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
@Getter
public class TeamChatManager {

    private final List<Player> notifiedPlayerList;

    public TeamChatManager() {
        this.notifiedPlayerList = new ArrayList<>();
    }

    public void sendTeamChatMessage(PlayerChatEvent playerChatEvent) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            if (player.hasPermission("velocitysystem.team") && notifiedPlayerList.contains(player))
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_message", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix(), playerChatEvent.getPlayer().getUsername(), playerChatEvent.getPlayer().getCurrentServer().get().getServerInfo().getName(), playerChatEvent.getMessage().replace("@Team ", "")));
        });
    }
}
