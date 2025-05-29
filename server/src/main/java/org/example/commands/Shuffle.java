package org.example.commands;

import org.example.exceptions.EmptyCollectionException;
import org.example.managers.CollectionManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда для перемешивания элементов коллекции в случайном порядке.
 */
public class Shuffle extends Command implements Serializable {


    @Serial
    private static final long serialVersionUID = 1349L;
    /**
     * Менеджер коллекции, используемый для управления элементами.
     */
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды.
     *
     * @param collectionManager менеджер коллекции, который будет использоваться для перемешивания элементов
     */
    public Shuffle(CollectionManager collectionManager) {
        super("shuffle", "shuffle : перемешать элементы коллекции в случайном порядке");
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        try {
            collectionManager.shuffle();
            return new Response("Коллекция перемешана успешно!");
        } catch (EmptyCollectionException e) {
            return new Response("Коллекция пуста!");
        }
    }
}