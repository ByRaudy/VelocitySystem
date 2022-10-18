package dev.jxnnik.velocitysystem;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jxnnik.velocitysystem.commands.HubCommand;
import dev.jxnnik.velocitysystem.commands.PingCommand;
import dev.jxnnik.velocitysystem.commands.admin.EndCommand;
import dev.jxnnik.velocitysystem.commands.punishment.*;
import dev.jxnnik.velocitysystem.commands.team.TeamChatCommand;
import dev.jxnnik.velocitysystem.listener.LoginListener;
import dev.jxnnik.velocitysystem.listener.PlayerChatListener;
import dev.jxnnik.velocitysystem.listener.ProxyPingListener;
import dev.jxnnik.velocitysystem.manager.*;
import lombok.Getter;

import java.sql.SQLException;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
@Plugin(
        id = "velocitysystem",
        name = "VelocitySystem",
        version = "1.0",
        description = "ProxySystem for Velocity",
        authors = {"ByRaudy"}
)
@Getter
public class VelocitySystem {

    private static VelocitySystem velocitySystem;
    private final ProxyServer proxyServer;
    private final ConfigManager configManager;
    private MySQLManager mySQLManager;
    private final PunishManager punishManager;
    private final TeamChatManager teamChatManager;

    @Inject
    public VelocitySystem(ProxyServer proxyServer) {
        velocitySystem = this;
        this.proxyServer = proxyServer;
        System.out.println("VELOCITYSYSTEM: Trying to init ConfigManager...");
        this.configManager = new ConfigManager();
        System.out.println("VELOCITYSYSTEM: Successfull");
        System.out.println("VELOCITYSYSTEM: Trying to init MySQLManager...");
        try {
            this.mySQLManager = new MySQLManager(configManager.getMySQL());
            System.out.println("VELOCITYSYSTEM: Successfull");
        } catch (SQLException exception) {
            System.out.println("VELOCITYSYSTEM: Error while init MySQLManager.java: Cannot connect to database!");
        }
        System.out.println("VELOCITYSYSTEM: Trying to init PunishManager...");
        this.punishManager = new PunishManager();
        System.out.println("VELOCITYSYSTEM: Successfull");
        System.out.println("VELOCITYSYSTEM: Trying to init TeamChatManager...");
        this.teamChatManager = new TeamChatManager();
        System.out.println("VELOCITYSYSTEM: Successfull");
        System.out.println("VELOCITYSYSTEM: VelocitySystem is successfully started.");
    }

    @Subscribe
    public void handleProxyInitialization(ProxyInitializeEvent event) {
        EventManager eventManager = proxyServer.getEventManager();
        CommandManager commandManager = proxyServer.getCommandManager();

        eventManager.register(this, new ProxyPingListener(this));
        eventManager.register(this, new LoginListener());
        eventManager.register(this, new PlayerChatListener());
        eventManager.register(this, new TablistManager(this));

        if (configManager.getConfig().getBoolean("hub_command_enabled"))
            commandManager.register(commandManager.metaBuilder("hub").aliases("l", "lobby").build(), new HubCommand());
        commandManager.register(commandManager.metaBuilder("ping").build(), new PingCommand());
        commandManager.register(new EndCommand().build());
        commandManager.register(new TeamChatCommand().build());
        commandManager.register(new BanCommand().build());
        commandManager.register(new MuteCommand().build());
        commandManager.register(new KickCommand().build());
        commandManager.register(new CheckCommand().build());
        commandManager.register(new UnbanCommand().build());
        commandManager.register(new UnmuteCommand().build());
    }

    @Subscribe
    public void handleProxyShutdown(ProxyShutdownEvent event) {
        this.mySQLManager.disconnectFromDatabase();
    }

    public static VelocitySystem getVelocitySystem() {
        return velocitySystem;
    }

    public ProxyServer getProxyServer() {
        return proxyServer;
    }
}