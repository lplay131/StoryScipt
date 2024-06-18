package ru.lasticks.storyscript;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.lasticks.storyscript.commands.CommandSS;
import ru.lasticks.storyscript.commands.scriptCommands.*;
import ru.lasticks.storyscript.network.OpenGuiPacket;
import ru.lasticks.storyscript.utils.ScriptManager;
import ru.lasticks.storyscript.variables.VariableReplacer;
import ru.lasticks.storyscript.variables.WorldVariables;

import java.io.File;

@Mod(StoryScript.MODID)
public class StoryScript {
    public static final String MODID = "storyscript";
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel CHANNEL;
    public static final Logger LOGGER = LogManager.getLogger();


    public StoryScript() {
        createFolders();

        CHANNEL = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        ScriptManager.registerCommand("run", new Run());
        ScriptManager.registerCommand("setColor", new SetColor());
        ScriptManager.registerCommand("setVar", new SetVar());
        ScriptManager.registerCommand("sleep", new Sleep());

        CHANNEL.registerMessage(0, OpenGuiPacket.class, OpenGuiPacket::encode, OpenGuiPacket::decode, OpenGuiPacket::handle);
    }

    public interface ScriptCommand {
        void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args);
    }

    @Mod.EventBusSubscriber(modid = StoryScript.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommandRegistration {

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandSS.register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            // Регистрация команд скрипта
            MinecraftServer server = event.getServer();
            MsgCommandRegister(server);
        }
    }

    public static void MsgCommandRegister(MinecraftServer server) {
        File worldDir = server.getWorldPath(LevelResource.LEVEL_DATA_FILE).toFile().getParentFile();
        File varsFile = new File(worldDir, "vars.properties");

        WorldVariables worldVariables = new WorldVariables(varsFile);
        VariableReplacer variableReplacer = new VariableReplacer(worldVariables);
        Msg msg = new Msg(variableReplacer);

        ScriptManager.registerCommand("msg", msg);
    }

    private static void createFolders() {
        String GAME_ROOT = System.getProperty("user.dir");
        File sScriptsFolder = new File(GAME_ROOT + File.separator + "SScripts");
        File varsFolder = new File(sScriptsFolder, "vars");

        // Создание папки SScripts
        if (!sScriptsFolder.exists()) {
            boolean created = sScriptsFolder.mkdirs();
            if (created) {
                LOGGER.info("SScripts folder created successfully.");
            } else {
                LOGGER.error("Failed to create SScripts folder.");
            }
        } else {
            LOGGER.info("SScripts folder already exists.");
        }
    }
}
