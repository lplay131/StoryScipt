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
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ScriptReader {

    private static String msgColor = "#ffffff";
    public static volatile Boolean KEY_NEXT_MESSAGE_PRESSED = true;
    private static WorldVariables worldVariables;

    public static void readScript(String filePath, CommandContext<CommandSourceStack> context) {
        msgColor = "#ffffff";
        CommandSourceStack source = context.getSource();
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            source.sendFailure(Component.literal("Failed to get player: " + e.getMessage()));
            return;
        }

        if (worldVariables == null) {
            // Инициализация переменных для текущего мира
            File worldFolder = player.getLevel().getServer().getWorldPath(LevelResource.ROOT).toFile();
            worldVariables = new WorldVariables(new File(worldFolder, "world_variables.properties"));
        }

        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
                KEY_NEXT_MESSAGE_PRESSED = false;
                String line;
                int lineNumber = 0;
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (!line.trim().isEmpty()) {
                        if (line.startsWith("msg"))
                            msg(line, source, player.getName().getString());
                        else if (line.startsWith("setColor"))
                            setColor(line);
                        else if (line.startsWith("setVar"))
                            setVariable(line);
                        else if (line.startsWith("getVar"))
                            getVariable(line, source);
                        else if (line.startsWith("run"))
                            runCmd(line, context);
                        else if (line.startsWith("waitKey"))
                            waitKey(context);
                        else if (line.startsWith("sleep"))
                            sleepTime(line);
                        else if (line.startsWith("//")) {
                        } else {
                            MinecraftServer server = source.getServer();
                            for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                                serverPlayer.sendSystemMessage((Component.translatable("messages.unknown_command", lineNumber).setStyle(Style.EMPTY.withColor(TextColor.parseColor("red")))));
                            }
                        }

                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void msg(String command, CommandSourceStack source, String playerName) {
        MinecraftServer server = source.getServer();
        String personage = null;
        String message = null;
        if (command.contains("\"")) {
            int firstQuote = command.indexOf("\"");
            int secondQuote = command.indexOf("\"", firstQuote + 1);
            if (firstQuote != -1 && secondQuote != -1) {
                personage = command.substring(firstQuote + 1, secondQuote);
                message = command.substring(secondQuote + 1).trim();
            }
        } else {
            String[] parts = command.split(" ");
            if (parts.length >= 3) {
                personage = parts[1];
                message = parts[2];
            }
        }

        if (personage != null && message != null) {
            message = message.replace("$playername", playerName);
            message = replaceVariablesInMessage(message);

            Style style = Style.EMPTY.withColor(TextColor.parseColor(msgColor));
            for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
                serverPlayer.sendSystemMessage((Component.literal("[" + personage + "] ").setStyle(style)).append(Component.literal(message).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffffff")))));
            }
        }
    }

    private static String replaceVariablesInMessage(String message) {
        int startIndex = 0;
        while ((startIndex = message.indexOf("$$", startIndex)) != -1) {
            int endIndex = startIndex + 2;
            while (endIndex < message.length() && Character.isLetterOrDigit(message.charAt(endIndex))) {
                endIndex++;
            }
            String variableName = message.substring(startIndex + 2, endIndex);
            String variableValue = worldVariables.getVariable(variableName);
            message = message.substring(0, startIndex) + variableValue + message.substring(endIndex);
            startIndex += variableValue.length();
        }
        return message;
    }

    private static void setColor(String command) {
        String[] parts = command.split("\"");
        if (parts.length >= 2) {
            msgColor = parts[1];
        }
    }

    private static void setVariable(String command) {
        String[] parts = command.split(" ", 3);
        if (parts.length >= 3) {
            String key = parts[1];
            String value = parts[2];
            worldVariables.setVariable(key, value);
        }
    }

    private static void getVariable(String command, CommandSourceStack source) {
        String[] parts = command.split(" ", 2);
        if (parts.length >= 2) {
            String key = parts[1];
            String value = worldVariables.getVariable(key);
            source.sendSuccess(Component.literal("Variable " + key + " = " + value), false);
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
