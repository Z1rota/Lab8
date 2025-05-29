package org.example.utility;

import org.example.commands.*;
import org.example.managers.*;
import org.example.network.Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Фасад для инициализации и запуска приложения.
 * Предоставляет упрощенный интерфейс для настройки и старта сервера.
 */
public class Facade {

    /**
     * Конструктор по умолчанию.
     */
    public Facade() {
        // Инициализация может быть расширена при необходимости
    }

    /**
     * Запускает серверное приложение.
     * Инициализирует все необходимые менеджеры, загружает коллекцию и запускает сервер.
     *
     * @throws SQLException если произошла ошибка при работе с базой данных
     */
    public void start() throws SQLException {
        final Logger logger = Logger.getLogger("logger");

        // Инициализация менеджера команд
        CommandManager commandManager = new CommandManager();

        // Инициализация менеджера выполнения команд
        RunManager runManager = new RunManager(commandManager);

        // Инициализация менеджера базы данных
        DataBaseManager dataBaseManager = new DataBaseManager();

        // Инициализация менеджера коллекции
        CollectionManager collectionManager = new CollectionManager();

        // Регистрация всех команд в системе
        commandManager.init(commandManager, collectionManager, dataBaseManager);

        // Создание и запуск сервера
        Server server = new Server("localhost", runManager, 1782, dataBaseManager, collectionManager);

        // Загрузка начального состояния коллекции
        collectionManager.loadCollection();

        logger.info("Сервер запускается...");
        server.run();
    }
}