package ru.lplay.storyscript.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ScriptReader {

    private static String  msgColor = "#ffffff";

    public static void readScript(String filePath, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        msgColor = "#ffffff";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) {
                    if (line.startsWith("msg"))
                        msg(line, context);
                    else if (line.startsWith("setColor"))
                        setColor(line, context);
                    else
                        source.sendFailure(Component.literal("Неизвестная команда на "+lineNumber+" строчке.").setStyle(Style.EMPTY.withColor(TextColor.parseColor("#d12a2a"))));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void msg(String command, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String[] parts = command.split("\"");
        if (parts.length >= 3) {
            String personage = parts[1];
            String message = parts[2].substring(1).trim();
            Style style = Style.EMPTY.withColor(TextColor.parseColor(msgColor));
            source.sendSuccess((Component.literal("["+personage+"] ").setStyle(style)).append(Component.literal(message).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffffff")))), true);
        }
    }

    private static void setColor(String command, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String[] parts = command.split("\"");
        if (parts.length >= 2) {
            msgColor = parts[1];
        }
    }
}
