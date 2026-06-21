package com.shop.service;

import com.shop.model.*;
import com.shop.repository.ProductRepository;

import java.util.Map;

/**
 * Service for shopping cart and purchase operations.
 */
public class ShopService {
    private final ProductRepository productRepository;

    public ShopService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Adds a product to the buyer's shopping cart.
     */
    public void addToCart(Buyer buyer, String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found."));

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product \"" + product.getName() + "\" is out of stock.");
        }

        int currentInCart = buyer.getShoppingCart().getOrDefault(product, 0);
        if (currentInCart + quantity > product.getStock()) {
            throw new IllegalStateException("Not enough stock. Available: " + product.getStock() +
                    " | Already in cart: " + currentInCart);
        }

        buyer.addToCart(product, quantity);
        System.out.println("Added " + quantity + " x \"" + product.getName() + "\" to cart.");
    }

    /**
     * Removes a product from the cart.
     */
    public void removeFromCart(Buyer buyer, String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product with ID " + productId + " not found."));
        buyer.removeFromCart(product);
        System.out.println("\"" + product.getName() + "\" removed from cart.");
    }

    /**
     * Displays the buyer's current shopping cart.
     */
    public void showCart(Buyer buyer) {
        Map<Product, Integer> cart = buyer.getShoppingCart();
        if (cart.isEmpty()) {
            System.out.println("Your shopping cart is empty.");
            return;
        }
        System.out.println("============= Shopping Cart =============");
        cart.forEach((product, qty) -> {
            System.out.printf("  - %-30s | Qty: %d | Unit price: %,.0f | Subtotal: %,.0f Toman%n",
                    product.getName(), qty, product.getPrice(), product.getPrice() * qty);
        });
        System.out.printf("-------------------------------------------\n");
        System.out.printf("Total: %,.0f Toman%n", buyer.getCartTotal());
        System.out.printf("Account balance: %,.0f Toman%n", buyer.getAccountBalance());
        System.out.println("===========================================");
    }

    /**
     * Finalizes the cart and processes payment.
     * @return the generated Invoice, or null on failure
     */
    public Invoice checkout(Buyer buyer) {
        Map<Product, Integer> cart = buyer.getShoppingCart();
        if (cart.isEmpty()) {
            System.out.println("Your shopping cart is empty.");
            return null;
        }

        // Verify every item in the cart still exists in the catalog so if the admin remove that item we prevent
        //the buyer from shopping the item which does not exist anymore

        java.util.List<Product> removedItems = new java.util.ArrayList<>();
        for (Product product : cart.keySet()) {
            if (productRepository.findById(product.getId()).isEmpty()) {
                removedItems.add(product);
            }
        }
        if (!removedItems.isEmpty()) {
            for (Product product : removedItems) {
                buyer.removeFromCart(product);
                System.out.println("\"" + product.getName() +
                        "\" is no longer available and was removed from your cart.");
            }
            System.out.println("Please review your cart and try checkout again.");
            return null;
        }

        double total = buyer.getCartTotal();

        if (buyer.getAccountBalance() < total) {
            System.out.printf("Insufficient balance. Total: %,.0f | Balance: %,.0f Toman%n",
                    total, buyer.getAccountBalance());
            return null;
        }

        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (product.getStock() < quantity) {
                System.out.println("Not enough stock for \"" + product.getName() + "\".");
                return null;
            }
        }

        buyer.deductBalance(total);

        cart.forEach((product, qty) -> product.decreaseStock(qty));

        Invoice invoice = new Invoice(buyer, cart);
        buyer.addInvoice(invoice);
        buyer.clearCart();

        System.out.println("Checkout completed successfully!");
        System.out.println(invoice);
        return invoice;
    }
}
