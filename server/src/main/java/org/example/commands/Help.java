package org.example.commands;

import org.example.managers.CollectionManager;
import org.example.managers.CommandManager;
import org.example.network.Request;
import org.example.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * Команда, выводящая список всех доступных команд с их описанием.
 * Используется для предоставления справочной информации пользователю.
 */
public class Help extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1341L;

    // Менеджер коллекции (не используется в данной реализации, но передаётся в конструктор)
    private final CollectionManager collectionManager;

    // Менеджер команд, содержащий информацию о всех зарегистрированных командах
    private final CommandManager commandManager;

    /**
     * Конструктор команды help.
     *
     * @param collectionManager менеджер коллекции (резервный, может пригодиться в будущем)
     * @param commandManager менеджер, содержащий список всех доступных команд
     */
    public Help(CollectionManager collectionManager, CommandManager commandManager) {
        super("help", "help : вывести справку по доступным командам");
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду help. Формирует строку с описаниями всех доступных команд.
     *
     * @param request объект запроса (не используется, но передаётся по интерфейсу)
     * @return объект ответа со справочной информацией по командам
     */
    @Override
    public Response execute(Request request) {
        StringBuilder resp = new StringBuilder().append("Список команд: \n");
        commandManager.getCommands().values().forEach(command -> resp.append(command.getDescription()).append("\n"));
        return new Response(resp.toString());
    }
}
