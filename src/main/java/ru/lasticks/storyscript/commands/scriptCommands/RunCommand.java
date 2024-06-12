package ru.lasticks.storyscript.commands.scriptCommands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import ru.lasticks.storyscript.StoryScript;

public class RunCommand implements StoryScript.ScriptCommand {
    @Override
    public void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        if (args.length < 1) {
            context.getSource().sendFailure(Component.literal("Недостаточно аргументов для команды run"));
            return;
        }

        CommandSourceStack source = context.getSource();
        CommandDispatcher<CommandSourceStack> dispatcher = source.getServer().getCommands().getDispatcher();
        String command = String.join(" ", args);

        try {
            dispatcher.execute(command, source);
        } catch (CommandSyntaxException e) {
            context.getSource().sendFailure(Component.literal("Error with command executing: " + e.getMessage()));
        }
    }
}