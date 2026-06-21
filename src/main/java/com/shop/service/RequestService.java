package com.shop.service;

import com.shop.model.*;

import java.util.List;

/**
 * Service for managing requests to be approved by the admin.
 */
public class RequestService {
    private final Admin admin;

    public RequestService() {
        this.admin = Admin.getInstance();
    }

    public void submitRequest(Request request) {
        if (request == null) return;
        admin.addRequest(request);
        System.out.println("Request #" + request.getId() + " (" + request.getType().getDisplayName() + ") submitted to admin.");
    }

    public List<Request> getPendingRequests() {
        return admin.getPendingRequests();
    }

    public List<Request> getAllRequests() {
        return admin.getRequestList();
    }

    /**
     * Admin approves a request by ID.
     * @return true if found and approved
     */
    public boolean approveRequest(int requestId) {
        Request request = findById(requestId);
        if (request == null) {
            System.out.println("Request with ID " + requestId + " not found.");
            return false;
        }
        if (request.getStatus() != Request.RequestStatus.PENDING) {
            System.out.println("This request has already been processed.");
            return false;
        }

        request.setStatus(Request.RequestStatus.APPROVED);

        // Apply side effects
        switch (request.getType()) {
            case COMMENT_APPROVAL:
                if (request.getComment() != null) {
                    request.getComment().setStatus(Comment.CommentStatus.APPROVED);
                    System.out.println("Comment approved.");
                }
                break;
            case BALANCE_TOP_UP:
                if (request.getRequester() != null) {
                    request.getRequester().addBalance(request.getTopUpAmount());
                    System.out.println("Added " + String.format("%.0f", request.getTopUpAmount()) +
                            " Toman to " + request.getRequesterUsername() + "'s account.");
                }
                break;
            case REGISTRATION:
                if (request.getRequester() != null) {
                    request.getRequester().setRegistrationStatus(Buyer.RegistrationStatus.APPROVED);
                }
                System.out.println("Registration for user \"" + request.getRequesterUsername() + "\" approved. They can now log in.");
                break;
        }
        return true;
    }

    /**
     * Admin rejects a request by ID.
     */
    public boolean rejectRequest(int requestId) {
        Request request = findById(requestId);
        if (request == null) {
            System.out.println("Request with ID " + requestId + " not found.");
            return false;
        }
        if (request.getStatus() != Request.RequestStatus.PENDING) {
            System.out.println("This request has already been processed.");
            return false;
        }
        request.setStatus(Request.RequestStatus.REJECTED);
        if (request.getType() == Request.RequestType.COMMENT_APPROVAL && request.getComment() != null) {
            request.getComment().setStatus(Comment.CommentStatus.REJECTED);
        }
        if (request.getType() == Request.RequestType.REGISTRATION && request.getRequester() != null) {
            request.getRequester().setRegistrationStatus(Buyer.RegistrationStatus.REJECTED);
        }
        System.out.println("Request #" + requestId + " rejected.");
        return true;
    }

    private Request findById(int id) {
        return admin.getRequestList().stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
