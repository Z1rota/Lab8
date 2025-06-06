package org.example.commands;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'register' - регистрирует нового пользователя в системе.
 * Обеспечивает создание новой учетной записи с указанием логина и пароля.
 * Наследует базовую функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 *
 * <p>В процессе выполнения команда запрашивает необходимые данные для регистрации.</p>
 * <p>Команда не требует аргументов в командной строке - все данные запрашиваются интерактивно.</p>
 */
public class Register extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между разными версиями класса.
     * Значение 342L представляет собой уникальный номер версии для этого класса.
     */
    @Serial
    private static final long serialVersionUID = 342L;

    /**
     * Конструктор команды register.
     * Инициализирует команду с параметрами:
     * - имя команды: "register"
     * - описание: "Зарегистрировать пользователя"
     * - флаг hasArgs: false (команда не требует аргументов в командной строке)
     */
    public Register() {
        super("register", "Зарегистрировать пользователя", false);
    }
}