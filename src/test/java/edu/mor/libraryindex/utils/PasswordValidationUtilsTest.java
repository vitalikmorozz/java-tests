package edu.mor.libraryindex.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordValidationUtilsTest {
    @Test
    public void testIsValidPassword_TooShortPassword() {
        assertFalse(ValidationUtils.isValidPassword("abc123!"));
    }

    @Test
    public void testIsValidPassword_OnlyLowerCase() {
        assertFalse(ValidationUtils.isValidPassword("password"));
    }

    @Test
    public void testIsValidPassword_OnlyUpperCase() {
        assertFalse(ValidationUtils.isValidPassword("PASSWORD"));
    }

    @Test
    public void testIsValidPassword_OnlyDigits() {
        assertFalse(ValidationUtils.isValidPassword("12345678"));
    }

    @Test
    public void testIsValidPassword_OnlySpecialChars() {
        assertFalse(ValidationUtils.isValidPassword("!@#$%^&*"));
    }

    @Test
    public void testIsValidPassword_NoUpperCase() {
        assertFalse(ValidationUtils.isValidPassword("password1!"));
    }

    @Test
    public void testIsValidPassword_NoLowerCase() {
        assertFalse(ValidationUtils.isValidPassword("PASSWORD1!"));
    }

    @Test
    public void testIsValidPassword_NoDigit() {
        assertFalse(ValidationUtils.isValidPassword("Password!"));
    }

    @Test
    public void testIsValidPassword_NoSpecialChar() {
        assertFalse(ValidationUtils.isValidPassword("Password1"));
    }

    @Test
    public void testIsValidPassword_ValidPassword1() {
        assertTrue(ValidationUtils.isValidPassword("Password1!"));
    }

    @Test
    public void testIsValidPassword_ValidPassword2() {
        assertTrue(ValidationUtils.isValidPassword("paSsw0rd!"));
    }

    @Test
    public void testIsValidPassword_ValidPassword3() {
        assertTrue(ValidationUtils.isValidPassword("Passw0rd!@"));
    }

    @Test
    public void testIsValidPassword_ValidPassword4() {
        assertTrue(ValidationUtils.isValidPassword("p@ssW0rd!"));
    }

    @Test
    public void testIsValidPassword_ValidPassword5() {
        assertTrue(ValidationUtils.isValidPassword("1Password!"));
    }

    @Test
    public void testIsValidPassword_ValidPassword6() {
        assertTrue(ValidationUtils.isValidPassword("Pa$$w0rd"));
    }

    @Test
    public void testIsValidPassword_ValidPassword7() {
        assertTrue(ValidationUtils.isValidPassword("pAssw0rd^"));
    }

    @Test
    public void testIsValidPassword_ValidPassword8() {
        assertTrue(ValidationUtils.isValidPassword("P@s5word!"));
    }

    @Test
    public void testIsValidPassword_ValidPassword9() {
        assertTrue(ValidationUtils.isValidPassword("pa5$W0rd"));
    }

    @Test
    public void testIsValidPassword_ValidPassword10() {
        assertTrue(ValidationUtils.isValidPassword("mySecurePassword12!"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "paSsw0rd!", "Passw0rd!@", "p@ssW0rd!", "1Password!", "Pa$$w0rd", "pAssw0rd^", "P@s5word!", "pa5$W0rd", "mySecurePassword12!"})
    public void testIsValidPassword_ValidPasswords(String password) {
        assertTrue(ValidationUtils.isValidPassword(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc123!", "password", "PASSWORD", "12345678", "!@#$%^&*", "password1!", "PASSWORD1!", "Password!", "Password1", "p@ssword"})
    public void testIsValidPassword_InvalidPasswords(String password) {
        assertFalse(ValidationUtils.isValidPassword(password));
    }
}
