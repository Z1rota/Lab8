package org.example.network;

import org.example.commands.Command;
import org.example.mainClasses.MusicBand;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс, представляющий запрос клиента к серверу.
 * Хранит информацию о команде, аргументах, объекте MusicBand и пользователе, отправившем запрос.
 * Используется для сериализации данных при передаче по сети.
 */
public class Request implements Serializable {

    /** Уникальный идентификатор версии сериализуемого класса */
    @Serial
    private static final long serialVersionUID = 21L;

    /** Объект музыкальной группы, передаваемый в запросе (если требуется) */
    public MusicBand musicBand;

    /** Команда, которую необходимо выполнить на сервере */
    Command command;

    /** Аргументы команды (может быть строка, число, и т.д.) */
    Object args;

    /** Пользователь, отправивший запрос */
    User user;

    /**
     * Конструктор запроса с командой и пользователем.
     *
     * @param command команда для выполнения
     * @param user пользователь, отправивший запрос
     */
    public Request(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    /**
     * Конструктор запроса только с аргументом (например, строкой).
     *
     * @param string аргумент запроса
     */
    public Request(String string) {
        this.args = string;
    }

    /**
     * Конструктор запроса с командой, аргументом и пользователем.
     *
     * @param command команда для выполнения
     * @param args аргумент команды
     * @param user пользователь, отправивший запрос
     */
    public Request(Command command, Object args, User user) {
        this.command = command;
        this.args = args;
        this.user = user;
    }

    /**
     * Конструктор запроса с командой, объектом MusicBand и пользователем.
     *
     * @param command команда для выполнения
     * @param band объект музыкальной группы
     * @param user пользователь, отправивший запрос
     */
    public Request(Command command, MusicBand band, User user) {
        this.command = command;
        this.musicBand = band;
        this.user = user;
    }

    /**
     * Конструктор запроса только с музыкальной группой и пользователем.
     *
     * @param musicBand объект музыкальной группы
     * @param user пользователь, отправивший запрос
     */
    public Request(MusicBand musicBand, User user) {
        this.musicBand = musicBand;
        this.user = user;
    }


    /**
     * Конструктор запроса с командой, музыкальной группой, аргументами и пользователем.
     *
     * @param command команда для выполнения
     * @param band объект музыкальной группы
     * @param args аргументы команды
     * @param user пользователь, отправивший запрос
     */
    public Request(Command command, MusicBand band, Object args, User user) {
        this.command = command;
        this.musicBand = band;
        this.args = args;
        this.user = user;
    }

    /**
     * Получить команду из запроса.
     *
     * @return команда
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Получить объект музыкальной группы из запроса.
     *
     * @return объект MusicBand
     */
    public MusicBand getMusicBand() {
        return musicBand;
    }

    /**
     * Конструктор запроса только с командой.
     *
     * @param command команда для выполнения
     */
    public Request(Command command) {
        this.command = command;
    }

    /**
     * Получить аргументы запроса.
     *
     * @return объект аргумента
     */
    public Object getArgs() {
        return args;
    }
}
