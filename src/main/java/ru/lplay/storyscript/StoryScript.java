package ru.lplay.storyscript;

import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.RegisterCommandsEvent;
import ru.lplay.storyscript.commands.CommandSS;

@Mod(StoryScript.MODID)
public class StoryScript {

    public static final String MODID = "storyscript";

    public StoryScript() {
        // Инициализация мода
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            CommandSS.register(event.getDispatcher());
        }
    }
}