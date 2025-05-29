package org.example.commands;

import org.example.managers.DataBaseManager;
import org.example.network.LoginError;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'register' - регистрирует нового пользователя в системе.
 * Проверяет уникальность логина и сохраняет учетные данные в базе данных.
 * Наследует функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 */
public class Register extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 342L;

    /**
     * Менеджер для работы с базой данных пользователей
     */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды register.
     *
     * @param dataBaseManager менеджер для работы с базой данных пользователей
     */
    public Register(DataBaseManager dataBaseManager) {
        super("register", "Зарегистрировать пользователя");
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет регистрацию нового пользователя.
     * Проверяет наличие пользователя в базе данных и сохраняет новые учетные данные.
     *
     * @param request объект запроса, содержащий данные пользователя
     * @return объект Response с результатом выполнения операции:
     *         - сообщение об успешной регистрации
     *         - сообщение об ошибке, если логин уже занят
     */
    @Override
    public Response execute(Request request) {
        if (!dataBaseManager.existUser(request.getUser())) {
            dataBaseManager.addUser(request.getUser());
            return new Response("Регистрация успешна!");
        }
        return new Response(LoginError.LOGIN_ERROR, "Логин уже занят, введите новые данные");
    }
}