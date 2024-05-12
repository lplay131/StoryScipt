package ru.lplay.storyscript.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;

import static ru.lplay.storyscript.utils.ScriptReader.readScript;

public class CommandSS {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ss")
                .then(Commands.argument("script", StringArgumentType.greedyString())
                        .executes(context -> execute(context))));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        String scriptPath = context.getArgument("script", String.class);

        String fullPath = "SScripts/" + scriptPath + ".sscript";
        File scriptFile = new File(source.getServer().getServerDirectory(), fullPath);

        if (!scriptFile.exists()) {
            MinecraftServer server = source.getServer();
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.sendSystemMessage((Component.translatable("message.error.file_not_found", fullPath).setStyle(Style.EMPTY.withColor(TextColor.parseColor("red")))));
            }
            return 0;
        }

        readScript(scriptFile.getPath(), context);

        return 1;
    }
}
