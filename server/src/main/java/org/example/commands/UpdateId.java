package org.example.commands;

import org.example.exceptions.InvalidDataException;
import org.example.exceptions.NoElementException;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для обновления элемента коллекции по заданному идентификатору.
 * Обновление производится как в базе данных, так и в коллекции в памяти.
 */
public class UpdateId extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1350L;

    // Менеджер коллекции, используемый для обновления объекта в памяти
    private final CollectionManager collectionManager;

    // Менеджер базы данных, используемый для обновления объекта в БД
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды update.
     *
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public UpdateId(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("update", "update id {element} : обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет обновление элемента коллекции.
     * Сначала обновляет данные в базе, затем — в памяти.
     *
     * @param request объект запроса, содержащий id, объект и пользователя
     * @return результат выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
            Long id = (Long) request.getArgs();

            // Сначала пробуем обновить объект в БД
            if (dataBaseManager.updateObject(id, request.getUser(), request.getMusicBand())) {
                // Если обновление в БД прошло успешно — обновляем в памяти
                collectionManager.updateId(id, request.getMusicBand());
                return new Response("Объект успешно изменен");
            }

            return new Response("Объект не изменен");

        } catch (InvalidDataException e) {
            return new Response("Введены неверные данные!");
        } catch (NoElementException e) {
            return new Response("Объекта с таким id нет!");
        } catch (NumberFormatException e) {
            return new Response("Введите значение типа long");
        }
    }
}
