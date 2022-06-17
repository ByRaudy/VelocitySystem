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

/*
Class created by ByRaudy
------------------------
Discord: ByRaudy#9708
Instagram: @byraudy
 */
public class UnbanCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("unban").requires(commandSource -> commandSource.hasPermission("velocitysystem.unban"))
                .executes(this::sendHelp)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string())
                        .suggests(this::setBannedPlayersSuggestions)
                        .executes(context -> unban((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("unban_help_message"));
        return 1;
    }

    private CompletableFuture<Suggestions> setBannedPlayersSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getPunishManager().getBannedPlayerData().forEach(suggestionsBuilder::suggest);
        return suggestionsBuilder.buildFuture();
    }

    private int unban(Player player, UUID target) {
        if (VelocitySystem.getVelocitySystem().getPunishManager().isBanned(target)) {
            VelocitySystem.getVelocitySystem().getPunishManager().unbanPlayer(target, player);
            return 1;
        }

        player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("unban_player_is_not_banned_message"));

        return 1;
    }
}
