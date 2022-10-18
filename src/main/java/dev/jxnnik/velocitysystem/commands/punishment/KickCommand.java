package dev.jxnnik.velocitysystem.commands.punishment;

import dev.jxnnik.velocitysystem.VelocitySystem;
import dev.jxnnik.velocitysystem.utils.uuid.UUIDFetcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/*
Class created by ByRaudy
------------------------
Discord: Jannik#9708
Instagram: @byraudy
 */
public class KickCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("kick")
                .requires(commandSource -> commandSource.hasPermission("velocitysystem.kick")).executes(this::sendHelp)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string()).suggests(this::suggestPlayers).executes(this::sendHelp)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("kick_reason", StringArgumentType.greedyString()).executes(context -> kickPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), context.getArgument("kick_reason", String.class)))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("kick_help_message"));
        return 1;
    }

    private int kickPlayer(Player player, UUID target, String reason) {
        if (VelocitySystem.getVelocitySystem().getProxyServer().getPlayer(target).get() == null) return 1;

        Player targetPlayer = VelocitySystem.getVelocitySystem().getProxyServer().getPlayer(target).get();

        if (targetPlayer.hasPermission("velocity.cantbekicked")) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("kick_cant_player_message"));
            return 1;
        }

        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(players -> {
            if (players.hasPermission("velocitysystem.kick.see")) {
                players.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("kick_player_message", targetPlayer.getUsername(), player.getUsername(), reason));
            }
        });

        targetPlayer.disconnect(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("kick_screen", VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("server_name"), reason));
        return 1;
    }

    private CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            suggestionsBuilder.suggest(player.getUsername());
        });
        return suggestionsBuilder.buildFuture();
    }
}
