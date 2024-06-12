package ru.lasticks.storyscript.commands.scriptCommands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import ru.lasticks.storyscript.StoryScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.lasticks.storyscript.variables.VariableReplacer;

public class MessageCommand implements StoryScript.ScriptCommand {
    public static String msgColor = "#ffffff";
    private static final Logger LOGGER = LogManager.getLogger();
    private final VariableReplacer variableReplacer;

    public MessageCommand(VariableReplacer variableReplacer) {
        this.variableReplacer = variableReplacer;
    }

    @Override
    public void execute(MinecraftServer server, CommandContext<CommandSourceStack> context, String[] args) {
        String command = String.join(" ", args);
        String personage = null;
        String message = null;

        if (command.contains("\"")) {
            int firstQuote = command.indexOf("\"");
            int secondQuote = command.indexOf("\"", firstQuote + 1);
            if (firstQuote != -1 && secondQuote != -1) {
                personage = command.substring(firstQuote + 1, secondQuote);
                message = command.substring(secondQuote + 1).trim();
            }
        } else {
            String[] parts = command.split(" ");
            if (parts.length >= 3) {
                personage = parts[1];
                message = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length));
            }
        }

        if (message == null) {
            context.getSource().sendFailure(Component.literal("Invalid message format."));
            return;
        }

        LOGGER.info("========= Original message: {}", message);
        message = variableReplacer.replaceVariables(message);
        LOGGER.info("========= Message after variable replacement: {}", message);

        Style personageStyle = Style.EMPTY.withColor(TextColor.parseColor(msgColor));
        Style messageStyle = Style.EMPTY.withColor(TextColor.parseColor("#ffffff"));

        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            MutableComponent personageComponent = Component.literal("[" + personage + "] ").setStyle(personageStyle);
            MutableComponent messageComponent = Component.literal(message).setStyle(messageStyle);
            serverPlayer.sendSystemMessage(personageComponent.append(messageComponent));
        }
    }


}
