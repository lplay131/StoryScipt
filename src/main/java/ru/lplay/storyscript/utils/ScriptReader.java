package ru.lplay.storyscript.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ScriptReader {

    public static void readScript(String filePath, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                execute(line, context);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute(String command, CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSuccess(Component.nullToEmpty(command), true);
    }
}
