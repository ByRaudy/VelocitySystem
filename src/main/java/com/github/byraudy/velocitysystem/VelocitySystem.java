package com.github.byraudy.velocitysystem;

import com.github.byraudy.velocitysystem.commands.HubCommand;
import com.github.byraudy.velocitysystem.commands.PingCommand;
import com.github.byraudy.velocitysystem.commands.admin.EndCommand;
import com.github.byraudy.velocitysystem.commands.punishment.*;
import com.github.byraudy.velocitysystem.listener.LoginListener;
import com.github.byraudy.velocitysystem.listener.PlayerChatListener;
import com.github.byraudy.velocitysystem.listener.ProxyPingListener;
import com.github.byraudy.velocitysystem.manager.ConfigManager;
import com.github.byraudy.velocitysystem.manager.MySQLManager;
import com.github.byraudy.velocitysystem.manager.PunishManager;
import com.github.byraudy.velocitysystem.manager.TablistManager;
import com.github.byraudy.velocitysystem.velocitysystem.BuildConstants;
import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import java.sql.SQLException;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
@Plugin(
        id = "velocitysystem",
        name = "VelocitySystem",
        version = BuildConstants.VERSION,
        description = "ProxySystem for Velocity",
        authors = {"ByRaudy"}
)
public class VelocitySystem {

    private static VelocitySystem velocitySystem;
    private final ProxyServer proxyServer;
    private final ConfigManager configManager;
    private MySQLManager mySQLManager;
    private final PunishManager punishManager;

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
    }

    @Subscribe
    public void handleProxyInitialization(ProxyInitializeEvent event) {
        EventManager eventManager = proxyServer.getEventManager();
        CommandManager commandManager = proxyServer.getCommandManager();

        eventManager.register(this, new ProxyPingListener());
        eventManager.register(this, new LoginListener());
        eventManager.register(this, new PlayerChatListener());
        eventManager.register(this, new TablistManager(this));

        commandManager.register(commandManager.metaBuilder("hub").aliases("l", "lobby").build(), new HubCommand());
        commandManager.register(commandManager.metaBuilder("ping").build(), new PingCommand());
        commandManager.register(new EndCommand().build());
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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MySQLManager getMySQLManager() {
        return mySQLManager;
    }

    public PunishManager getPunishManager() {
        return punishManager;
    }
}