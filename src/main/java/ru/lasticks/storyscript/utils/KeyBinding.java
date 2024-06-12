package ru.lasticks.storyscript.utils;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_STORYSCRIPT = "key.category.storyscript.category";
    public static final String KEY_NEXT_MESSAGE = "key.storyscript.next_message";

    public static final KeyMapping NEXT_MESSAGE_KEY = new KeyMapping(KEY_NEXT_MESSAGE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_APOSTROPHE, KEY_CATEGORY_STORYSCRIPT);
}