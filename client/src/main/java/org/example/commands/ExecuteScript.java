package org.example.commands;

import org.example.managers.CommandManager;
import org.example.utility.FileMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * Команда для выполнения скрипта из указанного файла.
 * Читает команды из файла и последовательно исполняет их.
 * Наследует базовую функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 */
public class ExecuteScript extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта при необходимости.
     * Значение 1339L выбрано как уникальный номер версии для этого класса.
     */
    @Serial
    private static final long serialVersionUID = 1339L;

    /**
     * Конструктор команды execute_script.
     * Инициализирует команду с:
     * - именем "execute_script"
     * - подробным описанием функциональности
     * - флагом true, указывающим что команда требует дополнительный аргумент (имя файла)
     */
    public ExecuteScript() {
        super("execute_script",
                "execute_script file_name : считать и исполнить скрипт из указанного файла. " +
                        "В скрипте содержатся команды в таком же виде, в котором их вводит " +
                        "пользователь в интерактивном режиме.",
                true);
    }
}