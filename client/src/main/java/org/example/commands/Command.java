package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный базовый класс для всех команд.
 * Определяет общую структуру и поведение команд в системе.
 * Реализует интерфейс Serializable для поддержки сериализации команд.
 */
public abstract class Command implements Serializable {
    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает совместимость при сериализации/десериализации объектов команд.
     */
    @Serial
    private static final long serialVersionUID = 666L;

    /**
     * Название команды (используется для вызова команды)
     */
    private final String name;

    /**
     * Описание команды (используется в справке)
     */
    private final String description;

    /**
     * Флаг, указывающий требует ли команда дополнительных аргументов
     */
    private final boolean hasArgs;

    /**
     * Конструктор абстрактной команды.
     *
     * @param name название команды
     * @param description описание команды
     * @param hasArgs флаг наличия аргументов
     */
    public Command(String name, String description, boolean hasArgs) {
        this.name = name;
        this.description = description;
        this.hasArgs = hasArgs;
    }

    /**
     * Проверяет требует ли команда аргументов.
     *
     * @return true если команда требует аргументов, false в противном случае
     */
    public boolean isHasArgs() {
        return this.hasArgs;
    }

    /**
     * Возвращает описание команды.
     *
     * @return строку с описанием команды
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает название команды.
     *
     * @return строку с названием команды
     */
    public String getName() {
        return name;
    }

    /**
     * Сравнивает команды по их свойствам.
     *
     * @param o объект для сравнения
     * @return true если команды равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        if (hasArgs != command.hasArgs) return false;
        if (name != null ? !name.equals(command.name) : command.name != null) return false;
        return description != null ? description.equals(command.description) : command.description == null;
    }
}