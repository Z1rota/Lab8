package org.example.managers;



import org.example.commands.*;

import java.util.HashMap;

/**
 * Класс для управления командами.
 * Реализует регистрацию, хранение и выполнение команд.
 */
public class CommandManager {
    private HashMap<String, Command> commands = new HashMap<>();

    public void putCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

}