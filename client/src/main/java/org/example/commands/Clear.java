package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'clear' - полностью очищает коллекцию элементов.
 * Удаляет все элементы, хранящиеся в коллекции.
 * Наследует базовую функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 */
public class Clear extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта при необходимости.
     * Значение 1338L выбрано как уникальный номер версии для этого класса.
     */
    @Serial
    private static final long serialVersionUID = 1338L;

    /**
     * Конструктор команды clear.
     * Инициализирует команду с:
     * - именем "clear"
     * - описанием "clear : очистить коллекцию"
     * - флагом false, указывающим что команда не требует дополнительных данных
     *   для выполнения
     */
    public Clear() {
        super("clear", "clear : очистить коллекцию", false);
    }
}