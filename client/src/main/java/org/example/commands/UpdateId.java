package org.example.commands;

import org.example.builders.MusicBandsBuilder;
import org.example.exceptions.InvalidDataException;
import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'update' - обновляет элемент коллекции по указанному идентификатору.
 * Наследует функциональность от абстрактного класса Command и реализует
 * интерфейс Serializable для поддержки сериализации.
 *
 * <p>Требует обязательного аргумента - числового идентификатора элемента.</p>
 * <p>После указания ID запрашивает новые значения полей элемента через интерактивный ввод.</p>
 * <p>Если элемент с указанным ID не найден, выводит соответствующее сообщение.</p>
 */
public class UpdateId extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между разными версиями класса.
     * Значение 1350L представляет собой уникальный номер версии для данного класса.
     */
    @Serial
    private static final long serialVersionUID = 1350L;

    /**
     * Конструктор команды update.
     * Инициализирует команду с параметрами:
     * - имя команды: "update"
     * - описание: обновление элемента по идентификатору
     * - флаг hasArgs: true (команда требует обязательного аргумента - ID элемента)
     */
    public UpdateId() {
        super("update",
                "update id {element} : обновить значение элемента коллекции, " +
                        "id которого равен заданному",
                true);
    }
}