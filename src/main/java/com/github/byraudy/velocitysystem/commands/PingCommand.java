package com.github.byraudy.velocitysystem.commands;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class PingCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ping_message", ((Player) invocation.source()).getPing()));
    }
}
