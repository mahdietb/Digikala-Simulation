package com.shop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Purchase invoice containing items bought and total amount.
 */
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1000;

    private String invoiceId;
    private LocalDateTime date;
    private double totalAmount;
    private Map<Product, Integer> items;
    private Buyer buyer;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public Invoice(Buyer buyer, Map<Product, Integer> items) {
        this.invoiceId = "INV-" + (counter++);
        this.date = LocalDateTime.now();
        setBuyer(buyer);
        setItems(new HashMap<>(items));
        this.totalAmount = calculateTotal();
    }

    public String getInvoiceId() { return invoiceId; }

    public LocalDateTime getDate() { return date; }

    public double getTotalAmount() { return totalAmount; }

    public Map<Product, Integer> getItems() { return items; }

    public void setItems(Map<Product, Integer> items) {
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Invoice must contain at least one product.");
        this.items = items;
        this.totalAmount = calculateTotal();
    }

    public Buyer getBuyer() { return buyer; }

    public void setBuyer(Buyer buyer) {
        if (buyer == null) throw new IllegalArgumentException("Buyer cannot be null.");
        this.buyer = buyer;
    }

    private double calculateTotal() {
        return items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public boolean containsProduct(Product product) {
        return items.containsKey(product);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Date: ").append(date.format(FORMATTER)).append("\n");
        sb.append("Buyer: ").append(buyer.getUsername()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("Items purchased:\n");
        items.forEach((product, qty) -> {
            sb.append("  - ").append(product.getName())
                    .append(" | Qty: ").append(qty)
                    .append(" | Unit price: ").append(String.format("%.0f", product.getPrice()))
                    .append(" | Subtotal: ").append(String.format("%.0f", product.getPrice() * qty))
                    .append(" Toman\n");
        });
        sb.append("----------------------------------------\n");
        sb.append("Total amount paid: ").append(String.format("%.0f", totalAmount)).append(" Toman\n");
        sb.append("========================================");
        return sb.toString();
    }
}
