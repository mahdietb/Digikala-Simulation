package com.shop.model;

import com.shop.util.ValidationUtil;

import java.io.Serializable;


public abstract class UserAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    private String phoneNumber;
    private String password;

    public UserAccount(String username, String email, String phoneNumber, String password) {
        setUsername(username);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassword(password);
    }

    /**
     * Constructor for system-defined accounts whose password does not need
     * to pass the public registration format validation .
     */
    protected UserAccount(String username, String email, String phoneNumber,
                          String password, boolean skipPasswordValidation) {
        setUsername(username);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        if (skipPasswordValidation) {
            setRawPassword(password);
        } else {
            setPassword(password);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        this.email = email.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format: " + phoneNumber);
        }
        this.phoneNumber = phoneNumber.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Invalid password format. Must be at least 8 characters, including an uppercase letter, a lowercase letter, and a digit.");
        }
        this.password = password;
    }


    // Sets the password without format validation.

    protected void setRawPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.password = password;
    }

    public boolean checkPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    @Override
    public String toString() {
        return "Username: " + username +
                " | Email: " + email +
                " | Phone: " + phoneNumber;
    }
}
