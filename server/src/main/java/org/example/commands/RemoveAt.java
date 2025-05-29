package org.example.commands;

import org.example.exceptions.NoElementException;
import org.example.mainClasses.MusicBand;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда 'remove_at' - удаляет элемент коллекции по указанному индексу.
 * Проверяет права пользователя на удаление элемента и выполняет удаление
 * как из коллекции, так и из базы данных.
 * Наследует функциональность от абстрактного класса Command
 * и реализует интерфейс Serializable для поддержки сериализации.
 */
public class RemoveAt extends Command implements Serializable {

    /**
     * Уникальный идентификатор версии сериализации.
     * Обеспечивает корректную десериализацию объекта между разными версиями класса.
     */
    @Serial
    private static final long serialVersionUID = 1345L;

    /**
     * Менеджер коллекции для доступа и управления элементами
     */
    private final CollectionManager collectionManager;

    /**
     * Менеджер базы данных для проверки прав и удаления элементов
     */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды remove_at.
     *
     * @param collectionManager менеджер коллекции для работы с элементами
     * @param dataBaseManager менеджер базы данных для проверки прав доступа
     */
    public RemoveAt(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("remove_at",
                "remove_at index : удалить элемент, находящийся в заданной позиции коллекции (index)");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет удаление элемента по указанному индексу.
     * Проверяет права пользователя перед удалением и удаляет элемент
     * как из коллекции, так и из базы данных.
     *
     * @param request объект запроса, содержащий индекс и данные пользователя
     * @return объект Response с результатом выполнения операции:
     *         - сообщение об успешном удалении
     *         - сообщение об ошибке, если элемент не найден или нет прав доступа
     *         - сообщение об ошибке формата, если индекс не является числом
     */
    @Override
    public Response execute(Request request) {
        try {
            long num = (long) request.getArgs();
            MusicBand band = collectionManager.getBands().get((int) num);

            if (dataBaseManager.deleteObject(request.getUser(), band.getId())) {
                collectionManager.removeAt((int) num);
                return new Response("Объект успешно удален!");
            } else {
                return new Response("Объект не удален");
            }
        } catch (NoElementException e) {
            return new Response("Элемента под этим индексом нет!");
        } catch (NumberFormatException e) {
            return new Response("Введите число!");
        }
    }
}