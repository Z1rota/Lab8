package org.example.network;

import org.example.builders.MusicBandsBuilder;
import org.example.builders.UserBuilder;
import org.example.commands.*;
import org.example.exceptions.InvalidDataException;
import org.example.mainClasses.MusicBand;
import org.example.managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Класс, отвечающий за обработку пользовательских команд и отправку соответствующих запросов на сервер.
 * Обеспечивает авторизацию/регистрацию пользователя, взаимодействие с клиентом и исполнение команд.
 */
public class RequestManager {
    private CommandManager commands;

    /** Главный пользователь, сессия которого активна */
    private User mainUser;

    /**
     * Основной метод для запуска обработки команд пользователя.
     * Включает в себя авторизацию/регистрацию, чтение ввода пользователя,
     * построение запросов и отправку их на сервер через клиент.
     *
     * @throws InvalidDataException если данные пользователя некорректны
     * @throws InterruptedException если поток был прерван
     */
    Client client = new Client("localhost", 1782, 5000, 3);

    public void ready() {
        this.commands = new CommandManager();

        // Регистрация всех доступных команд
        commands.putCommand(new Add());
        commands.putCommand(new Clear());
        commands.putCommand(new ExecuteScript());
        commands.putCommand(new GroupCountingByLabel());
        commands.putCommand(new Help());
        commands.putCommand(new Info());
        commands.putCommand(new PrintDescending());
        commands.putCommand(new PrintFieldAscendingLabel());
        commands.putCommand(new RemoveAt());
        commands.putCommand(new RemoveById());
        commands.putCommand(new RemoveFirst());
        commands.putCommand(new Show());
        commands.putCommand(new Shuffle());
        commands.putCommand(new UpdateId());
        commands.putCommand(new Login());
        commands.putCommand(new Register());

    }
    public void execute() throws InvalidDataException, InterruptedException {
        // Инициализация менеджера команд
        CommandManager commands = new CommandManager();

        // Регистрация всех доступных команд
        commands.putCommand(new Add());
        commands.putCommand(new Clear());
        commands.putCommand(new ExecuteScript());
        commands.putCommand(new GroupCountingByLabel());
        commands.putCommand(new Help());
        commands.putCommand(new Info());
        commands.putCommand(new PrintDescending());
        commands.putCommand(new PrintFieldAscendingLabel());
        commands.putCommand(new RemoveAt());
        commands.putCommand(new RemoveById());
        commands.putCommand(new RemoveFirst());
        commands.putCommand(new Show());
        commands.putCommand(new Shuffle());
        commands.putCommand(new UpdateId());
        commands.putCommand(new Login());
        commands.putCommand(new Register());

        String[] input;
        Scanner scanner = new Scanner(System.in);



        boolean success = false;


        var user = loginToDb();
        boolean isLogged = user.isLogin();

        if (isLogged) {
            Response response = client.sendRequest(new Request(new Login(), user));
            System.out.println(response.getResult());

            if (response.getLoginError() == LoginError.LOGIN_ERROR) {
                System.out.println("Такого пользователя не существует или введены неверные данные.");
                while (true) {
                    leave();
                    user = loginToDb();
                    var newResponse = client.sendRequest(new Request(new Login(), user));
                    System.out.println(newResponse.getResult());
                    if (newResponse.getLoginError() != LoginError.LOGIN_ERROR) {
                        success = true;
                        break;
                    } else {
                        break;
                    }
                }
            } else {
                success = true;
            }
        }

        if (!success) {
            Response response = client.sendRequest(new Request(new Register(), user));
            System.out.println(response.getResult());
            if (response.getLoginError() == LoginError.LOGIN_ERROR) {
                System.exit(1);
            }
        }

        System.out.println("Введите help для получения списка команд: ");

        while (true) {
            String cmd = (scanner.nextLine() + " ").trim();
            input = cmd.split(" ");

            if (input[0].equals("exit")) {
                System.out.println("До связи");
                System.exit(0);
            }

            if (commands.getCommands().get(input[0]) == null) {
                System.err.println("команды нет!");
                continue;
            }

            Command command = commands.getCommands().get(input[0]);

            if (!command.isHasArgs()) {
                if (input.length != 1) {
                    System.err.println("у этой команды не должно быть аргументов");
                    continue;
                }

                if (input[0].equals("add")) {
                    MusicBand band = new MusicBandsBuilder(user).create();
                    band.setUserLogin(user.getLogin());
                    System.out.println(client.sendRequest(new Request(command, band, user)).getResult());
                } else {
                    try {
                        Request request = new Request(command, user);
                        System.out.println(client.sendRequest(request).getResult());
                    } catch (NullPointerException e) {
                        System.out.println("Клиент не смог подключиться к серверу");
                        System.exit(505);
                    }
                }
                continue;
            }

            if (input.length != 2) {
                System.err.println("Команде нужен только один аргумент");
                continue;
            }

            if (!input[0].equals("execute_script")) {
                long id = Long.parseLong(input[1]);

                if (input[0].equals("update")) {
                    MusicBand band = new MusicBandsBuilder(user).create();
                    band.setUserLogin(user.getLogin());
                    System.out.println(client.sendRequest(new Request(command, band, id, user)).getResult());
                    continue;
                }

                if (input[0].equals("remove_at") || input[0].equals("remove_by_id")) {
                    System.out.println(client.sendRequest(new Request(command, id, user)).getResult());
                }

            } else {
                String scriptName = input[1];

                Set<String> visitedScripts = new HashSet<>();
                if (hasRecursion(scriptName, visitedScripts)) {
                    System.out.println("Обнаружена рекурсия при выполнении скрипта: " + scriptName);
                    continue;
                }

                Request request = new Request(command, scriptName, user);
                System.out.println(client.sendRequest(request).getResult());
            }
        }
    }

    public boolean checkLogin(User user) throws InterruptedException {
        Response response = client.sendRequest(new Request(new Login(), user));
        if (response.getLoginError() == LoginError.LOGIN_ERROR) {
            return false;
        }
        return true;
    }
    public boolean register(User user) throws InterruptedException {
        Response response = client.sendRequest(new Request(new Register(), user));
        if (response.getLoginError() == LoginError.LOGIN_ERROR) { return false;}
        return true;
    }

    public User loginToDb() throws InvalidDataException {
        return new UserBuilder().create();
    }

    /**
     * Метод для проверки скрипта на наличие рекурсии до его отправки на сервер.
     *
     * @param scriptName     имя скрипта для проверки
     * @param visitedScripts множество уже проверенных скриптов
     * @return true, если обнаружена рекурсия, иначе false
     */
    private boolean hasRecursion(String scriptName, Set<String> visitedScripts) {
        if (visitedScripts.contains(scriptName)) {
            return true;
        }

        visitedScripts.add(scriptName);

        try (Scanner fileScanner = new Scanner(new File(scriptName))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.startsWith("execute_script")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        String nestedScript = parts[1];
                        if (hasRecursion(nestedScript, visitedScripts)) {
                            return true;
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + scriptName);
        }

        visitedScripts.remove(scriptName);
        return false;
    }

    /**
     * Метод для создания пользователя через билдер.
     *
     * @return созданный пользователь
     * @throws InvalidDataException если введённые данные недопустимы
     */


    /**
     * Метод для выхода пользователя из режима входа.
     * Запрашивает у пользователя подтверждение выхода.
     */
    private void leave() {
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите \"exit\" если у вас нет существующего аккаунта, иначе нажмите Enter");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                System.exit(1);
            }
            if (input.isBlank()) {
                break;
            } else {
                System.out.println("ну нормально же общались, нажми Enter");
            }
        }
    }

    public Client getClient() {
        return client;
    }
}
