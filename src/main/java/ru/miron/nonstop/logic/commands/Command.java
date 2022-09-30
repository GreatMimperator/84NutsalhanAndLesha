package ru.miron.nonstop.logic.commands;

import ru.miron.nonstop.logic.util.json.JSONConvertable;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;
import ru.miron.nonstop.logic.util.json.types.values.JSONString;

public class Command implements JSONConvertable {
    private CommandName commandName;
    private JSONConvertable commandArgs;
    private EnterEntry enterEntry;
    
    /**
     * @throws IllegalArgumentException if commandArgs null state doesn't correspond to commandName or enterEntry or commandName is null 
     */
    public Command(CommandName commandName, EnterEntry enterEntry, JSONConvertable commandArgs) throws IllegalArgumentException {
        setCommandName(commandName);
        setEnterEntry(enterEntry);
        setCommandArgs(commandArgs);
    }

    public CommandName getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandName commandName) {
        if (commandName == null)
            throw new IllegalArgumentException();
        this.commandName = commandName;
    }

    public JSONConvertable getCommandArgs() {
        return commandArgs;
    }

    /**
     * @throws IllegalArgumentException if commandArgs is not null or null when command is argumentless or not
     */
    public void setCommandArgs(JSONConvertable commandArgs) throws IllegalArgumentException {
        if (commandName.isArgumentless()) {
            if (commandArgs != null) {
                throw new IllegalArgumentException();
            }
        } else {
            if (commandArgs == null) {
                throw new IllegalArgumentException();
            }
        }
        this.commandArgs = commandArgs;
    }

    public EnterEntry getEnterEntry() {
        return enterEntry;
    }

    /**
     * @throws IllegalArgumentException if enterEntry is null
     */
    public void setEnterEntry(EnterEntry enterEntry) throws IllegalArgumentException {
        if (enterEntry == null) {
            throw new IllegalArgumentException();
        }
        this.enterEntry = enterEntry;
    }

    @Override
    public JSONMap toJSON() {
        JSONMap root = new JSONMap();
        if (enterEntry != null) {
            root.put("user", enterEntry.toJSON());
        }
        root.put("command name", new JSONString(commandName.name));
        if (commandArgs != null) {
            root.put("command arguments", commandArgs.toJSON());
        }
        return root;
    }
}
