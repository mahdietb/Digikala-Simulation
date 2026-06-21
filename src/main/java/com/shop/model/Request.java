package com.shop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  it handles registration, comment approval, account top-up.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum RequestType {
        REGISTRATION("Registration"),
        COMMENT_APPROVAL("Comment Approval"),
        BALANCE_TOP_UP("Balance Top-Up");

        private final String displayName;
        RequestType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    public enum RequestStatus {
        PENDING("Pending approval"),
        APPROVED("Approved"),
        REJECTED("Rejected");

        private final String displayName;
        RequestStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private static int counter = 1;

    private int id;
    private RequestType type;
    private RequestStatus status;
    private Buyer requester;      // can be null for registration
    private String requesterUsername; // used when buyer not yet registered
    private Comment comment;      // non-null for COMMENT_APPROVAL
    private double topUpAmount;   // non-zero for BALANCE_TOP_UP
    private String cardNumber;    // for top-up requests
    private LocalDateTime createdAt;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

    // Constructor for registration requests
    public Request(String requesterUsername) {
        this.id = counter++;
        this.type = RequestType.REGISTRATION;
        this.status = RequestStatus.PENDING;
        this.requesterUsername = requesterUsername;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for comment approval
    public Request(Buyer requester, Comment comment) {
        this.id = counter++;
        this.type = RequestType.COMMENT_APPROVAL;
        this.status = RequestStatus.PENDING;
        this.requester = requester;
        this.requesterUsername = requester.getUsername();
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for balance top-up
    public Request(Buyer requester, double topUpAmount, String cardNumber) {
        this.id = counter++;
        this.type = RequestType.BALANCE_TOP_UP;
        this.status = RequestStatus.PENDING;
        this.requester = requester;
        this.requesterUsername = requester.getUsername();
        this.topUpAmount = topUpAmount;
        this.cardNumber = cardNumber;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }

    public RequestType getType() { return type; }

    public RequestStatus getStatus() { return status; }

    public void setStatus(RequestStatus status) {
        if (status == null) throw new IllegalArgumentException("Request status cannot be null.");
        this.status = status;
    }

    public Buyer getRequester() { return requester; }

    public void setRequester(Buyer requester) { this.requester = requester; }

    public String getRequesterUsername() { return requesterUsername; }

    public Comment getComment() { return comment; }

    public double getTopUpAmount() { return topUpAmount; }

    public String getCardNumber() { return cardNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------\n");
        sb.append("Request #").append(id).append("\n");
        sb.append("Type: ").append(type.getDisplayName()).append("\n");
        sb.append("Status: ").append(status.getDisplayName()).append("\n");
        sb.append("User: ").append(requesterUsername).append("\n");
        sb.append("Date: ").append(createdAt.format(FORMATTER)).append("\n");
        if (type == RequestType.BALANCE_TOP_UP) {
            sb.append("Top-up amount: ").append(String.format("%.0f", topUpAmount)).append(" Toman\n");
            if (cardNumber != null) {
                sb.append("Card number: ").append(maskCard(cardNumber)).append("\n");
            }
        }
        if (type == RequestType.COMMENT_APPROVAL && comment != null) {
            sb.append("Comment: ").append(comment.getText()).append("\n");
            sb.append("Product: ").append(comment.getProduct().getName()).append("\n");
        }
        sb.append("----------------------------------------");
        return sb.toString();
    }

    private String maskCard(String card) {
        if (card.length() < 4) return card;
        return "*".repeat(card.length() - 4) + card.substring(card.length() - 4);
    }
}
