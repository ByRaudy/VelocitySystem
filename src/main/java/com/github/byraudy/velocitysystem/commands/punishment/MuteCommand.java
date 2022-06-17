package com.github.byraudy.velocitysystem.commands.punishment;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.github.byraudy.velocitysystem.utils.uuid.UUIDFetcher;
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
import java.util.function.Supplier;

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class MuteCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("mute").requires(commandSource -> commandSource.hasPermission("velocitysystem.ban")).executes(this::sendHelp)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string()).suggests(this::setPlayersSuggestions).executes(this::sendHelp)
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Werbung").executes(context -> mutePlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Werbung", 1210000)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Beleidigungen").executes(context -> mutePlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Beleidigungen", 1210000)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Verhalten").executes(context -> mutePlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Verhalten", 604800)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Spamming").executes(context -> mutePlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Spamming", 604800)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Server-Beleidigungen").executes(context -> mutePlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Server Beleidigungen", 2419000))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_help_message"));
        return 1;
    }

    private CompletableFuture<Suggestions> setPlayersSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            suggestionsBuilder.suggest(player.getUsername());
        });
        return suggestionsBuilder.buildFuture();
    }

    private static int mutePlayer(Player player, UUID target, String reason, int duration) {
        if (target == player.getUniqueId()) return 1;

        if (VelocitySystem.getVelocitySystem().getPunishManager().isMuted(target)) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_is_already_muted_message"));
            return 1;
        }

        Player targetPlayer = VelocitySystem.getVelocitySystem().getProxyServer().getPlayer(target).orElseGet(new Supplier<Player>() {
            @Override
            public Player get() {
                return null;
            }
        });

        if (targetPlayer != null) {
            if (targetPlayer.hasPermission("velocitysystem.cantbemuted")) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_cant_player_message"));
                return 1;
            }

            targetPlayer.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("mute_player_message", reason, VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(duration)));
        }

        VelocitySystem.getVelocitySystem().getPunishManager().mutePlayer(target, UUIDFetcher.getName(target), reason, duration, player);
        return 1;
    }
}
