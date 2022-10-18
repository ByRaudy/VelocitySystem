package dev.jxnnik.velocitysystem.commands.admin;

import dev.jxnnik.velocitysystem.VelocitySystem;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EndCommand {

    public BrigadierCommand build() {
        LiteralCommandNode<CommandSource> node = LiteralArgumentBuilder.<CommandSource>literal("end").requires(commandSource -> commandSource.hasPermission("velocitysystem.end")).executes(this::sendHelpMessage)
                .then(LiteralArgumentBuilder.<CommandSource>literal("instant")
                        .executes(this::executeInstantShutdown))
                .then(LiteralArgumentBuilder.<CommandSource>literal("after")
                        .executes(this::sendHelpMessage)
                        .then(RequiredArgumentBuilder.<CommandSource, String>argument("secounds", StringArgumentType.string())
                                .suggests(this::suggestAfterSecounds)
                                .executes(context -> executeAfterShutdown(context, Integer.parseInt(context.getArgument("secounds", String.class))))))
                .build();

        return new BrigadierCommand(node);
    }

    private int sendHelpMessage(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("end_message_help"));
        return 1;
    }

    private CompletableFuture<Suggestions> suggestAfterSecounds(CommandContext<CommandSource> context, SuggestionsBuilder suggestionsBuilder) {
        suggestionsBuilder.suggest(5);
        suggestionsBuilder.suggest(10);
        suggestionsBuilder.suggest(20);
        suggestionsBuilder.suggest(30);
        return suggestionsBuilder.buildFuture();
    }

    private int executeInstantShutdown(CommandContext<CommandSource> context) {
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("message_end"));

        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> player.disconnect(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("message_end_screen")));
        VelocitySystem.getVelocitySystem().getProxyServer().shutdown();
        return 1;
    }

    private int executeAfterShutdown(CommandContext<CommandSource> context, int secounds) {
        if (secounds != 5 && secounds != 10 && secounds != 20 && secounds != 30) {
            context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("message_end_secounds"));
            return 0;
        }

        AtomicInteger taskSecounds = new AtomicInteger(secounds);
        VelocitySystem.getVelocitySystem().getProxyServer().getScheduler().buildTask(VelocitySystem.getVelocitySystem(), () -> {
            switch (taskSecounds.get()) {
                case 30, 25, 20, 15, 10, 5, 4, 3, 2, 1, 0 ->
                        VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> player.showTitle(Title.title(Component.text("§c§l" + taskSecounds.get()), VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessageWithoutPrefix("end_title", taskSecounds))));
                case -1 -> {
                    VelocitySystem.getVelocitySystem().getProxyServer().getAllPlayers().forEach(player -> player.disconnect(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("message_end_screen")));
                    VelocitySystem.getVelocitySystem().getProxyServer().shutdown();
                }
            }

            taskSecounds.getAndDecrement();
        }).repeat(1, TimeUnit.SECONDS).schedule();
        context.getSource().sendMessage(VelocitySystem.getVelocitySystem().getConfigManager().getMessages().getMessage("message_end"));

        return 1;
    }
}
