package ru.lplay.storyscript.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import ru.lplay.storyscript.utils.ErrorMsg;

import java.io.File;

import static ru.lplay.storyscript.utils.ScriptReader.readScript;

public class CommandSS {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ss")
                .then(Commands.argument("script", StringArgumentType.greedyString())
                        .executes(context -> execute(context))));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        String scriptPath = context.getArgument("script", String.class);

        // Путь до скрипта
        String fullPath = "SScripts/" + scriptPath + ".sscript";
        File scriptFile = new File(source.getServer().getServerDirectory(), fullPath);

        // Проверка существует ли скрипт
        if (!scriptFile.exists()) {
            ErrorMsg.errorScript("Файл " + fullPath + " не найден!", context);
            return 0; // Возвращаем 0, чтобы команда завершилась с ошибкой
        }

        // Чтение скрипта
        readScript(scriptFile.getPath(), context);

        return 1;
    }
}
