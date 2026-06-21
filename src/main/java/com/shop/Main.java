package com.shop;

import com.shop.controller.*;
import com.shop.model.Admin;
import com.shop.model.Buyer;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import com.shop.service.*;
import com.shop.view.ConsoleView;

import java.util.Scanner;

/**
 * Main application entry point.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Repositories
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();

        // Services
        RequestService requestService = new RequestService();
        AuthService authService = new AuthService(userRepository, requestService);
        ProductService productService = new ProductService(productRepository);
        ShopService shopService = new ShopService(productRepository);
        CommentRatingService commentRatingService = new CommentRatingService(requestService);
        BalanceService balanceService = new BalanceService(requestService);
        PersistenceService persistenceService = new PersistenceService();

        // View
        ConsoleView view = new ConsoleView();

        // Controllers
        AuthController authController = new AuthController(view, authService, scanner);
        ProductController productController = new ProductController(
                view, productService, shopService, commentRatingService, scanner);
        AdminController adminController = new AdminController(
                view, productService, requestService, userRepository, scanner);
        BuyerController buyerController = new BuyerController(
                view, productController, shopService, commentRatingService, balanceService, scanner);

        // Load saved data, or seed fresh demo data

        boolean loaded = persistenceService.loadState(userRepository, productRepository);
        if (!loaded) {
            DataSeeder.seed(productService);
        }

        // Safety net: save on JVM shutdown too not just on a clean "Exit" from the main menu.
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                persistenceService.saveState(userRepository, productRepository)));

   
        boolean running = true;
        while (running) {
            view.showMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    authController.handleRegistration();
                    break;

                case "2":
                    Object user = authController.handleLogin();
                    if (user instanceof Admin) {
                        adminController.startAdminSession();
                    } else if (user instanceof Buyer) {
                        buyerController.startBuyerSession((Buyer) user);
                    }
                    break;

                case "3":
                    // Product page accessible without login
                    productController.showProductsPage(null);
                    break;

                case "0":
                    view.print("You have exited the online shop. Goodbye!");
                    running = false;
                    break;

                default:
                    view.printError("Invalid option. Please choose again.");
            }
        }

        // The shutdown hook above will also fire here, but saving explicitly
        // on a clean exit means the user sees the confirmation message
        // before the program actually terminates.
        persistenceService.saveState(userRepository, productRepository);
        scanner.close();
    }
}
