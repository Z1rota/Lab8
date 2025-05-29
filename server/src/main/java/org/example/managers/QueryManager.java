package org.example.managers;

/**
 * Менеджер SQL-запросов.
 * Содержит предопределенные SQL-запросы для работы с базой данных.
 * Все запросы используют параметризованный ввод для защиты от SQL-инъекций.
 */
public class QueryManager {

    /**
     * SQL-запрос для поиска пользователя и получения его пароля и соли.
     * Параметры:
     * 1. Имя пользователя (String)
     */
    public final String findingUser = "SELECT password, salt FROM users WHERE name = ?;";

    /**
     * SQL-запрос для получения соли пользователя.
     * Параметры:
     * 1. Имя пользователя (String)
     */
    public final String getPassword = "SELECT salt FROM users WHERE name = ?;";

    /**
     * SQL-запрос для добавления нового пользователя.
     * Параметры:
     * 1. Имя пользователя (String)
     * 2. Хеш пароля (String)
     * 3. Соль (String)
     */
    public final String addUser = "INSERT INTO users (name, password, salt) VALUES (?, ?, ?);";

    /**
     * SQL-запрос для добавления музыкальной группы.
     * Параметры:
     * 1. Название группы (String)
     * 2. Координата X (float)
     * 3. Координата Y (long)
     * 4. Количество участников (int)
     * 5. Дата создания (String)
     * 6. Жанр (String)
     * 7. Название лейбла (String)
     * 8. Количество групп на лейбле (int)
     * 9. Продажи лейбла (long)
     * 10. Логин пользователя (String)
     * Возвращает: ID добавленной записи
     */
    public final String addBand = "INSERT INTO bands(name, x, y, participantsnum, creationdate, "
            + "genre, labelname, labelbands, labelsales, userlogin) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?) RETURNING id;";

    /**
     * SQL-запрос для удаления музыкальной группы.
     * Параметры:
     * 1. Логин пользователя (String)
     * 2. ID группы (long)
     * Возвращает: ID удаленной записи
     */
    public final String deleteObj = "DELETE FROM bands WHERE (userlogin = ?) AND (id = ?) RETURNING id;";

    /**
     * SQL-запрос для обновления информации о музыкальной группе.
     * Параметры:
     * 1. Название группы (String)
     * 2. Координата X (float)
     * 3. Координата Y (long)
     * 4. Количество участников (int)
     * 5. Дата создания (String)
     * 6. Жанр (String)
     * 7. Название лейбла (String)
     * 8. Количество групп на лейбле (int)
     * 9. Продажи лейбла (long)
     * 10. Логин пользователя (String)
     * 11. ID группы (long)
     */
    public final String updateObj = "UPDATE bands SET name = ?, x = ?, y = ?, participantsnum = ?, "
            + "creationdate = ?, genre = ?, labelname = ?, labelbands = ?, "
            + "labelsales = ? WHERE userlogin = ? AND id = ?";

    /**
     * SQL-запрос для получения всех музыкальных групп.
     * Возвращает все поля таблицы bands.
     */
    public final String addObjects = "SELECT * FROM bands;";
}