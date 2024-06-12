package ru.lasticks.storyscript.commands.scriptCommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import ru.lasticks.storyscript.StoryScript;
import ru.lasticks.storyscript.variables.WorldVariables;

import java.io.File;
import java.util.Objects;

public class SetVar implements StoryScript.ScriptCommand {
    private static WorldVariables worldVariables;

    @Override
    public void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        if (args.length < 2) {
            context.getSource().sendFailure(Component.literal("Usage: setvar <key> <value>"));
            return;
        }

        CommandSourceStack source = context.getSource();
        ServerPlayer player;
        try {
            player = source.getPlayerOrException();
        } catch (Exception e) {
            source.sendFailure(Component.literal("This command can only be executed by a player."));
            return;
        }

        if (worldVariables == null) {
            // Инициализация переменных для текущего мира
            File worldFolder = Objects.requireNonNull(player.getServer()).getWorldPath(LevelResource.ROOT).toFile();
            worldVariables = new WorldVariables(new File(worldFolder, "vars.properties"));
        }

        String key = args[0];
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            valueBuilder.append(args[i]);
            if (i < args.length - 1) {
                valueBuilder.append(" ");
            }
        }
        String value = valueBuilder.toString();
        worldVariables.setVariable(key, value);

        // Регистрация команды msg
        assert server != null;
        StoryScript.MsgCommandRegister(server);
    }
}
