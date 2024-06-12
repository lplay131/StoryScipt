package ru.lasticks.storyscript.utils;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import ru.lasticks.storyscript.StoryScript;

import java.util.HashMap;
import java.util.Map;

public class ScriptManager {
    private static final Map<String, StoryScript.ScriptCommand> commands = new HashMap<>();

    public static void registerCommand(String name, StoryScript.ScriptCommand command) {
        commands.put(name, command);
    }

    public static void executeCommand(String name, MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        StoryScript.ScriptCommand command = commands.get(name);
        if (command != null) {
            command.execute(server, context, args);
        } else {
            context.getSource().sendFailure(Component.literal("Unknown command: " + name));
        }
    }
}