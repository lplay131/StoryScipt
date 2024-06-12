package ru.lasticks.storyscript.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import ru.lasticks.storyscript.client.gui.MyGuiScreen;

import java.util.function.Supplier;

public class OpenGuiPacket {

    public OpenGuiPacket() {
        // Конструктор без параметров
    }

    public static void encode(OpenGuiPacket packet, FriendlyByteBuf buffer) {
        // Кодирование пакета (если есть данные, которые нужно передать)
    }

    public static OpenGuiPacket decode(FriendlyByteBuf buffer) {
        return new OpenGuiPacket();
    }

    public static void handle(OpenGuiPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                openGuiOnClient();
            }
        });
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void openGuiOnClient() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.setScreen(new MyGuiScreen(null, minecraft));
    }
}
