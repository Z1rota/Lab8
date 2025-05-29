package org.example.managers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Менеджер для хеширования паролей.
 * Обеспечивает безопасное хеширование паролей с использованием алгоритма SHA-1.
 * Используется для защиты паролей пользователей при хранении в базе данных.
 */
public class PasswordManager {

    /**
     * Хеширует переданный пароль с использованием алгоритма SHA-1.
     *
     * @param password пароль для хеширования
     * @return хеш-строка в шестнадцатеричном представлении
     * @throws RuntimeException если возникли проблемы с алгоритмом хеширования или кодировкой
     */
    public String hashPassword(String password) {
        try {

            MessageDigest mg = MessageDigest.getInstance("SHA-1");

            // Вычисляем хеш от пароля в кодировке UTF-8
            byte[] digest = mg.digest(password.getBytes("UTF-8"));

            // Преобразуем байтовый массив в шестнадцатеричную строку
            BigInteger bg = new BigInteger(1, digest);
            return bg.toString(16);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // Преобразуем checked exceptions в unchecked для удобства использования
            throw new RuntimeException("Ошибка при хешировании пароля", e);
        }
    }
}