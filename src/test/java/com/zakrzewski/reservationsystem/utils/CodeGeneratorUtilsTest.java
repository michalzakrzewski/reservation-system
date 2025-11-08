package com.zakrzewski.reservationsystem.utils;

import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGeneratorUtilsTest {
    private static final String PIN_REGEX = "^[0-9]+$";
    private static final String CHAR_CODE_REGEX = "^[a-z]+$";

    @Test
    void testGeneratePINCode_shouldHaveCorrectLength() {
        //given
        final int expectedLength = 6;

        //when
        final String pin = CodeGeneratorUtils.generatePINCode(expectedLength);

        //then
        assertEquals(expectedLength, pin.length(), "The generated PIN should have exactly " + expectedLength + " characters");
    }

    @Test
    void testGeneratePINCode_shouldContainOnlyDigits() {
        //given
        final int length = 8;

        //when
        final String pin = CodeGeneratorUtils.generatePINCode(length);

        //then
        assertTrue(pin.matches(PIN_REGEX), "The PIN Code should contain only numbers (0-9)");
    }

    @Test
    void testGeneratePINCode_shouldBeRandom() {
        //given
        final int length = 4;
        final int attempts = 1000;
        final Set<String> generatedCodes = new HashSet<>();

        //when
        for (int i = 0; i < attempts; i++) {
            generatedCodes.add(CodeGeneratorUtils.generatePINCode(length));
        }

        //then
        assertTrue(generatedCodes.size() > attempts * 0.9, "A sufficient number of unique codes should be generated to confirm randomness");
    }

    @Test
    void testGenerateCharCode_shouldHaveCorrectLength() {
        //given
        final int expectedLength = 12;

        //when
        final String code = CodeGeneratorUtils.generateCharCode(expectedLength);

        //then
        assertEquals(expectedLength, code.length(), "The generated character code should be exactly " + expectedLength + " characters");
    }

    @Test
    void testGenerateCharCode_shouldContainOnlyLowerLetters() {
        //given
        final int length = 10;

        //when
        final String code = CodeGeneratorUtils.generateCharCode(length);

        //then
        assertTrue(code.matches(CHAR_CODE_REGEX), "The character code should contain only lowercase letters (a-z)");
    }

    @Test
    void testGenerateCharCode_shouldBeRandom() {
        //given
        final int length = 5;
        final int attempts = 1000;
        final Set<String> generatedCodes = new HashSet<>();

        //when
        for (int i = 0; i < attempts; i++) {
            generatedCodes.add(CodeGeneratorUtils.generateCharCode(length));
        }

        //then
        assertTrue(generatedCodes.size() > attempts * 0.9, "A sufficient number of unique character codes should be generated");
    }


    @Test
    void testGenerateCode_shouldHandleZeroLength() {
        //then
        assertEquals("", CodeGeneratorUtils.generatePINCode(0), "For length 0 it should return an empty String");
        assertEquals("", CodeGeneratorUtils.generateCharCode(0), "For length 0 it should return an empty String");
    }

    @Test
    void testGenerateCode_shouldThrowExceptionForNegativeLength() {
        //then
        assertThrows(InvalidInputException.class, () -> {
            CodeGeneratorUtils.generatePINCode(-1);
        });
    }
}