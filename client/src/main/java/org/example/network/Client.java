package org.example.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

/**
 * Клиент для взаимодействия с сервером по сети.
 * Обеспечивает установку соединения, отправку запросов и получение ответов.
 * Поддерживает механизм повторного подключения при разрыве соединения.
 */
public class Client {

    /** Порт сервера */
    private int port;

    /** Хост сервера */
    private String host;

    /** Таймаут соединения в миллисекундах */
    private int timeout;

    /** Канал сокета для соединения */
    private SocketChannel socket;

    /** Поток вывода для отправки объектов */
    private ObjectOutputStream writer;

    /** Поток ввода для получения объектов */
    private ObjectInputStream reader;

    /** Текущее количество попыток переподключения */
    private int reconnectionAttempts;

    /** Максимальное количество попыток переподключения */
    private int maxReconnectionAttempts;

    /**
     * Конструктор клиента.
     *
     * @param host адрес сервера
     * @param port порт сервера
     * @param timeout таймаут соединения в миллисекундах
     * @param maxReconnectionAttempts максимальное количество попыток переподключения
     */
    public Client(String host, int port, int timeout, int maxReconnectionAttempts) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
    }

    /**
     * Устанавливает соединение с сервером.
     * Создает сокетное соединение и инициализирует потоки ввода/вывода.
     */
    public void connect() {
        try {
            socket = SocketChannel.open();
            socket.connect(new InetSocketAddress(host,port));
            writer = new ObjectOutputStream(socket.socket().getOutputStream());
            reader = new ObjectInputStream(socket.socket().getInputStream());
        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу");
        }
    }

    /**
     * Закрывает соединение с сервером.
     * Освобождает ресурсы сокета и потоков ввода/вывода.
     */
    public void disconnect() {
        try {
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.err.println("Не подключено к серверу");
        }
    }

    /**
     * Отправляет запрос серверу и получает ответ.
     * Поддерживает механизм повторного подключения при ошибках.
     *
     * @param request запрос для отправки
     * @return ответ от сервера или null при невозможности установить соединение
     * @throws InterruptedException если поток был прерван во время ожидания
     */
    public Response sendRequest(Request request) throws InterruptedException {
        this.connect();
        for (int reconnectionAttempts = 0; reconnectionAttempts < maxReconnectionAttempts; reconnectionAttempts++) {
            try {
                if (Objects.isNull(writer) || Objects.isNull(reader)) throw new IOException();
                if (request.getCommand() == null & !(request.getArgs() == ("exit")))
                    System.err.println("Запрос пуст, введите команду");

                writer.writeObject(request);
                writer.flush();
                Response response = (Response) reader.readObject();
                this.disconnect();
                return response;

            } catch (IOException e) {
                if (reconnectionAttempts >= maxReconnectionAttempts) {
                    break;
                }
                System.err.println("Рекконект через: " + timeout/1000 + " секунд");
                Thread.sleep(timeout);
                this.connect();

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                System.out.println("Не получилось подключиться, завершение работы...");
                System.exit(0);
            }
        }
        return null;
    }
}