package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'print_descending' - выводит элементы коллекции в порядке убывания.
 * Отображает все элементы коллекции, отсортированные в обратном порядке.
 * Наследует базовую функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 *
 * <p>Сортировка выполняется согласно естественному порядку сравнения элементов коллекции.</p>
 */
public class PrintDescending extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между разными версиями класса.
     * Значение 1343L представляет собой уникальный номер версии для этого класса.
     */
    @Serial
    private static final long serialVersionUID = 1343L;

    /**
     * Конструктор команды print_descending.
     * Инициализирует команду с параметрами:
     * - имя команды: "print_descending"
     * - описание: вывод элементов коллекции в порядке убывания
     * - флаг hasArgs: false (команда не требует дополнительных аргументов)
     */
    public PrintDescending() {
        super("print_descending",
                "print_descending : вывести элементы коллекции в порядке убывания",
                false);
    }
}