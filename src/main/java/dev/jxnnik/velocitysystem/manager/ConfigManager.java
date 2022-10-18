package dev.jxnnik.velocitysystem.manager;

import dev.jxnnik.velocitysystem.manager.config.Config;
import dev.jxnnik.velocitysystem.manager.config.Messages;
import dev.jxnnik.velocitysystem.manager.config.MySQL;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Instagram: @byraudy
 */
public class ConfigManager {

    private final Config config;
    private final Messages messages;
    private final MySQL mySQL;

    public ConfigManager() {
        this.config = new Config();
        this.messages = new Messages();
        this.mySQL = new MySQL();
    }

    public Config getConfig() {
        return config;
    }

    public Messages getMessages() {
        return messages;
    }

    public MySQL getMySQL() {
        return mySQL;
    }
}