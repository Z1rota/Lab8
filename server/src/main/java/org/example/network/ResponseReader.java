package org.example.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SocketChannel;

/**
 * Класс для чтения ответов от сервера в отдельном потоке.
 * Реализует интерфейс Runnable для выполнения в фоновом потоке.
 */
public class ResponseReader implements Runnable {

    /**
     * Поток ввода для чтения сериализованных объектов Response
     */
    private ObjectInputStream in;

    /**
     * Канал сокета для соединения с сервером
     */
    private SocketChannel socket;

    /**
     * Конструктор ResponseReader.
     *
     * @param in    поток ввода для чтения ответов
     * @param socket канал сокета для соединения
     */
    public ResponseReader(ObjectInputStream in, SocketChannel socket) {
        this.in = in;
        this.socket = socket;
    }

    /**
     * Основной метод выполнения потока.
     * Читает ответ от сервера из потока ввода.
     * В текущей реализации просто получает ответ без обработки.
     */
    @Override
    public void run() {
        try {
            if (in == null) {
                in = new ObjectInputStream(socket.socket().getInputStream());
            }

            Response response = (Response) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка чтения ответа от сервера", e);
        }
    }
}