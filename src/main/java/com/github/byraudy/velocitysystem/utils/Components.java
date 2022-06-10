package com.github.byraudy.velocitysystem.utils;

import com.github.byraudy.velocitysystem.VelocitySystem;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Components {

    public static TextComponent ofChildren(final ComponentLike... children) {
        if (children.length == 0) {
            return Component.empty();
        }

        return Component.text().append(children).build();
    }

    public static Component parse(final String input, final ServerPing.Players count) {
        return MiniMessage.miniMessage().deserialize(
                replacePlayerCount(input, Integer.toString(count.getOnline()), Integer.toString(count.getMax()))
        );
    }

    public static Component parse(final String input, final int onlinePlayers, final int maxPlayers) {
        return MiniMessage.miniMessage().deserialize(
                replacePlayerCount(input, Integer.toString(onlinePlayers), Integer.toString(maxPlayers))
        );
    }

    public static Component parse(final String input) {
        if (VelocitySystem.getVelocitySystem().getConfigManager().getConfig().getBoolean("gradients"))
            return MiniMessage.miniMessage().deserialize(input);

        return Component.text(input);
    }

    private static String replacePlayerCount(final String input, final String online, final String max) {
        return input.replace("{onlinePlayers}", online).replace("{maxPlayers}", max);
    }
}
