package com.github.byraudy.velocitysystem.commands.team;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class TeamChatCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("teamchat").requires(commandSource -> commandSource.hasPermission("velocitysystem.teamchat"))
                .executes(this::sendTeamChatHelp)
                .then(LiteralArgumentBuilder.<CommandSource>literal("list")
                        .executes(this::sendTeamList))
                .then(LiteralArgumentBuilder.<CommandSource>literal("notify")
                        .executes(this::setTeamChatNotify))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendTeamChatHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_messages", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
        return 1;
    }

    private int sendTeamList(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(Component.text("§8§m                                                 "));
        context.getSource().sendMessage(Component.text("§2 "));
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_list"));
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            if (player.hasPermission("velocitysystem.team"))
                context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_list_player", player.getUsername(), player.getCurrentServer().get().getServerInfo().getName()));
        });
        context.getSource().sendMessage(Component.text("§2 "));
        context.getSource().sendMessage(Component.text("§8§m                                                 "));
        return 1;
    }

    private int setTeamChatNotify(CommandContext<CommandSource> context) {
        if (VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().contains((Player) context.getSource())) {
            VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().remove((Player) context.getSource());
            context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_notify_remove", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
        } else {
            VelocitySystem.getVelocitySystem().getTeamChatManager().getNotifiedPlayerList().add((Player) context.getSource());
            context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("teamchat_notify_add", VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getTeamchatPrefix()));
        }
        return 1;
    }
}