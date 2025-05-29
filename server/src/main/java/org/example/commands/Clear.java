package org.example.commands;

import org.example.exceptions.AlreadyEmptyException;
import org.example.mainClasses.MusicBand;
import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Команда для очистки всех элементов коллекции, принадлежащих пользователю.
 * Реализует шаблон команд и удаляет элементы как из памяти, так и из базы данных.
 */
public class Clear extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1338L;

    /** Менеджер коллекции, отвечает за работу с in-memory коллекцией объектов */
    private final CollectionManager collectionManager;

    /** Менеджер базы данных, обрабатывающий удаление объектов в БД */
    private final DataBaseManager dataBaseManager;

    /**
     * Конструктор команды очистки.
     *
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public Clear(CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        super("clear", "clear : очистить коллекцию");
        this.collectionManager = collectionManager;
        this.dataBaseManager = dataBaseManager;
    }

    /**
     * Выполняет очистку коллекции, удаляя только те элементы, которые принадлежат пользователю.
     *
     * @param request объект запроса, содержащий информацию о пользователе
     * @return ответ с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        // Получаем все ID объектов в коллекции, принадлежащих пользователю
        List<Long> ids = collectionManager.getBands().stream()
                .filter(musicBand -> musicBand.getUserLogin().equals(request.getUser().getLogin()))
                .map(MusicBand::getId)
                .toList();

        // Удаляем эти объекты из базы данных
        if (dataBaseManager.deleteUserObjects(request.getUser(), ids)) {
            // Если успешно удалено из БД, удаляем и из памяти
            collectionManager.removeElements(ids);
            return new Response("Элементы удалены!");
        }

        // Если в коллекции нет объектов пользователя
        return new Response("Коллекция не очищена, так как в ней нет ваших объектов.");
    }
}
