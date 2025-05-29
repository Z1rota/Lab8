package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'add' - добавляет новый элемент в коллекцию.
 * Реализует интерфейс Serializable для поддержки сериализации/десериализации.
 * Наследует базовую функциональность от абстрактного класса Command.
 */
public class Add extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает совместимость между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 1337L;

    /**
     * Конструктор команды 'add'.
     * Инициализирует команду с:
     * - именем "add"
     * - описанием "add {element} : добавить новый элемент в коллекцию"
     * - флагом false, указывающим что команда не требует готового элемента
     *   (элемент будет создаваться в процессе выполнения)
     */
    public Add() {
        super("add", "add {element} : добавить новый элемент в коллекцию", false);
    }
}