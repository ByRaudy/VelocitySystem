package com.github.byraudy.velocitysystem.manager;

import com.github.byraudy.velocitysystem.manager.config.Config;
import com.github.byraudy.velocitysystem.manager.config.Messages;
import com.github.byraudy.velocitysystem.manager.config.MySQL;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
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