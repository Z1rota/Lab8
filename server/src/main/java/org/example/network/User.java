package org.example.network;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий пользователя системы.
 * Содержит логин и пароль пользователя.
 * Реализует интерфейс Serializable для поддержки сериализации.
 */
public class User implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 33L;

    /**
     * Логин пользователя.
     */
    private String login;

    /**
     * Пароль пользователя (в открытом виде, рекомендуется хеширование перед хранением).
     */
    private String password;

    /**
     * Конструктор пользователя.
     *
     * @param login    логин пользователя
     * @param password пароль пользователя
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Возвращает логин пользователя.
     *
     * @return логин пользователя
     */
    public String getLogin() {
        return login;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает новый логин пользователя.
     *
     * @param login новый логин
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Устанавливает новый пароль пользователя.
     *
     * @param password новый пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Сравнивает пользователей по логину и паролю.
     *
     * @param o объект для сравнения
     * @return true если пользователи одинаковые, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    /**
     * Возвращает хеш-код пользователя.
     *
     * @return хеш-код, вычисленный на основе логина и пароля
     */
    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}