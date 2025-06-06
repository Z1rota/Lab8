package org.example.builders;

import org.example.exceptions.InvalidDataException;
import org.example.managers.ManualInputManager;
import org.example.utility.FileMode;
import org.example.utility.Reader;

import java.util.Calendar;

/**
 * Абстрактный класс для построения объектов различных типов.
 * Использует Reader для ввода данных, в зависимости от режима (файловый или ручной ввод).
 */
public abstract class Builder {
    protected final Reader scanner;

    /**
     * Конструктор класса Builder.
     * Инициализирует Reader в зависимости от режима (файловый или ручной ввод).
     */
    public Builder() {
        this.scanner = new ManualInputManager();
    }

    /**
     * Метод для построения целого числа (int).
     *
     * @param name название поля, для которого запрашивается ввод
     * @return целое число, введенное пользователем
     * @throws InvalidDataException если введенные данные некорректны
     */
    protected Integer buildInt(String name) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                Integer bands = Integer.parseInt(input);
                return bands;
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Метод для построения логина пользователя.
     * Гарантирует, что введенная строка не будет пустой.
     *
     * @return строку с логином пользователя
     */
    protected String buildLogin() {
        String input;
        while (true) {
            System.out.println("Введите логин: ");
            input = scanner.nextLine();
            if(input.isBlank()) {
                System.err.println("Строка не может быть пустой");
            } else {
                return input;
            }
        }
    }

    /**
     * Метод для построения пароля пользователя.
     * Гарантирует, что введенная строка не будет пустой.
     *
     * @return строку с паролем пользователя
     */
    protected String buildPassword() {
        String input;
        while (true) {
            System.out.println("Введите пароль: ");
            input = scanner.nextLine();
            if(input.isBlank()) {
                System.err.println("Строка не может быть пустой");
            } else {
                return input;
            }
        }
    }

    /**
     * Метод для построения числа, представляющего месяц (от 0 до 11).
     *
     * @param name название поля, для которого запрашивается ввод
     * @return число, представляющее месяц
     * @throws InvalidDataException если введенные данные некорректны
     */
    protected Integer buildMonth(String name) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                Integer bands = Integer.parseInt(input);
                if (bands < 0 || bands > 11) {
                    System.err.println("Число должно быть больше 0 и меньше 12");
                } else {
                    return bands;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Метод для построения числа, представляющего день (от 1 до 31).
     * Проверяет корректность дня для указанного месяца и года.
     *
     * @param name название поля, для которого запрашивается ввод
     * @param month месяц для проверки корректности дня
     * @param year год для проверки корректности дня
     * @return число, представляющее день
     * @throws InvalidDataException если введенные данные некорректны
     */
    protected Integer buildDate(String name, int month, int year) throws InvalidDataException {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                Integer day = Integer.parseInt(input);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (calendar.get(Calendar.MONTH) != month || calendar.get(Calendar.DAY_OF_MONTH) != day) {
                    System.err.println("Некорректный день для данного месяца");
                } else {
                    return day;
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа Int");
            }
        }
    }

    /**
     * Метод для построения числа с плавающей точкой (float).
     *
     * @param name название поля, для которого запрашивается ввод
     * @return число типа float, введенное пользователем
     * @throws InvalidDataException если число слишком большое
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
     * Метод для построения длинного целого числа (long).
     * В файловом режиме ограничивает максимальное значение 968.
     *
     * @param name название поля, для которого запрашивается ввод
     * @return число типа long, введенное пользователем
     */
    protected Long buildLong(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                if (Long.parseLong(input) > 968 && FileMode.isFileMode) {
                    return (long) 968;
                }else if (Long.parseLong(input) > 968) {
                    System.err.println("Значение должно быть меньше 969!");
                } else {
                    return Long.parseLong(input);
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа long");
            }
        }
    }

    /**
     * Метод для построения длинного целого числа (long), представляющего продажи.
     * Значение должно быть больше 0.
     *
     * @param name название поля, для которого запрашивается ввод
     * @return число типа long, введенное пользователем
     */
    protected Long buildSales(String name) {
        String input;
        while (true) {
            System.out.println("Введите: " + name);
            input = scanner.nextLine();
            try {
                if (Long.parseLong(input) <= 0) {
                    System.err.println("Значение должно быть больше 0!");
                } else {
                    return Long.parseLong(input);
                }
            } catch (NumberFormatException e) {
                System.err.println("Число должно быть типа long");
            }
        }
    }

    /**
     * Метод для построения строки.
     * Возвращает null, если введена пустая строка.
     *
     * @param name название поля, для которого запрашивается ввод
     * @return строку, введенную пользователем, или null если строка пустая
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