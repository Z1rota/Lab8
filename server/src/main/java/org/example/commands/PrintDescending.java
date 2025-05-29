package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда, выводящая элементы коллекции в порядке убывания.
 * Использует менеджер коллекции для получения отсортированных данных.
 */
public class PrintDescending extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1343L;

    // Менеджер коллекции, предоставляющий доступ к элементам и их сортировке
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды print_descending.
     *
     * @param collectionManager объект, управляющий коллекцией и предоставляющий методы для её обработки
     */
    public PrintDescending(CollectionManager collectionManager) {
        super("print_descending", "print_descending : вывести элементы коллекции в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода элементов в порядке убывания.
     * В случае пустой коллекции возвращает сообщение об ошибке.
     *
     * @param request объект запроса (не используется данной командой)
     * @return объект ответа с результатом выполнения команды
     */
    @Override
    public Response execute(Request request) {
        try {
            // Получаем и возвращаем отсортированные в порядке убывания элементы
            return new Response(collectionManager.printAscend());
        } catch (EmptyCollectionException e) {
            // Если коллекция пуста — возвращаем соответствующее сообщение
            return new Response("Коллекция пуста!");
        }
    }
}
