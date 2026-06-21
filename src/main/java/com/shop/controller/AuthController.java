package com.shop.controller;

import com.shop.model.Admin;
import com.shop.model.Buyer;
import com.shop.service.AuthService;
import com.shop.view.ConsoleView;

import java.util.Scanner;

/**
 * Controller for login and registration flows.
 */
public class AuthController {
    private final ConsoleView view;
    private final AuthService authService;
    private final Scanner scanner;

    public AuthController(ConsoleView view, AuthService authService, Scanner scanner) {
        this.view = view;
        this.authService = authService;
        this.scanner = scanner;
    }

    /**
     * Handles registration form input.
     * @return newly registered Buyer, or null on failure
     */
    public Buyer handleRegistration() {
        view.showRegistrationHeader();
        try {
            view.prompt("Username");
            String username = scanner.nextLine().trim();
            view.prompt("Email");
            String email = scanner.nextLine().trim();
            view.prompt("Phone number (e.g. 09123456789)");
            String phone = scanner.nextLine().trim();
            view.prompt("Password (min 8 characters, including uppercase, lowercase, and a digit)");
            String password = scanner.nextLine().trim();

            return authService.registerBuyer(username, email, phone, password);

        } catch (IllegalArgumentException e) {
            view.printError(e.getMessage());
            return null;
        }
    }

    /**
     * Handles login form input.
     * Note: if login fails because the buyer's registration is still pending
     * or was rejected, AuthService already prints a specific explanation,
     * so here I only print the generic credentials error when that's not the case.
     * @return authenticated user object Buyer or Admin, or null on failure
     */
    public Object handleLogin() {
        view.showLoginHeader();
        view.prompt("Username");
        String username = scanner.nextLine().trim();
        view.prompt("Password");
        String password = scanner.nextLine().trim();

        Object user = authService.login(username, password);
        if (user == null) {
            view.printError("Invalid username or password, or your account is not yet approved.");
        }
        return user;
    }
}
