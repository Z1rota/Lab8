package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для удаления первого элемента из коллекции.
 * Удаление происходит как из базы данных, так и из коллекции в памяти.
 */
public class RemoveFirst extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1347L;

    // Менеджер коллекции, управляющий элементами в памяти
    private final CollectionManager collectionManager;

    // Менеджер базы данных, обеспечивающий взаимодействие с БД
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды remove_first.
     *
     * @param collectionManager объект, управляющий коллекцией
     * @param databaseManager объект, управляющий работой с базой данных
     */
    public RemoveFirst(CollectionManager collectionManager, DataBaseManager databaseManager) {
        super("remove_first", "remove_first : удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
        this.dataBaseManager = databaseManager;
    }

    /**
     * Выполняет удаление первого элемента из коллекции.
     * Сначала удаляет элемент в базе данных, затем в памяти.
     *
     * @param request объект запроса, содержащий пользователя
     * @return результат выполнения команды в виде объекта Response
     */
    @Override
    public Response execute(Request request) {
        try {
            // Получаем ID первого элемента и пробуем удалить его из базы данных
            if (dataBaseManager.deleteObject(request.getUser(), collectionManager.getFirstId())) {
                // Если успешно удалено из БД, удаляем также из коллекции в памяти
                collectionManager.removeFirst();
                return new Response("Элемент успешно удален!");
            } else {
                return new Response("Элемент не удален");
            }
        } catch (EmptyCollectionException e) {
            // Обработка случая, когда коллекция пуста
            return new Response("Коллекция пуста!");
        }
    }
}
