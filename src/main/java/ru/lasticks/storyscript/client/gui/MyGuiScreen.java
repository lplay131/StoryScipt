package ru.lasticks.storyscript.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import ru.lasticks.storyscript.StoryScript;

public class MyGuiScreen extends Screen {
    private final Screen parent;
    private final Minecraft minecraft;
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(StoryScript.MODID, "textures/gui/button.png");

    public MyGuiScreen(Screen parent, Minecraft minecraft) {
        super(Component.literal("My Dialog"));
        this.parent = parent;
        this.minecraft = minecraft;
    }

    @Override
    protected void init() {
        int buttonHeight = 20;
        int buttonSpacing = 5;
        int startX = 10;
        int startY = this.height - 4 * (buttonHeight + buttonSpacing);

        String[] buttonLabels = {"Кнопка 1", "Кнопка", "Другая кнопка", "Еще кнопка"};

        for (int i = 0; i < buttonLabels.length; i++) {
            int buttonY = startY + (buttonHeight + buttonSpacing) * i;
            String label = buttonLabels[i];
            int buttonWidth = getButtonWidth(label);
            this.addRenderableWidget(new LeftAlignedButton(startX, buttonY, buttonWidth, buttonHeight, Component.literal(label), button -> {
                minecraft.setScreen(parent);
            }));
        }
    }

    private int getButtonWidth(String label) {
        Font font = minecraft.font;
        int textWidth = font.width(label);
        int padding = 10;
        return textWidth + padding;
    }

    public class LeftAlignedButton extends Button {
        public LeftAlignedButton(int x, int y, int width, int height, Component title, Button.OnPress onPress) {
            super(x, y, width, height, title, onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            Font font = minecraft.font;
            int textColor = 16777120;
            int textX = this.getX() + 2;
            int textY = this.getY() + (this.height - 8) / 2;

            RenderSystem.setShaderTexture(0, BUTTON_TEXTURE);
            blit(poseStack, this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);

            boolean isHovered = mouseX >= this.getX() && mouseX < this.getX() + this.width &&
                    mouseY >= this.getY() && mouseY < this.getY() + this.height;

            if (isHovered) {
                Style style = Style.EMPTY.withUnderlined(true);
                MutableComponent formattedText = Component.literal(this.getMessage().getString()).setStyle(style);
                font.drawShadow(poseStack, formattedText, textX, textY, textColor);
            } else {
                font.drawShadow(poseStack, this.getMessage().getString(), textX, textY, textColor);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
