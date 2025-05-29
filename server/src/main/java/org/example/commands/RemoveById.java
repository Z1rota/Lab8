package org.example.commands;

import org.example.exceptions.NoElementException;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для удаления элемента из коллекции по его идентификатору.
 * Использует CollectionManager для удаления из памяти и DataBaseManager для удаления из базы данных.
 */
public class RemoveById extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1346L;

    // Менеджер коллекции, управляющий элементами в памяти
    private final CollectionManager collectionManager;

    // Менеджер базы данных, обеспечивающий удаление из БД
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды remove_by_id.
     *
     * @param collectionManager объект, управляющий коллекцией
     * @param dataBaseManager объект, управляющий работой с базой данных
     */
    public RemoveById(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("remove_by_id", "remove_by_id id : удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет удаление элемента по id. Проверяет наличие доступа через базу данных,
     * а затем удаляет элемент из коллекции. В случае отсутствия элемента выбрасывается исключение.
     *
     * @param request объект запроса, содержащий id элемента и пользователя
     * @return результат выполнения команды в виде объекта Response
     */
    @Override
    public Response execute(Request request) {
        try {
            // Пытаемся удалить элемент в базе данных по id и пользователю
            if (dataBaseManager.deleteObject(request.getUser(), (long) request.getArgs())) {
                // Удаляем элемент из коллекции в памяти
                collectionManager.removeById((long) request.getArgs());
                return new Response("Элемент успешно удален");
            } else {
                return new Response("Элемент не удален");
            }
        } catch (NoElementException e) {
            // Если элемент с заданным id не найден
            return new Response("Элемента с таким айди нет");
        }
    }
}
