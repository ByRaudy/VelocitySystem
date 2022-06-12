package com.github.byraudy.velocitysystem.commands;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.utils.uuid.UUIDFetcher;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class HubCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        Player player = (Player) invocation.source();

        if (!player.getCurrentServer().get().getServerInfo().getName().equals(VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("lobby_name"))) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("hub_message", VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("lobby_name")));
            player.createConnectionRequest(VelocitySystem.getVelocitySystem().getProxyServer().getServer(VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("lobby_name")).get()).connect();
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("hub_message_complete", VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("lobby_name")));
        } else {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("hub_message_error"));
        }
    }
}
