package org.example.mainClasses;

import org.example.utility.Validatable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Класс, представляющий музыкальную группу.
 * Реализует интерфейсы {@link Validatable} и {@link Comparable<MusicBand>}.
 */
public class MusicBand implements Validatable, Comparable<MusicBand>, Serializable {

    @Serial
    private static final long serialVersionUID = 228L;
    /**
     * Уникальный идентификатор музыкальной группы.
     * Значение должно быть больше 0, уникальным и генерироваться автоматически.
     */
    private long id;

    /**
     * Название музыкальной группы.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;

    /**
     * Координаты музыкальной группы.
     * Поле не может быть null.
     */
    private Coordinates coordinates;

    /**
     * Дата создания записи о музыкальной группе.
     * Поле не может быть null, значение генерируется автоматически.
     */
    private LocalDateTime creationDate;

    /**
     * Количество участников музыкальной группы.
     * Поле может быть null, значение должно быть больше 0.
     */
    private Integer numberOfParticipants;

    /**
     * Дата основания музыкальной группы.
     * Поле может быть null.
     */
    private Date establishmentDate;

    /**
     * Жанр музыкальной группы.
     * Поле может быть null.
     */
    private MusicGenre genre;

    /**
     * Лейбл музыкальной группы.
     * Поле не может быть null.
     */
    private Label label;

    /**
     * Счетчик для автоматической генерации уникального идентификатора.
     */
    public static long idcounter = 1;
    /**
     * Пользователь создавший определенный элемент
     */

    private String userLogin;

    /**
     * Конструктор для создания объекта MusicBand.
     *
     * @param name                название группы
     * @param coordinates         координаты группы
     * @param creationDate        дата создания записи
     * @param numberOfParticipants количество участников
     * @param establishmentDate   дата основания группы
     * @param genre               жанр музыки
     * @param label               лейбл группы
     */
    public MusicBand(String name, Coordinates coordinates, LocalDateTime creationDate,
                     Integer numberOfParticipants, Date establishmentDate, MusicGenre genre, Label label) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.label = label;
    }

    /**
     * Упрощенный конструктор для создания объекта MusicBand.
     * Автоматически генерирует id и creationDate.
     *
     * @param name                название группы
     * @param coordinates         координаты группы
     * @param numberOfParticipants количество участников
     * @param establishmentDate   дата основания группы
     * @param genre               жанр музыки
     * @param label               лейбл группы
     */
    public MusicBand(String name, Coordinates coordinates, LocalDateTime creationDate,
                     Integer numberOfParticipants, Date establishmentDate, MusicGenre genre,
                     Label label, String userLogin) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.label = label;
        this.userLogin = userLogin;
    }

    public MusicBand(String name, Coordinates coordinates, Integer numberOfParticipants,
                     Date establishmentDate, MusicGenre genre, Label label, String userLogin) {
        this(name, coordinates, LocalDateTime.now(), numberOfParticipants,
                establishmentDate, genre, label, userLogin);
        this.id = idcounter;
        if (validate()) {
            idcounter++;
        }
    }

    public MusicBand(Long id, String name, Coordinates coordinates, Integer numberOfParticipants,
                     LocalDateTime creationDate, MusicGenre genre, Label label, String userLogin) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.numberOfParticipants = numberOfParticipants;
        this.creationDate = creationDate;
        this.genre = genre;
        this.label = label;
        this.userLogin = userLogin;
    }

    public MusicBand(String text, Coordinates coord, int i, LocalDateTime date, MusicGenre selectedItem, Label label) {
        this.name = text;
        this.coordinates = coord;
        this.numberOfParticipants = i;
        this.creationDate = date;
        this.genre = selectedItem;
        this.label = label;
    }

    public MusicBand(MusicBand other) {
        this.id = other.id;
        this.name = other.name;
        this.coordinates = new Coordinates(other.coordinates.getX(), other.coordinates.getY());
        this.creationDate = other.creationDate != null ?
                LocalDateTime.from(other.creationDate) : null;  // Копируем creationDate
        this.numberOfParticipants = other.numberOfParticipants;
        this.establishmentDate = other.establishmentDate != null ?
                new Date(other.establishmentDate.getTime()) : null;
        this.genre = other.genre;
        this.label = other.label != null ?
                new Label(other.label.getName(), other.label.getBands(), other.label.getSales()) : null;
        this.userLogin = other.userLogin;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setEstablishmentDate(Date establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfParticipants(Integer numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    /**
     * Возвращает лейбл музыкальной группы.
     *
     * @return лейбл группы
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Возвращает название лейбла музыкальной группы.
     *
     * @return название лейбла
     */
    public String getLabelName() {
        return label.getName();
    }

    /**
     * Возвращает строковое представление лейбла.
     *
     * @return строковое представление лейбла
     */
    public String getLabels() {
        return label.toString();
    }

    /**
     * Возвращает идентификатор музыкальной группы.
     *
     * @return идентификатор группы
     */
    public long getId() {
        return this.id;
    }

    /**
     * Устанавливает идентификатор музыкальной группы.
     *
     * @param id идентификатор группы
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Возвращает строковое представление объекта MusicBand.
     *
     * @return строковое представление группы
     */
    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + (name != null ? name : "null") + '\'' +
                ", coordinates=" + (coordinates != null ? coordinates.toString() : "null") +
                ", numberOfParticipants=" + numberOfParticipants +
                ", creationDate=" + (creationDate != null ? creationDate.toString() : "null") +
                ", genre=" + (genre != null ? genre.toString() : "null") +
                ", label=" + (label != null ? label.toString() : "null") +
                '}';
    }

    /**
     * Проверяет валидность объекта MusicBand.
     *
     * @return true, если объект валиден, иначе false
     */
    @Override
    public boolean validate() {
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null) return false;
        if (numberOfParticipants != null && numberOfParticipants <= 0) return false;
        if (label == null) return false;
        return true;
    }

    /**
     * Сравнивает текущий объект MusicBand с другим объектом MusicBand по идентификатору.
     *
     * @param o объект для сравнения
     * @return результат сравнения (разница идентификаторов)
     */
    @Override
    public int compareTo(MusicBand o) {
        return (int) (this.id - o.id);
    }

    /**
     * Возвращает название музыкальной группы.
     *
     * @return название группы
     */
    public String getName() {
        return this.name;
    }

    /**
     * Возвращает жанр музыкальной группы.
     *
     * @return жанр музыки
     */
    public MusicGenre getGenre() {
        return this.genre;
    }

    /**
     * Возвращает количество участников музыкальной группы.
     *
     * @return количество участников
     */
    public int getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    /**
     * Возвращает координаты музыкальной группы.
     *
     * @return координаты группы
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }
    public String getUserLogin() {
        return this.userLogin;
    }
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }
}