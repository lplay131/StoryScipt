package ru.lplay.storyscript.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ErrorMsg {
    public static void errorScript(String error, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = source.getServer();
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage((Component.literal("[StoryScript] Ошибка при выполнении скрипта: "+error).setStyle(Style.EMPTY.withColor(TextColor.parseColor("red")))));
        }
    }
}
