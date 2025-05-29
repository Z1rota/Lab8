package org.example.commands;

import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Абстрактный класс для реализации шаблона "Команда".
 * Все конкретные команды должны наследоваться от этого класса и реализовывать метод execute.
 */
public abstract class Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 666L;

    /** Название команды */
    private final String name;

    /** Описание команды (для help или документации) */
    private final String description;

    /**
     * Конструктор для инициализации команды.
     *
     * @param name название команды
     * @param description описание команды
     */
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Получить описание команды.
     *
     * @return строка с описанием
     */
    public String getDescription() {
        return description;
    }

    /**
     * Получить название команды.
     *
     * @return строка с названием
     */
    public String getName() {
        return name;
    }

    /**
     * Абстрактный метод для выполнения команды.
     * Каждая конкретная команда должна реализовать этот метод.
     *
     * @param request объект запроса, содержащий данные от клиента
     * @return объект ответа, содержащий результат выполнения
     */
    public abstract Response execute(Request request);
}
