package ru.lasticks.storyscript.commands.scriptCommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import ru.lasticks.storyscript.StoryScript;

public class SetColorCommand implements StoryScript.ScriptCommand {

    @Override
    public void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        if (args.length < 1) {
            context.getSource().sendFailure(Component.literal("Invalid color format."));
            return;
        }

        MessageCommand.msgColor = args[0];
    }
}
