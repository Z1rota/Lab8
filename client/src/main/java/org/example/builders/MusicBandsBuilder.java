package org.example.builders;

import org.example.exceptions.InvalidDataException;
import org.example.mainClasses.MusicBand;
import org.example.network.User;

/**
 * Класс для построения объектов типа MusicBand.
 * Теперь учитывает пользователя, создающего группу.
 */
public class MusicBandsBuilder extends Builder {
    private User currentUser;

    /**
     * Конструктор с указанием текущего пользователя
     * @param currentUser пользователь, создающий группу
     */
    public MusicBandsBuilder(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Создает объект типа MusicBand с учетом текущего пользователя
     * @return объект типа MusicBand
     * @throws InvalidDataException если введенные данные некорректны
     */
    public MusicBand create() throws InvalidDataException {
        return new MusicBand(
                buildString("name"),
                new CoordinatesBuilder().create(),
                buildInt("кол-во участников"),
                new EstabilishmentDateBuilder().create(),
                new MusicGenreBuilder().create(),
                new LabelBuilder().create(),
                currentUser.getLogin() // Устанавливаем владельца группы
        );
    }

    /**
     * Обновляет текущего пользователя
     * @param user новый текущий пользователь
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}