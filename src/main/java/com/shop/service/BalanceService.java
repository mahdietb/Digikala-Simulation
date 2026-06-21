package com.shop.service;

import com.shop.model.Buyer;
import com.shop.model.Request;
import com.shop.util.ValidationUtil;

/**
 * Service for handling account balance top-up requests.
 */
public class BalanceService {
    private final RequestService requestService;

    public BalanceService(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * Submits a balance top-up request to the admin.
     *
     * @param buyer      the buyer requesting top-up
     * @param amount     the desired top-up amount
     * @param cardNumber the bank card number
     * @param cardPin    the card PIN
     * @param cvv2       the card CVV2
     */
    public void requestTopUp(Buyer buyer, double amount, String cardNumber, String cardPin, String cvv2) {
        // Validate payment details via regex
        if (!ValidationUtil.isValidCardNumber(cardNumber)) {
            throw new IllegalArgumentException("Invalid card number. It must be 16 digits.");
        }
        if (!ValidationUtil.isValidCardPin(cardPin)) {
            throw new IllegalArgumentException("Invalid card PIN. It must be 4 to 6 digits.");
        }
        if (!ValidationUtil.isValidCVV2(cvv2)) {
            throw new IllegalArgumentException("Invalid CVV2. It must be 3 or 4 digits.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Top-up amount must be positive.");
        }

        String normalizedCard = ValidationUtil.normalizeCardNumber(cardNumber);
        Request request = new Request(buyer, amount, normalizedCard);
        requestService.submitRequest(request);

        System.out.printf("Top-up request for %,.0f Toman submitted and is pending admin approval.%n", amount);
    }
}
