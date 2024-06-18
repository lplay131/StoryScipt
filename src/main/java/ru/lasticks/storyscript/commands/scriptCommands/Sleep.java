package ru.lasticks.storyscript.commands.scriptCommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import ru.lasticks.storyscript.StoryScript;

public class Sleep implements StoryScript.ScriptCommand {
    @Override
    public void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        try {
            int seconds = Integer.parseInt(args[0]);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException | NumberFormatException e) {
            StoryScript.LOGGER.error("Error with sleep command executing: ", e);
        }
    }
}
