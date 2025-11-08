package com.zakrzewski.reservationsystem.utils;

import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGeneratorUtils {
    private static final Random RANDOM = new SecureRandom();

    private static char getRandomChar(char start, char end) {
        return (char) (RANDOM.nextInt((int) end - (int) start + 1) + (int) start);
    }

    public static String generatePINCode(int length) {
        if (length < 0) {
            throw new InvalidInputException("PIN code length cannot be negative");
        }

        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(getRandomChar('0', '9'));
        }
        return sb.toString();
    }

    public static String generateCharCode(int length) {
        if (length < 0) {
            throw new InvalidInputException("CHAR code length cannot be negative");
        }

        final StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(getRandomChar('a', 'z'));
        }
        return sb.toString();
    }
}
