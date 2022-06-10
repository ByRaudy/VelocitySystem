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
public class BanCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("ban").requires(commandSource -> commandSource.hasPermission("velocitysystem.ban")).executes(BanCommand::sendHelp)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string()).suggests(this::setPlayersSuggestions).executes(BanCommand::sendHelp)
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Hacking").executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Hacking", 604800))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Drohungen").executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Drohungen", 2419000))))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Cape/Skin/Name").executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Cape/Skin/Name", 2419000)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Teaming").executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Teaming", 604800)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Hacking-Bestätigung").requires(commandSource -> commandSource.hasPermission("velocity.ban.higher")).executes(context -> overriteBan((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Hacking", -1)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Todeswunsch").requires(commandSource -> commandSource.hasPermission("velocity.ban.higher")).executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Todeswunsch", -1)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Bannumgehung").requires(commandSource -> commandSource.hasPermission("velocity.ban.higher")).executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Bannumgehung", -1)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Diskriminierung").requires(commandSource -> commandSource.hasPermission("velocity.ban.higher")).executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Diskriminierung", -1)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Sicherheitsban").requires(commandSource -> commandSource.hasPermission("velocitysystem.ban.reason.security")).executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Sicherheitsban", -1)))
                        .then(LiteralArgumentBuilder.<CommandSource>literal("Hausverbot").requires(commandSource -> commandSource.hasPermission("velocitysystem.ban.reason.house_ban")).executes(context -> banPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)), "Hausverbot", -1)))
                ).build();

        return new BrigadierCommand(node);
    }

    private CompletableFuture<Suggestions> setPlayersSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            suggestionsBuilder.suggest(player.getUsername());
        });
        return suggestionsBuilder.buildFuture();
    }

    private static int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ban_help_message"));
        return 1;
    }

    private static int banPlayer(Player player, UUID target, String reason, int duration) {
        if (target == player.getUniqueId()) return 1;

        if (VelocitySystem.getVelocitySystem().getPunishManager().isBanned(target)) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ban_is_already_banned_message"));
            return 1;
        }

        Player targetPlayer = VelocitySystem.getVelocitySystem().getProxyServer().getPlayer(target).orElseGet(new Supplier<Player>() {
            @Override
            public Player get() {
                return null;
            }
        });

        if (targetPlayer != null) {
            if (targetPlayer.hasPermission("velocitysystem.cantbebanned")) {
                player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("ban_cant_player_message"));
                return 1;
            }

            player.disconnect(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("ban_screen_message", VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getString("server_name"), VelocitySystem.getVelocitySystem().getPunishManager().getBanReasonFromPlayersUuid(targetPlayer.getUniqueId()), VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(duration)));
        }

        VelocitySystem.getVelocitySystem().getPunishManager().banPlayer(target, UUIDFetcher.getName(target), reason, duration, player);
        return 1;
    }

    private static int overriteBan(Player player, UUID target, String reason, int duration) {
        VelocitySystem.getVelocitySystem().getPunishManager().unbanPlayer(target, player);
        banPlayer(player, target, reason, duration);

        return 1;
    }
}
