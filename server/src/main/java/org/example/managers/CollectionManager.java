package org.example.managers;

import org.example.exceptions.AlreadyEmptyException;
import org.example.exceptions.EmptyCollectionException;
import org.example.exceptions.InvalidDataException;
import org.example.exceptions.NoElementException;
import org.example.mainClasses.Label;
import org.example.mainClasses.MusicBand;
import org.example.network.Response;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер коллекции музыкальных групп.
 * Обеспечивает хранение и управление коллекцией объектов MusicBand.
 * Поддерживает основные CRUD операции, сортировку, фильтрацию и другие операции с коллекцией.
 */
public class CollectionManager {
    /** Коллекция музыкальных групп (стек) */
    private Stack<MusicBand> bands = new Stack<MusicBand>();

    /** Дата инициализации коллекции */
    private LocalDate date;

    /** Менеджер для работы с базой данных */
    private DataBaseManager dataBaseManager = new DataBaseManager();

    /**
     * Конструктор менеджера коллекции.
     * Инициализирует дату создания коллекции текущей датой.
     * @throws SQLException при ошибках работы с базой данных
     */
    public CollectionManager() throws SQLException {
        this.date = LocalDate.parse(LocalDate.now().toString());
    }

    /**
     * Возвращает текущую коллекцию музыкальных групп.
     * @return стек объектов MusicBand
     */
    public Stack<MusicBand> getBands() {
        return this.bands;
    }

    /**
     * Возвращает ID первого элемента коллекции.
     * @return ID первого элемента или 0, если коллекция пуста
     */
    public long getFirstId() {
        for (MusicBand band : this.bands) {
            return band.getId();
        }
        return 0;
    }

    /**
     * Группирует элементы коллекции по лейблу и возвращает статистику.
     * @return строку с группировкой по лейблам и количеством элементов
     * @throws EmptyCollectionException если коллекция пуста
     */
    public String groupByLabel() throws EmptyCollectionException {
        if (bands.isEmpty()) {
            throw new EmptyCollectionException();
        }

        return bands.stream()
                .collect(Collectors.groupingBy(MusicBand::getLabel))
                .entrySet().stream()
                .map(entry -> {
                    String label = entry.getKey().toString();
                    List<MusicBand> musicBands = entry.getValue();
                    return label + ", Count: " + musicBands.size() + "\n" +
                            musicBands.stream()
                                    .map(MusicBand::toString)
                                    .collect(Collectors.joining("\n"));
                })
                .collect(Collectors.joining("\n"));
    }

    /**
     * Устанавливает дату создания коллекции.
     * @param date новая дата создания коллекции
     */
    public void setLocaleDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Возвращает дату создания коллекции.
     * @return дата создания коллекции
     */
    public LocalDate getLocaleDate() {
        return this.date;
    }

    /**
     * Проверяет уникальность ID музыкальной группы в коллекции.
     * @param musicBand группа для проверки
     * @return true если ID уникален, иначе false
     */
    public boolean checkId(MusicBand musicBand) {
        for (MusicBand band : bands) {
            if (musicBand.getId() == band.getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет коллекцию на наличие дубликатов ID.
     * @return true если все ID уникальны, иначе false
     */
    public boolean checkSameId() {
        Set<Long> ids = new HashSet<>();
        for (MusicBand band : bands) {
            if (!ids.add(band.getId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Добавляет коллекцию музыкальных групп в текущую коллекцию.
     * @param collection коллекция для добавления
     * @throws InvalidDataException если данные группы невалидны
     */
    public void addElem(Collection<MusicBand> collection) throws InvalidDataException {
        if (collection == null) return;
        for (MusicBand band : collection) {
            add(band);
        }
    }

    /**
     * Добавляет музыкальную группу в коллекцию.
     * @param band группа для добавления
     * @throws InvalidDataException если данные группы невалидны
     */
    public void add(MusicBand band) throws InvalidDataException {
        if (!band.validate()) {
            throw new InvalidDataException();
        }
        while (!checkId(band)) {
            MusicBand.idcounter++;
            band.setId(MusicBand.idcounter);
        }
        bands.add(band);
    }

    /**
     * Возвращает музыкальную группу по ID.
     * @param id ID искомой группы
     * @return найденная группа или null, если не найдена
     */
    public MusicBand getById(long id) {
        for (MusicBand band : bands) {
            if (band.getId() == id) return band;
        }
        return null;
    }

    /**
     * Удаляет музыкальную группу по ID.
     * @param id ID группы для удаления
     * @throws NoElementException если группа не найдена
     */
    public void removeById(long id) throws NoElementException {
        MusicBand band = getById(id);
        if (band == null) {
            throw new NoElementException();
        }
        bands.remove(band);
    }

    /**
     * Проверяет наличие группы в коллекции.
     * @param band группа для проверки
     * @return true если группа найдена, иначе false
     */
    public boolean isContain(MusicBand band) {
        return band != null && getById(band.getId()) != null;
    }

    /**
     * Очищает коллекцию.
     * @throws AlreadyEmptyException если коллекция уже пуста
     */
    public void clear() throws AlreadyEmptyException {
        if (bands.isEmpty()) {
            throw new AlreadyEmptyException();
        }
        bands.clear();
    }

    /**
     * Возвращает дату создания коллекции.
     * @return дата создания
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Возвращает тип коллекции.
     * @return тип коллекции (Stack)
     */
    public String getTypeOfCollection() {
        return "Stack";
    }

    /**
     * Возвращает размер коллекции.
     * @return количество элементов
     */
    public int size() {
        return bands.size();
    }

    /**
     * Возвращает информацию о коллекции.
     * @return строка с типом, датой создания и размером коллекции
     */
    public String info() {
        return "Тип: " + getTypeOfCollection() + "\n" +
                "Дата Создания: " + getDate() + "\n" +
                "Размер: " + size();
    }

    /**
     * Перемешивает элементы коллекции.
     * @throws EmptyCollectionException если коллекция пуста
     */
    public void shuffle() throws EmptyCollectionException {
        if (bands.isEmpty()) {
            throw new EmptyCollectionException();
        }
        List<MusicBand> newCollection = new ArrayList<>(bands);
        Collections.shuffle(newCollection);
        Stack<MusicBand> shuffled = new Stack<>();
        shuffled.addAll(newCollection);
        this.bands = shuffled;
    }

    /**
     * Обновляет музыкальную группу по ID.
     * @param id ID группы для обновления
     * @param band новые данные группы
     * @throws NoElementException если группа не найдена
     * @throws InvalidDataException если данные невалидны
     */
    public void updateId(long id, MusicBand band) throws NoElementException, InvalidDataException {
        MusicBand oldElem = getById(id);
        if (oldElem == null) {
            throw new NoElementException();
        }
        if (!band.validate()) {
            throw new InvalidDataException();
        }
        bands.remove(oldElem);
        band.setId(id);
        bands.add(band);
    }

    /**
     * Возвращает названия лейблов в обратном алфавитном порядке.
     * @return строку с названиями лейблов
     */
    public String printLabelField() {
        return bands.stream()
                .map(MusicBand::getLabelName)
                .sorted(Comparator.nullsLast(Comparator.reverseOrder()))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Возвращает элементы коллекции в порядке возрастания.
     * @return строковое представление отсортированной коллекции
     * @throws EmptyCollectionException если коллекция пуста
     */
    public String printAscend() throws EmptyCollectionException {
        if (bands.isEmpty()) {
            throw new EmptyCollectionException();
        }
        return bands.stream()
                .sorted(Comparator.naturalOrder())
                .map(MusicBand::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Возвращает все элементы коллекции.
     * @return строковое представление коллекции
     * @throws EmptyCollectionException если коллекция пуста
     */
    public String show() throws EmptyCollectionException {
        if (bands.isEmpty()) {
            throw new EmptyCollectionException();
        }

        return bands.stream()
                .map(MusicBand::toString)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Удаляет первый элемент коллекции.
     * @throws EmptyCollectionException если коллекция пуста
     */
    public void removeFirst() throws EmptyCollectionException {
        if (bands.isEmpty()) {
            throw new EmptyCollectionException();
        }
        bands.remove(bands.lastElement());
    }

    /**
     * Удаляет элемент по индексу.
     * @param num индекс элемента
     * @throws NoElementException если индекс невалиден
     */
    public void removeAt(int num) throws NoElementException {
        if (num < 0 || num >= bands.size()) {
            throw new NoElementException();
        }
        bands.remove(num);
    }

    /**
     * Устанавливает новую коллекцию.
     * @param bands новая коллекция музыкальных групп
     */
    public void setBands(Stack<MusicBand> bands) {
        this.bands = bands;
    }

    /**
     * Загружает коллекцию из базы данных.
     */
    public void loadCollection() {
        this.setBands(dataBaseManager.createCollection());
    }

    /**
     * Удаляет элементы по списку ID.
     * @param ids список ID для удаления
     */
    public void removeElements(List<Long> ids) {
        ids.forEach(id -> bands.remove(getById((id))));
    }
}