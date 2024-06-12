package ru.lasticks.storyscript.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.lasticks.storyscript.utils.ScriptFileReader;
import ru.lasticks.storyscript.utils.ScriptManager;
import ru.lasticks.storyscript.variables.WorldVariables;
import ru.lasticks.storyscript.StoryScript;

import java.io.File;
import java.util.List;

public class CommandSS {
    private static WorldVariables worldVariables;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ss")
                .then(Commands.literal("run")
                        .then(Commands.argument("script", StringArgumentType.greedyString())
                                .executes(CommandSS::executeScript)))
                .then(Commands.literal("setVar")
                        .then(Commands.argument("key", StringArgumentType.string())
                                .then(Commands.argument("value", StringArgumentType.greedyString())
                                        .executes(CommandSS::setVariable)))));
    }

    public static int executeScript(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        String scriptPath = context.getArgument("script", String.class);
        LOGGER.info("Executing script: {}", scriptPath);

        String fullPath = "SScripts/" + scriptPath + ".sscript";
        File scriptFile = new File(source.getServer().getServerDirectory(), fullPath);

        if (!scriptFile.exists()) {
            LOGGER.error("File not found: {}", fullPath);
            source.sendFailure(Component.literal("File not found: " + fullPath));
            return 0;
        }

        if (server == null) {
            LOGGER.error("Failed to get Minecraft server instance.");
            return 0;
        }

        List<String> scripts = ScriptFileReader.readScriptsFromFile(scriptFile.getPath());
        for (String script : scripts) {
            String[] parts = script.split(" ");
            String commandName = parts[0];
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);

            try {
                ScriptManager.executeCommand(commandName, server, context, args);
            } catch (Exception e) {
                LOGGER.error("Error with command executing: {}", script, e);
                source.sendFailure(Component.literal("Error with command executing: " + script));
                return 0;
            }
        }
        return 1;
    }

    public static int setVariable(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        var server = player.getServer();

        if (worldVariables == null) {
            // Инициализация переменных для текущего мира
            assert server != null;
            File worldFolder = server.getWorldPath(LevelResource.ROOT).toFile();
            worldVariables = new WorldVariables(new File(worldFolder, "vars.properties"));
        }

        String key = context.getArgument("key", String.class);
        String value = context.getArgument("value", String.class);
        worldVariables.setVariable(key, value);

        source.sendSuccess(Component.literal("Variable " + key + " set to " + value), true);

        // Регистрация команды msg
        assert server != null;
        StoryScript.MsgCommandRegister(server);

        return 1;
    }
}
