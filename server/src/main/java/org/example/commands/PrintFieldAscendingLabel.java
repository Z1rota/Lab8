package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда, выводящая значения поля "label" всех элементов коллекции
 * в порядке возрастания. Использует CollectionManager для получения отсортированных данных.
 */
public class PrintFieldAscendingLabel extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1344L;

    // Менеджер коллекции, предоставляющий доступ к элементам и их полю label
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды print_field_ascending_label.
     *
     * @param collectionManager объект, управляющий коллекцией
     */
    public PrintFieldAscendingLabel(CollectionManager collectionManager) {
        super("print_field_ascending_label",
                "print_field_ascending_label : вывести значения поля label всех элементов в порядке возрастания");
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля label всех элементов коллекции
     * в порядке возрастания. Если коллекция пуста — сообщает об этом.
     *
     * @param request объект запроса (аргументы не используются)
     * @return ответ с результатами сортировки или сообщением об ошибке
     */
    @Override
    public Response execute(Request request) {
        try {
            // Получаем отсортированные значения поля label и возвращаем их
            return new Response(collectionManager.printLabelField());
        } catch (EmptyCollectionException e) {
            // Возвращаем сообщение об ошибке, если коллекция пуста
            return new Response("Коллекция пуста!");
        }
    }
}
