package com.shop.view;

import com.shop.model.*;

import java.util.List;

/**
 * View layer: handles all console output.
 */
public class ConsoleView {

    //Main Menu

    public void showMainMenu() {
        System.out.println("\n========================================");
        System.out.println("           Online Shop");
        System.out.println("========================================");
        System.out.println("  1. Register");
        System.out.println("  2. Log in");
        System.out.println("  3. Browse products");
        System.out.println("  0. Exit");
        System.out.println("========================================");
        System.out.print("Your choice: ");
    }

    //Registration

    public void showRegistrationHeader() {
        System.out.println("\n=========== Registration ===========");
    }

    //Login

    public void showLoginHeader() {
        System.out.println("\n=========== Login ===========");
    }

    //Buyer Menu

    public void showBuyerMenu(Buyer buyer) {
        System.out.println("\n========================================");
        System.out.printf("User: %s%n", buyer.getUsername());
        System.out.printf("Balance: %,.0f Toman%n", buyer.getAccountBalance());
        System.out.println("----------------------------------------");
        System.out.println("  1. View / edit personal info");
        System.out.println("  2. Browse products");
        System.out.println("  3. Shopping cart");
        System.out.println("  4. Checkout");
        System.out.println("  5. Top up account balance");
        System.out.println("  6. Purchase history (invoices)");
        System.out.println("  0. Log out");
        System.out.println("========================================");
        System.out.print("Your choice: ");
    }

    //Products Page

    public void showProductsHeader() {
        System.out.println("\n=========== Products ===========");
    }

    public void showProductsMenu() {
        System.out.println("\n--- Browse Options ---");
        System.out.println("  1. Show all products");
        System.out.println("  2. Search by name");
        System.out.println("  3. Filter");
        System.out.println("  4. Clear filters");
        System.out.println("  5. View product details");
        System.out.println("  6. Add product to cart");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");
    }

    public void showProductList(List<Product> products, int page, int totalPages) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.printf("%n=========== Products (page %d of %d) ===========%n", page, totalPages);
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("  %2d. [%-10s] %-30s | %,8.0f Toman | %s | Rating: %.1f%n",
                    i + 1,
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.isAvailable() ? "In stock" : "Out of stock",
                    p.getAverageRating());
        }
        System.out.println("=================================================");
        if (totalPages > 1) {
            System.out.println("  n. Next page  |  p. Previous page");
        }
    }

    public void showProductDetails(Product product) {
        System.out.println(product);
    }

    //Admin Menu

    public void showAdminWelcome() {
        System.out.println("\n============================================");
        System.out.println("           Admin Control Panel");
        System.out.println("         Type 'help' for commands");
        System.out.println("============================================");
        System.out.print(">>> ");
    }

    public void showAdminHelp() {
        System.out.println("\n=========== Admin Commands ===========");
        System.out.println("Product management:");
        System.out.println("  Add Car <engineCC> <auto:true/false> <manufacturer> <name> <price> <stock>");
        System.out.println("  Add Bicycle <manufacturer> <type:MOUNTAIN/ROAD/CITY/HYBRID> <name> <price> <stock>");
        System.out.println("  Add Flash <name> <price> <stock> <weight> <dims> <capacityGB> <usbVersion>");
        System.out.println("  Add SSD <name> <price> <stock> <weight> <dims> <capacityGB> <readSpeed> <writeSpeed>");
        System.out.println("  Add PC <name> <price> <stock> <weight> <dims> <processor> <ramGB>");
        System.out.println("  Add Pencil <name> <price> <stock> <country> <type:HB/B/F/H/H2>");
        System.out.println("  Add Pen <name> <price> <stock> <country> <color>");
        System.out.println("  Add Notebook <name> <price> <stock> <country> <pages> <paperType>");
        System.out.println("  Add Food <name> <price> <stock> <prodDate:YYYY-MM-DD> <expDate:YYYY-MM-DD>");
        System.out.println("  Edit <id> name=<new name> price=<price> stock=<stock>   (each field optional)");
        System.out.println("      Note: do NOT type the < > characters, they are placeholders only.");
        System.out.println("      Example: Edit 61070414 name=Samsung FIT Plus price=900000 stock=40");
        System.out.println("  Remove <id>");
        System.out.println("  List");
        System.out.println("\nUser management:");
        System.out.println("  Users");
        System.out.println("\nRequest management:");
        System.out.println("  Requests");
        System.out.println("  Approve <requestId>");
        System.out.println("  Reject <requestId>");
        System.out.println("\n  help          - show this help");
        System.out.println("  exit / logout - exit the admin panel");
        System.out.println("========================================");
    }

    //Request List

    public void showRequests(List<Request> requests) {
        if (requests.isEmpty()) {
            System.out.println("There are no requests.");
            return;
        }
        System.out.println("\n=========== Requests ===========");
        requests.forEach(System.out::println);
    }

    //User List

    public void showUserList(List<Buyer> buyers) {
        if (buyers.isEmpty()) {
            System.out.println("No users have registered yet.");
            return;
        }
        System.out.println("\n=========== User List ===========");
        for (int i = 0; i < buyers.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, buyers.get(i));
        }
    }

    // Invoice History
    public void showInvoiceHistory(List<Invoice> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("You have not made any purchases yet.");
            return;
        }
        System.out.println("\n=========== Purchase History ===========");
        invoices.forEach(System.out::println);
    }

    //Profile

    public void showProfile(Buyer buyer) {
        System.out.println("\n=========== Account Information ===========");
        System.out.println("Username: " + buyer.getUsername());
        System.out.println("Email: " + buyer.getEmail());
        System.out.println("Phone: " + buyer.getPhoneNumber());
        System.out.printf("Account balance: %,.0f Toman%n", buyer.getAccountBalance());
    }

    public void showProfileEditMenu() {
        System.out.println("\n--- Edit Information ---");
        System.out.println("  1. Change email");
        System.out.println("  2. Change phone number");
        System.out.println("  3. Change password");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");
    }

    //Cart

    public void showCartMenu() {
        System.out.println("\n--- Shopping Cart ---");
        System.out.println("  1. View cart");
        System.out.println("  2. Remove item from cart");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");
    }

    //General Output

    public void printLine() {
        System.out.println("----------------------------------------");
    }

    public void prompt(String message) {
        System.out.print(message + ": ");
    }

    public void print(String message) {
        System.out.println(message);
    }

    public void printError(String message) {
        System.out.println("Error: " + message);
    }

    public void printSuccess(String message) {
        System.out.println(message);
    }
}
