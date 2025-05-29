package org.example.network;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс, представляющий пользователя системы.
 * Содержит данные для авторизации и методы для взаимодействия с пользователем.
 */
public class User implements Serializable {

    /** Сериализационный идентификатор для обеспечения совместимости */
    @Serial
    private static final long serialVersionUID = 33L;

    /** Логин пользователя */
    private String login;

    /** Пароль пользователя */
    private String password;

    /**
     * Запрашивает у пользователя информацию о наличии зарегистрированного аккаунта.
     * Метод продолжает запрашивать ввод до получения корректного ответа.
     *
     * @return true, если пользователь зарегистрирован, иначе false
     */
    public boolean isLogin() {
        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            System.out.println("Вы зарегистрированы? (y/n)");
            line = scanner.nextLine();
            switch (line) {
                case "y", "yes", "Y", "YES", "ДА", "да" -> {
                    return true;
                }
                case "n", "N", "no", "NO", "нет", "НЕТ" -> {
                    return false;
                }
                default -> {
                    System.out.println("Неверный ввод");
                }
            }
        }
    }

    /**
     * Конструктор, инициализирующий пользователя с заданными логином и паролем.
     *
     * @param login логин пользователя
     * @param password пароль пользователя
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Возвращает логин пользователя.
     *
     * @return логин
     */
    public String getLogin() {
        return login;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает логин пользователя.
     *
     * @param login новый логин
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password новый пароль
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Сравнивает текущего пользователя с другим объектом по логину и паролю.
     *
     * @param o объект для сравнения
     * @return true, если объекты равны, иначе false
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
     * Возвращает хеш-код пользователя на основе логина и пароля.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
