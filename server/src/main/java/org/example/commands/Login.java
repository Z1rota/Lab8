package org.example.commands;

import org.example.managers.DataBaseManager;
import org.example.network.LoginError;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для входа пользователя в систему.
 * Проверяет наличие пользователя в базе данных и возвращает соответствующий ответ.
 */
public class Login extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 341L;

    // Менеджер работы с базой данных, используется для проверки существования пользователя
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды login.
     *
     * @param dataBaseManager менеджер базы данных, через который производится проверка пользователя
     */
    public Login(DataBaseManager dataBaseManager) {
        super("login", "Войти в аккаунт");
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет проверку существования пользователя в базе данных.
     * Если пользователь найден — возвращается сообщение об успешной авторизации.
     * В противном случае — сообщение об ошибке авторизации.
     *
     * @param request объект запроса, содержащий пользователя для проверки
     * @return объект ответа с результатом проверки
     */
    @Override
    public Response execute(Request request) {
        if (dataBaseManager.existUser(request.getUser())) {
            return new Response("Авторизация успешна!");
        } else {
            return new Response(LoginError.LOGIN_ERROR, "Авторизация не прошла");
        }
    }
}
