package edu.mor.libraryindex.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidationUtilsTest {
    @Test
    public void testIsValidEmail_Null() {
        assertFalse(ValidationUtils.isValidEmail(null));
    }

    @Test
    public void testIsValidEmail_NoAtSymbol() {
        assertFalse(ValidationUtils.isValidEmail("userdomain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidTLD() {
        assertFalse(ValidationUtils.isValidEmail("user@domain.c"));
    }

    @Test
    public void testIsValidEmail_InvalidUsername() {
        assertFalse(ValidationUtils.isValidEmail("user@-domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidDomain() {
        assertFalse(ValidationUtils.isValidEmail("user@domain-.com"));
    }

    @Test
    public void testIsValidEmail_InvalidUsernameLength() {
        assertFalse(ValidationUtils.isValidEmail("1234567890123456789012345678901234567890123456789012345678901234@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidDomainLength() {
        assertFalse(ValidationUtils.isValidEmail("user@123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789.com"));
    }

    @Test
    public void testIsValidEmail_InvalidMultipleAtSymbols() {
        assertFalse(ValidationUtils.isValidEmail("user@domain.com@"));
    }

    @Test
    public void testIsValidEmail_InvalidMultipleDots() {
        assertFalse(ValidationUtils.isValidEmail("user..name@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidSpecialCharacters() {
        assertFalse(ValidationUtils.isValidEmail("user!name@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidSpecialCharactersInDomain() {
        assertFalse(ValidationUtils.isValidEmail("user@dom$ain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidSpecialCharactersInTLD() {
        assertFalse(ValidationUtils.isValidEmail("user@domain.c@m"));
    }

    @Test
    public void testIsValidEmail_InvalidSpaces() {
        assertFalse(ValidationUtils.isValidEmail("user name@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidNewlines() {
        assertFalse(ValidationUtils.isValidEmail("user\nname@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidTabs() {
        assertFalse(ValidationUtils.isValidEmail("user\tname@domain.com"));
    }

    @Test
    public void testIsValidEmail_ValidUpperCase() {
        assertTrue(ValidationUtils.isValidEmail("USER@DOMAIN.COM"));
    }

    @Test
    public void testIsValidEmail_ValidMixedCase() {
        assertTrue(ValidationUtils.isValidEmail("UsEr@DoMaIn.CoM"));
    }

    @Test
    public void testIsValidEmail_ValidEmailWithPlus() {
        assertTrue(ValidationUtils.isValidEmail("user+123@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidEmailWithMultipleDots() {
        assertFalse(ValidationUtils.isValidEmail("user..name@domain.com"));
    }

    @Test
    public void testIsValidEmail_InvalidEmailWithSpecialCharacters() {
        assertFalse(ValidationUtils.isValidEmail("user@domain&.com"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user@domain.com",
        "user123@domain.com",
        "user.name@domain.com",
        "user-name@domain.com",
        "user_name@domain.com",
        "user@subdomain.domain.com",
        "user@123.123.123.123",
        "user@domain.co.us",
        "user@domain.io",
        "user@domain.coffee",
        "user@domain.email",
        "user@domain.info",
        "user@domain.net",
        "user@domain.org",
        "USER@DOMAIN.COM",
        "UsEr@DoMaIn.CoM",
        "user+123@domain.com"
    })
    public void testIsValidEmail_ValidEmails(String email) {
        assertTrue(ValidationUtils.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "user",
        "user@",
        "@domain.com",
        "user@domain",
        "user@domain.",
        "user@domain.c",
        "user@domain..com",
        "user@domain&.com",
        "user@123.456.789.000",
        "user@-domain.com",
        "user@domain-.com",
        "user@domain.com ",
        " user@domain.com",
        "user@ domain.com",
        "user@domain.com\t",
        "user@domain.comm"
    })
    public void testIsValidEmail_InvalidEmails(String email) {
        assertFalse(ValidationUtils.isValidEmail(email));
    }
}