package ru.lasticks.storyscript.variables;

import ru.lasticks.storyscript.StoryScript;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableReplacer {
    private final WorldVariables worldVariables;

    public VariableReplacer(WorldVariables worldVariables) {
        this.worldVariables = worldVariables;
    }

    public String replaceVariables(String input) {
        Pattern pattern = Pattern.compile("\\$\\$(\\w+)");
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue = worldVariables.getVariable(variableName);
            StoryScript.LOGGER.info("Replacing variable: {} with value: {}", variableName, variableValue);
            matcher.appendReplacement(result, Matcher.quoteReplacement(variableValue != null ? variableValue : ""));
        }

        matcher.appendTail(result);
        StoryScript.LOGGER.info("Final result after replacements: {}", result.toString());

        return result.toString();
    }





}