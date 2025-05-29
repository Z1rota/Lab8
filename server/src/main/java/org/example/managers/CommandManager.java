package org.example.managers;

import org.example.commands.*;
import org.example.network.Request;
import org.example.network.Response;

import java.util.HashMap;

/**
 * Менеджер команд - центральный класс для управления всеми командами приложения.
 * Регистрирует команды, предоставляет доступ к списку команд и выполняет запрошенные команды.
 */
public class CommandManager {

    /** Хранилище зарегистрированных команд (имя команды -> объект команды) */
    private HashMap<String, Command> commands = new HashMap<>();

    /**
     * Добавляет команду в менеджер.
     * @param command команда для добавления
     */
    public void addCommand(Command command) {
        this.commands.put(command.getName(), command);
    }

    /**
     * Возвращает все зарегистрированные команды.
     * @return HashMap с командами (имя -> команда)
     */
    public HashMap<String, Command> getCommands() {
        return commands;
    }

    /**
     * Инициализирует менеджер команд, регистрируя все доступные команды.
     * @param commandManager экземпляр менеджера команд
     * @param collectionManager менеджер коллекции
     * @param dataBaseManager менеджер базы данных
     */
    public void init(CommandManager commandManager, CollectionManager collectionManager, DataBaseManager dataBaseManager) {
        this.addCommand(new Add(collectionManager, dataBaseManager));
        commandManager.addCommand(new Clear(collectionManager, dataBaseManager));
        commandManager.addCommand(new ExecuteScript(commandManager));
        commandManager.addCommand(new GroupCountingByLabel(collectionManager));
        commandManager.addCommand(new Help(collectionManager, commandManager));
        commandManager.addCommand(new Info(collectionManager));
        commandManager.addCommand(new PrintDescending(collectionManager));
        commandManager.addCommand(new PrintFieldAscendingLabel(collectionManager));
        commandManager.addCommand(new RemoveAt(collectionManager, dataBaseManager));
        commandManager.addCommand(new RemoveById(collectionManager, dataBaseManager));
        commandManager.addCommand(new RemoveFirst(collectionManager, dataBaseManager));
        commandManager.addCommand(new Show(collectionManager));
        commandManager.addCommand(new Shuffle(collectionManager));
        commandManager.addCommand(new UpdateId(collectionManager, dataBaseManager));
        commandManager.addCommand(new Login(dataBaseManager));
        commandManager.addCommand(new Register(dataBaseManager));
    }

    /**
     * Выполняет команду на основе полученного запроса.
     * @param request запрос, содержащий команду и аргументы
     * @return результат выполнения команды
     */
    public Response execute(Request request) {
        Command command = this.commands.get(request.getCommand().getName());
        if (command != null) {
            return command.execute(request);
        } else {
            return new Response("Команды нет");
        }
    }
}