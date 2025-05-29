package org.example.network;

import org.example.commands.Command;
import org.example.mainClasses.MusicBand;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс для передачи запросов между клиентом и сервером.
 * Содержит команду, аргументы, данные музыкальной группы и информацию о пользователе.
 * Реализует интерфейс Serializable для поддержки сериализации при передаче по сети.
 */
public class Request implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 21L;

    /** Музыкальная группа, связанная с запросом */
    public MusicBand musicBand;

    /** Команда для выполнения */
    private Command command;

    /** Аргументы команды */
    private Object args;

    /** Пользователь, отправивший запрос */
    private User user;

    /** Строка регистрации (для специальных случаев) */
    private String register;

    /**
     * Конструктор для запросов регистрации.
     * @param register строка регистрации
     * @param user пользователь
     */
    public Request(String register, User user) {
        this.register = register;
        this.user = user;
    }

    /**
     * Конструктор для запросов с командой.
     * @param command команда для выполнения
     * @param user пользователь
     */
    public Request(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    /**
     * Конструктор для запросов с командой и аргументами.
     * @param command команда для выполнения
     * @param args аргументы команды
     * @param user пользователь
     */
    public Request(Command command, Object args, User user) {
        this.user = user;
        this.command = command;
        this.args = args;
    }

    /**
     * Конструктор для запросов с командой, музыкальной группой и аргументами.
     * @param command команда для выполнения
     * @param band музыкальная группа
     * @param args аргументы команды
     * @param user пользователь
     */
    public Request(Command command, MusicBand band, Object args, User user) {
        this.command = command;
        this.musicBand = band;
        this.args = args;
        this.user = user;
    }

    /**
     * Конструктор для запросов с музыкальной группой.
     * @param musicBand музыкальная группа
     * @param user пользователь
     */
    public Request(MusicBand musicBand, User user) {
        this.musicBand = musicBand;
        this.user = user;
    }

    /**
     * Конструктор для запросов с командой и музыкальной группой.
     * @param command команда для выполнения
     * @param band музыкальная группа
     * @param user пользователь
     */
    public Request(Command command, MusicBand band, User user) {
        this.command = command;
        this.musicBand = band;
        this.user = user;
    }

    /**
     * Конструктор для запросов со строковым аргументом.
     * @param string строковый аргумент
     */
    public Request(String string) {
        this.args = string;
    }

    /**
     * Возвращает команду запроса.
     * @return объект Command
     */
    public Command getCommand() {
        return this.command;
    }

    /**
     * Возвращает музыкальную группу, связанную с запросом.
     * @return объект MusicBand
     */
    public MusicBand getMusicBand() {
        return this.musicBand;
    }

    /**
     * Возвращает аргументы команды.
     * @return аргументы команды
     */
    public Object getArgs() {
        return args;
    }

    /**
     * Возвращает пользователя, отправившего запрос.
     * @return объект User
     */
    public User getUser() {
        return this.user;
    }
}