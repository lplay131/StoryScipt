package ru.lasticks.storyscript.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import ru.lasticks.storyscript.StoryScript;
import ru.lasticks.storyscript.utils.KeyBinding;
import ru.lasticks.storyscript.utils.ScriptReader;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = StoryScript.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.NEXT_MESSAGE_KEY.consumeClick()) {
                ScriptReader.KEY_NEXT_MESSAGE_PRESSED = true;
            }
        }
    }

    @Mod.EventBusSubscriber(modid = StoryScript.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.NEXT_MESSAGE_KEY);
        }
    }
}
