package com.shop.controller;

import com.shop.model.*;
import com.shop.service.*;
import com.shop.view.ConsoleView;

import java.util.Scanner;

/**
 * Controller for buyer user interface interactions.
 */
public class BuyerController {
    private final ConsoleView view;
    private final ProductController productController;
    private final ShopService shopService;
    private final CommentRatingService commentRatingService;
    private final BalanceService balanceService;
    private final Scanner scanner;

    public BuyerController(ConsoleView view,
                           ProductController productController,
                           ShopService shopService,
                           CommentRatingService commentRatingService,
                           BalanceService balanceService,
                           Scanner scanner) {
        this.view = view;
        this.productController = productController;
        this.shopService = shopService;
        this.commentRatingService = commentRatingService;
        this.balanceService = balanceService;
        this.scanner = scanner;
    }

    /**
     * Buyer session loop. Returns when buyer logs out.
     */
    public void startBuyerSession(Buyer buyer) {
        boolean running = true;
        while (running) {
            view.showBuyerMenu(buyer);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    handleProfile(buyer);
                    break;
                case "2":
                    productController.showProductsPage(buyer);
                    break;
                case "3":
                    handleCart(buyer);
                    break;
                case "4":
                    shopService.checkout(buyer);
                    break;
                case "5":
                    handleTopUp(buyer);
                    break;
                case "6":
                    view.showInvoiceHistory(buyer.getPurchaseHistory());
                    break;
                case "0":
                    view.print("Logged out.");
                    running = false;
                    break;
                default:
                    view.printError("Invalid option.");
            }
        }
    }

    // Profile-part

    private void handleProfile(Buyer buyer) {
        view.showProfile(buyer);
        view.showProfileEditMenu();
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                view.prompt("New email");
                String email = scanner.nextLine().trim();
                try {
                    buyer.setEmail(email);
                    view.printSuccess("Email updated successfully.");
                } catch (IllegalArgumentException e) {
                    view.printError(e.getMessage());
                }
                break;
            case "2":
                view.prompt("New phone number");
                String phone = scanner.nextLine().trim();
                try {
                    buyer.setPhoneNumber(phone);
                    view.printSuccess("Phone number updated successfully.");
                } catch (IllegalArgumentException e) {
                    view.printError(e.getMessage());
                }
                break;
            case "3":
                view.prompt("Current password");
                String oldPass = scanner.nextLine().trim();
                if (!buyer.checkPassword(oldPass)) {
                    view.printError("Current password is incorrect.");
                    break;
                }
                view.prompt("New password");
                String newPass = scanner.nextLine().trim();
                try {
                    buyer.setPassword(newPass);
                    view.printSuccess("Password updated successfully.");
                } catch (IllegalArgumentException e) {
                    view.printError(e.getMessage());
                }
                break;
            case "0":
                break;
            default:
                view.printError("Invalid option.");
        }
    }

    // Cart-part

    private void handleCart(Buyer buyer) {
        view.showCartMenu();
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                shopService.showCart(buyer);
                break;
            case "2":
                shopService.showCart(buyer);
                if (!buyer.getShoppingCart().isEmpty()) {
                    view.prompt("ID of the product you want to remove");
                    String pid = scanner.nextLine().trim();
                    try {
                        shopService.removeFromCart(buyer, pid);
                    } catch (IllegalArgumentException e) {
                        view.printError(e.getMessage());
                    }
                }
                break;
            case "0":
                break;
            default:
                view.printError("Invalid option.");
        }
    }

    // Top-up the cart - part

    private void handleTopUp(Buyer buyer) {
        view.print("\n=========== Top Up Account Balance ===========");
        view.prompt("Amount (Toman)");
        double amount;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            view.printError("Invalid amount.");
            return;
        }

        view.prompt("Card number (16 digits)");
        String cardNumber = scanner.nextLine().trim();
        view.prompt("Card PIN");
        String pin = scanner.nextLine().trim();
        view.prompt("CVV2");
        String cvv2 = scanner.nextLine().trim();

        try {
            balanceService.requestTopUp(buyer, amount, cardNumber, pin, cvv2);
        } catch (IllegalArgumentException e) {
            view.printError(e.getMessage());
        }
    }
}
