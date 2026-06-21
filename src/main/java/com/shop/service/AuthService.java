package com.shop.service;

import com.shop.model.Admin;
import com.shop.model.Buyer;
import com.shop.model.Request;
import com.shop.repository.UserRepository;

/**
 * Service responsible for authentication and registration.
 */
public class AuthService {
    private final UserRepository userRepository;
    private final RequestService requestService;

    public AuthService(UserRepository userRepository, RequestService requestService) {
        this.userRepository = userRepository;
        this.requestService = requestService;
    }

    /**
     * Registers a new buyer and sends registration request to admin.
     * @return the newly created Buyer if validation passes
     * @throws IllegalArgumentException if any field is invalid or already taken
     */
    public Buyer registerBuyer(String username, String email, String phoneNumber, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username \"" + username + "\" is already taken.");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email \"" + email + "\" is already registered.");
        }


        Buyer buyer = new Buyer(username, email, phoneNumber, password);


        userRepository.addBuyer(buyer);


        Request regRequest = new Request(username);
        regRequest.setRequester(buyer);
        requestService.submitRequest(regRequest);

        System.out.println("Registration submitted. Please wait for admin approval.");
        return buyer;
    }

    /**
     * Authenticates buyer or admin.
     * Buyers whose registration is still PENDING or has been REJECTED
     * cannot log in until the admin approves them.
     * @return the authenticated user (Buyer or Admin), or null if login failed
     */
    public Object login(String username, String password) {
        // Check admin first
        Admin admin = Admin.getInstance();
        if (admin.getUsername().equals(username) && admin.checkPassword(password)) {
            System.out.println("Login successful. Welcome, Admin!");
            return admin;
        }


        Buyer buyer = userRepository.findByUsername(username).orElse(null);
        if (buyer == null || !buyer.checkPassword(password)) {
            return null;
        }

        switch (buyer.getRegistrationStatus()) {
            case PENDING:
                System.out.println("Your registration is still pending admin approval. Please try again later.");
                return null;
            case REJECTED:
                System.out.println("Your registration request was rejected by the admin.");
                return null;
            case APPROVED:
            default:
                System.out.println("Login successful. Welcome, " + buyer.getUsername() + "!");
                return buyer;
        }
    }
}
