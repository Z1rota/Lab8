package org.example.managers;

import org.example.mainClasses.Coordinates;
import org.example.mainClasses.Label;
import org.example.mainClasses.MusicBand;
import org.example.mainClasses.MusicGenre;
import org.example.network.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Менеджер для работы с базой данных приложения.
 * Обеспечивает все операции с базой данных: аутентификацию пользователей,
 * CRUD операции с музыкальными группами, управление соединением с БД.
 */
public class DataBaseManager {
    /** Соединение с базой данных */
    private final Connection connection;

    /** Менеджер SQL-запросов */
    private final QueryManager queryManager = new QueryManager();

    /**
     * Конструктор менеджера БД.
     * Инициализирует соединение с базой данных на основе параметров из файла properties.txt.
     * @throws SQLException если не удалось установить соединение с БД
     */
    public DataBaseManager() throws SQLException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("properties.txt")) {
            if (input == null) {
                throw new SQLException("\"properties.txt\" не найден");
            }
            Properties prop = new Properties();
            prop.load(input);
            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String password = prop.getProperty("db.password");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (IOException e) {
            throw new SQLException("Ошибка загрузки конфигурации БД");
        }
    }

    /**
     * Закрывает соединение с базой данных.
     * @throws SQLException если произошла ошибка при закрытии соединения
     */
    public void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    /**
     * Проверяет существование пользователя в базе данных.
     * @param user пользователь для проверки
     * @return true если пользователь существует и пароль верный, иначе false
     */
    public boolean existUser(User user) {
        try {
            PasswordManager passwordManager = new PasswordManager();
            Connection connection = this.connection;
            PreparedStatement preparedStatement = connection.prepareStatement(queryManager.findingUser);
            preparedStatement.setString(1, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String password = user.getPassword() + resultSet.getString("salt");
                if(resultSet.getString("password").equals(passwordManager.hashPassword(password))) {
                    resultSet.close();
                    return true;
                }

            }
        } catch (SQLException e) {
            System.err.println("Ошибка выполнения запроса");
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Добавляет нового пользователя в базу данных.
     * @param user пользователь для добавления
     */
    public void addUser(User user) {
        try {
            PasswordManager passwordManager = new PasswordManager();
            String salt = saltGenerator();
            Connection connection = this.connection;
            String password = passwordManager.hashPassword(user.getPassword() + salt);
            PreparedStatement pr = connection.prepareStatement(queryManager.addUser);
            pr.setString(1, user.getLogin());
            pr.setString(2, password);
            pr.setString(3, salt);
            pr.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса");
            e.printStackTrace();
        }
    }

    /**
     * Добавляет музыкальную группу в базу данных.
     * @param band музыкальная группа для добавления
     * @param user пользователь, добавляющий группу
     * @return ID добавленной группы или -1 при ошибке
     */
    public int addbands(MusicBand band, User user) {
        Connection connection = this.connection;
        try {
            PreparedStatement pr = connection.prepareStatement(queryManager.addBand);
            pr.setString(1,band.getName());
            pr.setFloat(2, band.getCoordinates().getX());
            pr.setLong(3, band.getCoordinates().getY());
            pr.setInt(4,band.getNumberOfParticipants());
            pr.setString(5,band.getCreationDate());
            pr.setString(6,band.getGenre().toString());
            pr.setString(7, band.getLabelName());
            pr.setInt(8,band.getLabel().getBands());
            pr.setLong(9,band.getLabel().getSales());
            pr.setString(10,user.getLogin());
            ResultSet resultSet = pr.executeQuery();
            if (!resultSet.next()) {
                System.err.println("Не удалось добавить объект");
                resultSet.close();
                return -1;
            }
            System.err.println("Объект успешно добавлен");
            int num = resultSet.getInt(1);
            resultSet.close();
            return num;
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Обновляет музыкальную группу в базе данных.
     * @param id ID группы для обновления
     * @param user пользователь, выполняющий обновление
     * @param band новые данные группы
     * @return true если обновление успешно, иначе false
     */
    public boolean updateObject(Long id, User user, MusicBand band) {
        Connection connection = this.connection;
        try {
            PreparedStatement pr = connection.prepareStatement(queryManager.updateObj);
            pr.setString(1,band.getName());
            pr.setFloat(2, band.getCoordinates().getX());
            pr.setLong(3,band.getCoordinates().getY());
            pr.setInt(4,band.getNumberOfParticipants());
            pr.setString(5,band.getCreationDate());
            pr.setString(6,band.getGenre().toString());
            pr.setString(7,band.getLabelName());
            pr.setInt(8,band.getLabel().getBands());
            pr.setLong(9,band.getLabel().getSales());
            pr.setString(10,band.getUserLogin());
            pr.setLong(11,id);
            int rows = pr.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Удаляет музыкальные группы пользователя по списку ID.
     * @param user пользователь, чьи группы удаляются
     * @param ids список ID групп для удаления
     * @return true если удаление успешно, иначе false
     */
    public boolean deleteUserObjects(User user, List<Long> ids) {
        Connection connection = this.connection;
        try {
            for (long id: ids) {
                PreparedStatement pr = connection.prepareStatement(queryManager.deleteObj);
                pr.setString(1, user.getLogin());
                pr.setLong(2,id);
                ResultSet resultSet = pr.executeQuery();
                resultSet.close();
                return resultSet.next();

            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Удаляет музыкальную группу по ID.
     * @param user пользователь, выполняющий удаление
     * @param id ID группы для удаления
     * @return true если удаление успешно, иначе false
     */
    public Boolean deleteObject(User user, long id) {
        Connection connection = this.connection;
        try {
            PreparedStatement pr = connection.prepareStatement(queryManager.deleteObj);
            pr.setString(1,user.getLogin());
            pr.setLong(2,id);
            ResultSet resultSet = pr.executeQuery();
            resultSet.close();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Создает коллекцию музыкальных групп из базы данных.
     * @return стек объектов MusicBand
     */
    public Stack<MusicBand> createCollection() {
        Connection connection = this.connection;
        Stack<MusicBand> bands = new Stack<>();
        try {
            try {
                PreparedStatement pr = connection.prepareStatement(queryManager.addObjects);
                ResultSet resultSet = pr.executeQuery();
                while (resultSet.next()) {
                    bands.add(new MusicBand(resultSet.getLong(1),
                            resultSet.getString(2),
                            new Coordinates(resultSet.getFloat(3),resultSet.getLong(4)),
                            resultSet.getInt(5),
                            LocalDateTime.parse(resultSet.getString(6)),
                            MusicGenre.valueOf(resultSet.getString(7)),
                            new Label(resultSet.getString(8),resultSet.getInt(9),
                                    resultSet.getLong(10))));
                }
                resultSet.close();
                return bands;
            } catch (SQLException e) {
                System.err.println("Ошибка выполнения запроса");
                return new Stack<>();
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Поля Объектов не валидны");
            return new Stack<>();
        }
    }

    /**
     * Генерирует случайную соль для хеширования паролей.
     * @return строка с случайной солью
     */
    private String saltGenerator() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(15);

        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}