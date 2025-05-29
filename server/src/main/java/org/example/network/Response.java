package org.example.network;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс для передачи ответов от сервера клиенту.
 * Содержит результат выполнения команды или информацию об ошибке аутентификации.
 * Реализует интерфейс Serializable для поддержки сериализации при передаче по сети.
 */
public class Response implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 20L;

    /**
     * Результат выполнения команды или сообщение для клиента.
     * По умолчанию установлено значение "Успешно".
     */
    private String result = "Успешно";

    /**
     * Ошибка аутентификации, если таковая имеется.
     */
    private LoginError loginError;

    /**
     * Конструктор ответа с текстовым сообщением.
     * @param string текстовое сообщение для клиента
     */
    public Response(String string) {
        this.result = string;
    }

    /**
     * Устанавливает результат выполнения команды.
     * @param result текстовое сообщение результата
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Возвращает результат выполнения команды.
     * @return текстовое сообщение результата
     */
    public String getResult() {
        return result;
    }

    /**
     * Конструктор ответа с ошибкой аутентификации.
     * @param loginError тип ошибки аутентификации
     * @param result дополнительное текстовое сообщение
     */
    public Response(LoginError loginError, String result) {
        this.result = result;
        this.loginError = loginError;
    }

    /**
     * Возвращает ошибку аутентификации.
     * @return объект LoginError или null, если ошибки нет
     */
    public LoginError getLoginError() {
        return loginError;
    }

    /**
     * Конструктор по умолчанию.
     * Создает ответ с результатом "Успешно".
     */
    public Response() {}
}