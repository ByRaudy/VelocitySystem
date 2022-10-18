package dev.jxnnik.velocitysystem.listener;

import dev.jxnnik.velocitysystem.VelocitySystem;
import dev.jxnnik.velocitysystem.manager.config.Config;
import dev.jxnnik.velocitysystem.utils.Components;
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
Discord: Jannik#9708
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
public class ProxyPingListener {

    private Config config;
    
    public ProxyPingListener(VelocitySystem velocitySystem) {
        this.config = velocitySystem.getConfigManager().getConfig();
    }
    
    @Subscribe
    public void handleProxyPing(ProxyPingEvent event) {
        if(config.getBoolean("motd")) {
            ServerPing.Builder builder = event.getPing().asBuilder();

            builder.description(Components.ofChildren(Components.parse(config.getString("motd.line1")),
                    Component.newline(),
                    Components.parse(config.getString("motd.line2"))));

            if (config.getBoolean("maintenance"))
                builder.version(new ServerPing.Version(2, config.getString("maintenance_protocol")));
            if (config.getBoolean("ping_info")) {
                List<ServerPing.SamplePlayer> serverPings = new ArrayList<>();
                for (String string : config.getString("ping_info_message").split("\n")) {
                    serverPings.add(new ServerPing.SamplePlayer(string, UUID.randomUUID()));
                }
                builder.samplePlayers(serverPings.toArray(new ServerPing.SamplePlayer[0]));
            }

            event.setPing(builder.build());
        }
    }
}
