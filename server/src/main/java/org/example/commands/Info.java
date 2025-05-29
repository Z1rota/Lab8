package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда, выводящая информацию о текущей коллекции.
 * Включает тип коллекции, дату инициализации, количество элементов и другую общую информацию.
 */
public class Info extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1342L;

    // Менеджер коллекции, содержащий методы получения сведений о ней
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды info.
     *
     * @param collectionManager менеджер коллекции, из которой будет извлекаться информация
     */
    public Info(CollectionManager collectionManager) {
        super("info", "info : вывести в стандартный поток вывода информацию о коллекции " +
                "(тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду info. Получает сведения о коллекции через менеджер и возвращает их пользователю.
     *
     * @param request объект запроса (не используется напрямую, но передаётся по протоколу)
     * @return объект ответа с информацией о коллекции
     */
    @Override
    public Response execute(Request request) {
        StringBuilder response = new StringBuilder().append("Информация о коллекции: \n");
        response.append(collectionManager.info());
        return new Response(response.toString());
    }
}
