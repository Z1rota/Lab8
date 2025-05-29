package org.example.builders;

import org.example.network.User;

/**
 * Класс для построения объектов типа User.
 * Наследует функциональность от абстрактного класса Builder и реализует
 * метод создания пользователя с запросом логина и пароля.
 */
public class UserBuilder extends Builder {

    /**
     * Создает новый объект типа User, запрашивая у пользователя
     * ввод логина и пароля.
     *
     * @return новый объект User с заданными учетными данными
     * @see Builder#buildLogin() метод для построения логина
     * @see Builder#buildPassword() метод для построения пароля
     */
    public User create() {
        return new User(buildLogin(), buildPassword());
    }
}