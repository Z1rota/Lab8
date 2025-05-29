package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для группировки элементов коллекции по значению поля label
 * и вывода количества элементов в каждой группе.
 * Наследует функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 */
public class GroupCountingByLabel extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта при необходимости.
     * Значение 1340L выбрано как уникальный номер версии для этого класса.
     */
    @Serial
    private static final long serialVersionUID = 1340L;

    /**
     * Конструктор команды group_counting_by_label.
     * Инициализирует команду с:
     * - именем "group_counting_by_label"
     * - описанием функциональности группировки по полю label
     * - флагом false, указывающим что команда не требует дополнительных аргументов
     */
    public GroupCountingByLabel() {
        super("group_counting_by_label",
                "group_counting_by_label : сгруппировать элементы коллекции " +
                        "по значению поля label, вывести количество элементов в каждой группе",
                false);
    }
}