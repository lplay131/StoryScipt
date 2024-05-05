package ru.lplay.storyscript.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.*;

public class ScriptReader {

    public static void readScript(String filePath, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                msg(line, context);
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
            source.sendSuccess(Component.nullToEmpty("["+personage+"] "+message), true);
        } else {
            source.sendSuccess(Component.nullToEmpty("Неизвесная команда!"), true);
        }
    }
}
