package org.example.builders;

import org.example.exceptions.InvalidDataException;
import org.example.mainClasses.Coordinates;

/**
 * Класс для построения объектов типа Coordinates.
 * Наследует функциональность от абстрактного класса Builder и реализует
 * метод создания координат с запросом значений полей у пользователя.
 */
public class CoordinatesBuilder extends Builder {

    /**
     * Создает новый объект типа Coordinates, последовательно запрашивая
     * у пользователя значения для координат x (типа float) и y (типа long).
     *
     * @return новый объект Coordinates с заданными значениями
     * @throws InvalidDataException если введенные данные для координат некорректны:
     *                              - для x: не является числом float или слишком большое
     *                              - для y: не является числом long или выходит за допустимые пределы
     * @see Builder#buildFloat(String) метод для построения значения x
     * @see Builder#buildLong(String) метод для построения значения y
     */
    public Coordinates create() throws InvalidDataException {
        return new Coordinates(buildFloat("значение x"), buildLong("значение y"));
    }
}