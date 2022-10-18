package dev.jxnnik.velocitysystem.commands;

import dev.jxnnik.velocitysystem.VelocitySystem;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
public class PingCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ping_message", ((Player) invocation.source()).getPing()));
    }
}
