package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для вывода всех элементов коллекции в строковом представлении.
 */
public class Show extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1348L;
    /**
     * Менеджер коллекции, используемый для управления элементами.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     *
     * @param collectionManager менеджер коллекции, который будет использоваться для получения данных
     */
    public Show(CollectionManager collectionManager) {
        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        try {
            return new Response(collectionManager.show());
        } catch (EmptyCollectionException e) {
            return new Response("Коллекция пуста!");
        }
    }
}