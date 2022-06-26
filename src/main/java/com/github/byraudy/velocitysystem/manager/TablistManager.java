package com.github.byraudy.velocitysystem.manager;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.manager.config.Config;
import com.github.byraudy.velocitysystem.utils.Components;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import net.kyori.adventure.text.Component;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class TablistManager {

    private final Config config;

    public TablistManager(VelocitySystem velocitySystem) {
        this.config = velocitySystem.getConfigManager().getConfig();
    }

    @Subscribe
    public void handleLogin(LoginEvent event) {
        if(config.getBoolean("tablist")) {
            VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(current -> {
                Component headerComponent = Components.parse(config.getString("tablist_header").replace("%server%", current.getCurrentServer().get().getServerInfo().getName()).replace("%online_players%", VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().size() + "").replace("%max_players%", VelocitySystem.getVelocitySystem().getProxyServer().getConfiguration().getShowMaxPlayers() + ""));
                Component footerComponent = Components.parse(config.getString("tablist_footer"));

                current.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
            });
        }
    }

    @Subscribe
    public void handleDisconnect(DisconnectEvent event) {
        if(config.getBoolean("tablist")) {
            VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(current -> {
                Component headerComponent = Components.parse(config.getString("tablist_header").replace("%server%", current.getCurrentServer().get().getServerInfo().getName()).replace("%online_players%", VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().size() + "").replace("%max_players%", VelocitySystem.getVelocitySystem().getProxyServer().getConfiguration().getShowMaxPlayers() + ""));
                Component footerComponent = Components.parse(config.getString("tablist_footer"));

                current.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
            });
        }
    }

    @Subscribe
    public void handleServerPostConnect(ServerPostConnectEvent event) {
        if(config.getBoolean("tablist")) {
            VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(current -> {
                Component headerComponent = Components.parse(config.getString("tablist_header").replace("%server%", current.getCurrentServer().get().getServerInfo().getName()).replace("%online_players%", VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().size() + "").replace("%max_players%", VelocitySystem.getVelocitySystem().getProxyServer().getConfiguration().getShowMaxPlayers() + ""));
                Component footerComponent = Components.parse(config.getString("tablist_footer"));

                current.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
            });
        }
    }
}
