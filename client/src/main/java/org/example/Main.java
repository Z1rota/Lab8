package org.example;

import org.example.exceptions.InvalidDataException;
import org.example.gui.LoginPanel;
import org.example.gui.MainFrame;
import org.example.network.RequestManager;

import javax.swing.*;

/**
 * Главный класс приложения.
 * Запускает выполнение программы, инициализируя менеджер запросов.
 */
public class Main {

    /**
     * Точка входа в программу.
     * Создаёт экземпляр {@link RequestManager} и запускает его выполнение.
     *
     * @param args аргументы командной строки (не используются)
     * @throws InvalidDataException если ввод пользователя некорректен
     * @throws InterruptedException если поток был прерван
     */
    public static void main(String[] args) throws InvalidDataException, InterruptedException {
        RequestManager program = new RequestManager();
        program.ready();
        SwingUtilities.invokeLater(() -> {
            new MainFrame(program).setVisible(true);
        });


    }
}
