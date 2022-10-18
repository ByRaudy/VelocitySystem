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
public class CheckCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("check").requires(commandSource -> commandSource.hasPermission(" "))
                .then(RequiredArgumentBuilder.<CommandSource, String>argument("playername", StringArgumentType.string()).suggests(this::setPlayersSuggestions).executes(context -> checkPlayer((Player) context.getSource(), UUIDFetcher.getUUID(context.getArgument("playername", String.class)))))
                .build();

        return new BrigadierCommand(node);
    }

    private CompletableFuture<Suggestions> setPlayersSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> {
            suggestionsBuilder.suggest(player.getUsername());
        });
        return suggestionsBuilder.buildFuture();
    }

    private int checkPlayer(Player player, UUID target) {
        player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("check_player_loading_message"));
        if (VelocitySystem.getVelocitySystem().getPunishManager().isBanned(target)) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("check_player_ban_yes_message", UUIDFetcher.getName(target), VelocitySystem.getVelocitySystem().getPunishManager().getBannersNameFromPlayersUuid(target), VelocitySystem.getVelocitySystem().getPunishManager().getBanReasonFromPlayersUuid(target), VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(VelocitySystem.getVelocitySystem().getPunishManager().getBanEnding(target))));
        } else {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("check_player_ban_no_message", UUIDFetcher.getName(target)));
        }

        if (VelocitySystem.getVelocitySystem().getPunishManager().isMuted(target)) {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("check_player_mute_yes_message", UUIDFetcher.getName(target), VelocitySystem.getVelocitySystem().getPunishManager().getMuterFromPlayersUuid(target), VelocitySystem.getVelocitySystem().getPunishManager().getMuteReasonFromPlayersUuid(target), VelocitySystem.getVelocitySystem().getPunishManager().getReamainingTime(VelocitySystem.getVelocitySystem().getPunishManager().getMuteEnding(target))));
        } else {
            player.sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("check_player_mute_no_message", UUIDFetcher.getName(target)));
        }
        return 1;
    }
}
