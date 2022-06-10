package com.github.byraudy.velocitysystem.listener;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.utils.Components;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class ProxyPingListener {

    @Subscribe
    public void handleProxyPing(ProxyPingEvent event) {
        ServerPing.Builder builder = event.getPing().asBuilder();

        builder.description(Components.ofChildren(Components.parse(VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("motd.line1")),
                Component.newline(),
                Components.parse(VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("motd.line2"))));

        if (VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getBoolean("maintenance"))
            builder.version(new ServerPing.Version(2, VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("maintenance_protocol")));
        if (VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getBoolean("ping_info")) {
            List<ServerPing.SamplePlayer> serverPings = new ArrayList<>();
            for (String string : VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("ping_info_message").split("\n")) {
                serverPings.add(new ServerPing.SamplePlayer(string, UUID.randomUUID()));
            }
            builder.samplePlayers(serverPings.toArray(new ServerPing.SamplePlayer[0]));
        }

        event.setPing(builder.build());
    }
}
