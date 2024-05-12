package ru.lplay.storyscript;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.lplay.storyscript.commands.CommandSS;

import java.io.File;

@Mod(StoryScript.MODID)
public class StoryScript {

    public static final String MODID = "storyscript";

    public StoryScript() {
        // Создание папки "SScripts" при инициализации мода
        createSScriptsFolder();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandSS.register(event.getDispatcher());
        }
    }

    private static void createSScriptsFolder() {
        File sScriptsFolder = new File(".", "SScripts");
        if (!sScriptsFolder.exists()) {
            sScriptsFolder.mkdirs();
        }
    }
}
