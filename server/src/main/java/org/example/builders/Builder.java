package org.example.builders;

import org.example.exceptions.InvalidDataException;
import org.example.managers.ManualInputManager;
import org.example.managers.ScriptExecuteManager;
import org.example.utility.FileMode;
import org.example.utility.Reader;

import java.util.Calendar;

/**
 * Абстрактный класс, служащий базой для всех строителей объектов.
 * В зависимости от режима работы приложения (ручной ввод или скриптовый),
 * инициализирует соответствующий источник данных для считывания пользовательского ввода.
 */
public abstract class Builder {
    /** Источник ввода данных (может быть ручной ввод или чтение из скрипта) */
    protected final Reader scanner;

    /**
     * Конструктор класса.
     * В зависимости от режима (скриптовый или ручной) выбирает соответствующий источник ввода.
     */
    public Builder() {
        this.scanner = (FileMode.isFileMode) ? new ScriptExecuteManager() : new ManualInputManager();
    }

    /**
     * Считывает целое число типа int.
     *
     * @param name название параметра, отображаемое пользователю
     * @return введённое пользователем значение типа Integer
     * @throws InvalidDataException если данные некорректны
     */
    protected Integer buildInt(String name) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Считывает номер месяца (от 0 до 11).
     *
     * @param name название параметра
     * @return число от 0 до 11, соответствующее месяцу
     * @throws InvalidDataException если введены некорректные данные
     */
    protected Integer buildMonth(String name) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                int month = Integer.parseInt(input);
                if (month < 0 || month > 11) {
                    System.err.println("Число должно быть от 0 до 11 включительно");
                } else {
                    return month;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Считывает корректный день месяца, учитывая конкретный месяц и год.
     *
     * @param name название параметра
     * @param month номер месяца
     * @param year год
     * @return день месяца
     * @throws InvalidDataException если введено некорректное значение
     */
    protected Integer buildDate(String name, int month, int year) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                int day = Integer.parseInt(input);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                // Проверка, не скорректировался ли день календарем (например, 30 февраля -> 2 марта)
                if (calendar.get(Calendar.MONTH) != month || calendar.get(Calendar.DAY_OF_MONTH) != day) {
                    System.err.println("Некорректный день для заданного месяца и года");
                } else {
                    return day;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Считывает значение типа float.
     *
     * @param name название параметра
     * @return значение float
     */
    protected Float buildFloat(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine() + "f";
            try {
                float value = Float.parseFloat(input);
                if (value == Float.POSITIVE_INFINITY || value == Float.NEGATIVE_INFINITY) {
                    throw new InvalidDataException();
                }
                return value;
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа float");
            } catch (InvalidDataException e) {
                System.err.println("Число слишком большое");
            }
        }
    }

    /**
     * Считывает значение типа long.
     * Если приложение работает в режиме скрипта — ограничивает максимальное значение 968.
     *
     * @param name название параметра
     * @return значение long
     */
    protected Long buildLong(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                long value = Long.parseLong(input);
                if (value > 968 && FileMode.isFileMode) {
                    return 968L;
                } else if (value > 968) {
                    System.err.println("Значение должно быть не больше 968");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа long");
            }
        }
    }

    /**
     * Считывает значение продаж (long).
     * Значение должно быть строго больше 0.
     *
     * @param name название параметра
     * @return значение типа long
     */
    protected Long buildSales(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                long value = Long.parseLong(input);
                if (value <= 0) {
                    System.err.println("Значение должно быть больше 0!");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа long");
            }
        }
    }

    /**
     * Считывает строку.
     * Пустая строка будет интерпретироваться как null.
     *
     * @param name название параметра
     * @return строка или null, если введена пустая строка
     */
    protected String buildString(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            if (input.isBlank()) {
                return null;
            } else {
                return input;
            }
        }
    }
}
