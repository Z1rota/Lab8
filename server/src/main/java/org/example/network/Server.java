package org.example.network;

import org.example.managers.CollectionManager;
import org.example.managers.DataBaseManager;
import org.example.managers.RunManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Класс сервера для обработки клиентских запросов.
 * Использует пулы потоков для эффективной обработки соединений.
 */
public class Server {

    private final int port;
    private final String host;
    private final CollectionManager collectionManager;
    private final RunManager runManager;
    private final DataBaseManager dataBaseManager;

    private ServerSocketChannel serverSocket;
    private Request request;
    private Response response;

    private final ExecutorService connectionPool = Executors.newCachedThreadPool();
    private final ExecutorService requestProcessingPool = Executors.newFixedThreadPool(3);

    private static final Logger logger = Logger.getLogger("logger");

    private final BufferedInputStream input = new BufferedInputStream(System.in);
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(input));

    /**
     * Конструктор сервера.
     *
     * @param host адрес сервера
     * @param runManager менеджер выполнения команд
     * @param port порт сервера
     * @param dataBaseManager менеджер работы с БД
     * @param collectionManager менеджер коллекции
     */
    public Server(String host, RunManager runManager, int port,
                  DataBaseManager dataBaseManager, CollectionManager collectionManager) {
        this.host = host;
        this.port = port;
        this.dataBaseManager = dataBaseManager;
        this.collectionManager = collectionManager;
        this.runManager = runManager;
    }

    /**
     * Запускает сервер и начинает обработку клиентских запросов.
     */
    public void run() {
        openServerSocket();
        logger.info("Сервер запущен на порту " + port);

        try {
            while (true) {
                processConsoleCommands();


                SocketChannel clientSocket = serverSocket.accept();
                if (clientSocket != null) {
                    processClientRequest(clientSocket);
                }
            }
        } catch (IOException e) {
            logger.severe("Критическая ошибка сервера: " + e.getMessage());
            shutdownServer();
        }
    }

    /**
     * Обрабатывает команды из консоли сервера.
     * @throws IOException если произошла ошибка ввода/вывода
     */
    private void processConsoleCommands() throws IOException {
        if (consoleReader.ready()) {
            String command = consoleReader.readLine();
            if ("save".equals(command) || "s".equals(command)) {
                collectionManager.loadCollection();
                logger.info("Коллекция успешно сохранена!");
            }
        }
    }

    /**
     * Обрабатывает запрос от клиента.
     * @param clientSocket сокет клиента
     */
    private void processClientRequest(SocketChannel clientSocket) {
        connectionPool.submit(() -> {
            try (ObjectInputStream reader = new ObjectInputStream(clientSocket.socket().getInputStream());
                 ObjectOutputStream writer = new ObjectOutputStream(clientSocket.socket().getOutputStream())) {

                Request request = (Request) reader.readObject();
                logger.info("Получен запрос от " + request.getUser().getLogin() +
                        ": " + request.getCommand().getName());

                Future<Response> responseFuture = requestProcessingPool.submit(
                        () -> runManager.run(request));


                Response response = responseFuture.get();
                sendResponse(response, writer);
                logger.info("Ответ отправлен " + request.getUser().getLogin() +
                        ": " + response.getResult());

            } catch (IOException | ClassNotFoundException |
                     InterruptedException | ExecutionException e) {
                logger.warning("Ошибка обработки запроса: " + e.getMessage());
            } finally {
                closeClientSocket(clientSocket);
            }
        });
    }

    /**
     * Закрывает клиентский сокет.
     * @param socket сокет для закрытия
     */
    private void closeClientSocket(SocketChannel socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.warning("Ошибка закрытия сокета: " + e.getMessage());
        }
    }

    /**
     * Отправляет ответ клиенту.
     * @param response ответ сервера
     * @param writer поток вывода
     */
    private synchronized void sendResponse(Response response, ObjectOutputStream writer) {
        try {
            writer.writeObject(response);
            writer.flush();
        } catch (IOException e) {
            logger.warning("Ошибка отправки ответа: " + e.getMessage());
        }
    }

    /**
     * Открывает серверный сокет.
     */
    private void openServerSocket() {
        try {
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(host, port));
            serverSocket.configureBlocking(false);
            logger.info("Серверный сокет успешно открыт");
        } catch (IOException e) {
            logger.severe("Не удалось открыть серверный сокет: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Корректно завершает работу сервера.
     */
    private void shutdownServer() {
        try {
            connectionPool.shutdown();
            requestProcessingPool.shutdown();
            if (!connectionPool.awaitTermination(5, TimeUnit.SECONDS)) {
                connectionPool.shutdownNow();
            }
            if (!requestProcessingPool.awaitTermination(5, TimeUnit.SECONDS)) {
                requestProcessingPool.shutdownNow();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            consoleReader.close();
        } catch (IOException | InterruptedException e) {
            logger.warning("Ошибка при завершении работы сервера: " + e.getMessage());
        }
    }
}