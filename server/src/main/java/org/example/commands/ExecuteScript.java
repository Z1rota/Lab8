package org.example.commands;

import org.example.builders.MusicBandsBuilder;
import org.example.mainClasses.MusicBand;
import org.example.managers.CommandManager;
import org.example.managers.ScriptExecuteManager;
import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.FileMode;

import java.io.*;
import java.util.NoSuchElementException;

/**
 * Команда, позволяющая выполнить скрипт команд из указанного файла.
 * Скрипт должен содержать команды в том же формате, что и в интерактивном режиме.
 */
public class ExecuteScript extends Command implements Serializable {

    @Serial
    private static final long serialVersionUID = 1339L;

    private final CommandManager commandManager;

    public ExecuteScript(CommandManager commandManager) {
        super("execute_script", "execute_script file_name : выполнить команды из указанного файла");
        this.commandManager = commandManager;
    }

    public Response execute(Request request) {
        String path = ((String) request.getArgs()).trim();
        StringBuilder stringBuilder = new StringBuilder();

        try {
            FileMode.setFileMode(true);
            ScriptExecuteManager.pushFile(path);

            while (true) {
                String line;
                try {
                    line = ScriptExecuteManager.readfile();
                    if (line == null) break;
                } catch (NoSuchElementException e) {
                    break;
                }

                line = line.trim();
                if (line.isEmpty()) continue;

                String[] cmd = (line + " ").split(" ", 2);
                cmd[1] = cmd[1].trim();

                if (cmd[0].equals("execute_script")) {
                    if (ScriptExecuteManager.IsRepeat(cmd[1])) {
                        stringBuilder.append("Обнаружена рекурсия");
                        continue;
                    }
                }

                Command command = commandManager.getCommands().get(cmd[0]);
                if (command == null) continue;

                Response response;
                switch (cmd[0]) {
                    case "remove_by_id":
                        response = commandManager.execute(new Request(
                                command,
                                Integer.parseInt(cmd[1]),
                                request.getUser()));
                        break;
                    case "add":
                        MusicBand band = new MusicBandsBuilder().create();
                        response = commandManager.execute(new Request(
                                command,
                                band,
                                request.getUser()));
                        break;
                    case "update":
                        MusicBand updateBand = new MusicBandsBuilder().create();
                        response = commandManager.execute(new Request(
                                command,
                                updateBand,
                                Integer.parseInt(cmd[1]),
                                request.getUser()));
                        break;
                    default:
                        response = commandManager.execute(new Request(
                                command,
                                cmd[1],
                                request.getUser()));
                        break;
                }

                stringBuilder.append(response.getResult()).append("\n\n");

                if (cmd[0].equals("execute_script")) {
                    ScriptExecuteManager.popfile();
                }
            }

            FileMode.setFileMode(false);
            return new Response(stringBuilder.toString());

        } catch (FileNotFoundException e) {
            return new Response("Файл не найден");
        } catch (IOException e) {
            return new Response("Ошибка чтения файла");
        } catch (NumberFormatException e) {
            return new Response("Неверный формат числа в аргументе команды");
        }
    }
}