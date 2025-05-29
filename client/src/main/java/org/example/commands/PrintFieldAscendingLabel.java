package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'print_field_ascending_label' - выводит значения поля label всех элементов
 * коллекции в порядке возрастания.
 * Наследует функциональность от абстрактного класса Command и реализует интерфейс
 * Serializable для поддержки сериализации.
 *
 * <p>Команда сортирует и выводит только значения поля label, не изменяя порядок
 * самих элементов в коллекции.</p>
 */
public class PrintFieldAscendingLabel extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между различными версиями класса.
     * Значение 1344L представляет собой уникальный номер версии для данного класса.
     */
    @Serial
    private static final long serialVersionUID = 1344L;

    /**
     * Конструктор команды print_field_ascending_label.
     * Инициализирует команду с параметрами:
     * - имя команды: "print_field_ascending_label"
     * - описание: вывод значений поля label в порядке возрастания
     * - флаг hasArgs: false (команда не требует дополнительных аргументов)
     */
    public PrintFieldAscendingLabel() {
        super("print_field_ascending_label",
                "print_field_ascending_label : вывести значения поля label всех элементов в " +
                        "порядке возрастания",
                false);
    }
}