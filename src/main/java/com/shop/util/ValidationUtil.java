package com.shop.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation using regex.
 */
public class ValidationUtil {


    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^09[0-9]{9}$");


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");


    private static final Pattern CARD_NUMBER_PATTERN =
            Pattern.compile("^\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}$");


    private static final Pattern CVV2_PATTERN =
            Pattern.compile("^\\d{3,4}$");


    private static final Pattern CARD_PIN_PATTERN =
            Pattern.compile("^\\d{4,6}$");

    private ValidationUtil() {

    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidCardNumber(String cardNumber) {
        return cardNumber != null && CARD_NUMBER_PATTERN.matcher(cardNumber.trim()).matches();
    }

    public static boolean isValidCVV2(String cvv2) {
        return cvv2 != null && CVV2_PATTERN.matcher(cvv2.trim()).matches();
    }

    public static boolean isValidCardPin(String pin) {
        return pin != null && CARD_PIN_PATTERN.matcher(pin.trim()).matches();
    }

    /**
     * Normalizes card number by removing dashes.
     */
    public static String normalizeCardNumber(String cardNumber) {
        return cardNumber == null ? "" : cardNumber.replace("-", "").trim();
    }
}
