package org.example.commands;

import org.example.builders.MusicBandsBuilder;
import org.example.mainClasses.MusicBand;
import org.example.managers.CollectionManager;
import org.example.exceptions.InvalidDataException;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс команды "add", реализует добавление нового элемента в коллекцию.
 * Используется как часть командного шаблона.
 */
public class Add extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1337L;

    /** Менеджер коллекции, управляющий всеми музыкальными группами в памяти */
    private final CollectionManager collectionManager;

    /** Менеджер базы данных, обрабатывающий взаимодействие с базой */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды добавления.
     *
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public Add(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("add", "add {element} : добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет команду добавления.
     * Получает объект MusicBand из запроса, добавляет его в базу данных и в коллекцию.
     *
     * @param request объект запроса, содержащий данные и пользователя
     * @return объект ответа с результатом выполнения
     */
    @Override
    public Response execute(Request request) {
        try {
            MusicBand band = request.getMusicBand(); // Получение музыкальной группы из запроса
            int id = dataBaseManager.addbands(band, request.getUser()); // Добавление в базу данных
            if (id == -1) {
                return new Response("Не удалось добавить объект"); // Ошибка при добавлении
            }
            request.musicBand.setId((long) id); // Установка ID, полученного из базы
            collectionManager.add(band); // Добавление в локальную коллекцию
            return new Response("Объект добавлен в коллекцию!");
        } catch (InvalidDataException e) {
            return new Response("Объект не создан. Проверьте правильность данных"); // Исключение при некорректных данных
        }
    }
}
