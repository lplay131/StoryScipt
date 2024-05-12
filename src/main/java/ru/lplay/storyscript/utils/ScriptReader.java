package ru.lplay.storyscript.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ScriptReader {

    private static String msgColor = "#ffffff";
    public static Boolean KEY_NEXT_MESSAGE_PRESSED = true;

    public static void readScript(String filePath, CommandContext<CommandSourceStack> context) {
        msgColor = "#ffffff";
        CommandSourceStack source = context.getSource();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            KEY_NEXT_MESSAGE_PRESSED = false;
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    if (line.startsWith("msg"))
                        msg(line, source);
                    else if (line.startsWith("setColor"))
                        setColor(line);
                    else if (line.startsWith("run"))
                        runCmd(line, context);
                    else if (line.startsWith("waitKey"))
                        waitKey(context);
                    else if (line.startsWith("sleep"))
                        sleepTime(line);
                    else if (line.startsWith("//")) {
                    } else {
                        MinecraftServer server = source.getServer();
                        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                            player.sendSystemMessage((Component.translatable("messages.unknown_command", lineNumber).setStyle(Style.EMPTY.withColor(TextColor.parseColor("red")))));
                        }
                    }

                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void msg(String command, CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        String[] parts = command.split("\"");
        if (parts.length >= 3) {
            String personage = parts[1];
            String message = parts[2].substring(1).trim();
            Style style = Style.EMPTY.withColor(TextColor.parseColor(msgColor));
            //source.sendSystemMessage((Component.literal("[" + personage + "] ").setStyle(style)).append(Component.literal(message).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffffff")))));
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                player.sendSystemMessage((Component.literal("[" + personage + "] ").setStyle(style)).append(Component.literal(message).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffffff")))));
            }
        }
    }

    private static void setColor(String command) {
        String[] parts = command.split("\"");
        if (parts.length >= 2) {
            msgColor = parts[1];
        }
    }

    private static void waitKey(CommandContext<CommandSourceStack> context) {
        while (!KEY_NEXT_MESSAGE_PRESSED) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        KEY_NEXT_MESSAGE_PRESSED = false;
    }


    private static void sleepTime(String command) {
        try {
            int seconds = Integer.parseInt(command.split(" ")[1]);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void runCmd(String command, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getCommands().getDispatcher();
        try {
            dispatcher.execute(command.substring(4), context.getSource());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
