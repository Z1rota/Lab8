package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда, выполняющая группировку элементов коллекции по значению поля label.
 * После группировки выводится количество элементов в каждой из групп.
 */
public class GroupCountingByLabel extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1340L;

    // Менеджер коллекции, содержащей элементы, которые нужно сгруппировать
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды group_counting_by_label.
     *
     * @param collectionManager менеджер, управляющий основной коллекцией
     */
    public GroupCountingByLabel(CollectionManager collectionManager) {
        super("group_counting_by_label", "group_counting_by_label : сгруппировать элементы по label и вывести количество в каждой группе");
        this.collectionManager = collectionManager;
    }

    /**
     * Метод выполнения команды. Пытается сгруппировать элементы коллекции по полю label.
     * Если коллекция пуста, возвращается соответствующее сообщение.
     *
     * @param request объект запроса с пользовательскими данными
     * @return результат группировки или сообщение об ошибке
     */
    @Override
    public Response execute(Request request) {
        try {
            return new Response(collectionManager.groupByLabel()); // Выполнение логики группировки
        } catch (EmptyCollectionException e) {
            return new Response("Коллекция пуста!"); // Возвращает сообщение, если коллекция не содержит элементов
        }
    }
}
