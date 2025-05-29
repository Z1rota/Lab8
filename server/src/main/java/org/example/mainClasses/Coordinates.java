package org.example.mainClasses;

import java.io.Serial;
import java.io.Serializable;

/**
 * Класс, представляющий координаты с двумя значениями: x и y.
 */
public class Coordinates implements Serializable {

    @Serial
    private static final long serialVersionUID = 8800L;
    /**
     * Координата x. Поле не может быть null.
     */
    private Float x;

    /**
     * Координата y. Максимальное значение поля: 968.
     */
    private long y;

    /**
     * Конструктор для создания объекта Coordinates.
     *
     * @param x координата x
     * @param y координата y
     */
    public Coordinates(Float x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат в формате "x;y".
     *
     * @return строковое представление координат
     */
    @Override
    public String toString() {
        return x + ";" + y;
    }

    /**
     * Возвращает значение координаты x.
     *
     * @return значение координаты x
     */
    public float getX() {
        return x;
    }

    /**
     * Возвращает значение координаты y.
     *
     * @return значение координаты y
     */
    public long getY() {
        return y;
    }
}