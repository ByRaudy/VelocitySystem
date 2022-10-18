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
Twitter: @ByRaudy
Instagram: @jxnnik.official
 */
public class UnmuteCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("unmute").requires(commandSource -> commandSource.hasPermission("velocitysystem.unmute")).executes(this::sendHelp)
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string())
                        .suggests(this::setBannedPlayersSuggestions)
                        .executes(context -> unmute((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelp(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("unmute_help_message"));
        return 1;
    }

    private CompletableFuture<Suggestions> setBannedPlayersSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getPunishManager().getmutedPlayerData().forEach(suggestionsBuilder::suggest);
        return suggestionsBuilder.buildFuture();
    }

    private int unmute(Player player, UUID target) {
        if (VelocitySystem.getVelocitySystem().getPunishManager().isMuted(target)) {
            VelocitySystem.getVelocitySystem().getPunishManager().unmutePlayer(target, player);
            return 1;
        }

        return 1;
    }
}
