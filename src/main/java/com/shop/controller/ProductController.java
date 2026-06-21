package com.shop.controller;

import com.shop.model.Buyer;
import com.shop.model.Product;
import com.shop.model.enums.BikeType;
import com.shop.model.enums.PencilType;
import com.shop.model.products.digital.DigitalProduct;
import com.shop.model.products.digital.PersonalComputer;
import com.shop.model.products.digital.storage.FlashMemory;
import com.shop.model.products.digital.storage.SSD;
import com.shop.model.products.food.Food;
import com.shop.model.products.stationery.Notebook;
import com.shop.model.products.stationery.Pen;
import com.shop.model.products.stationery.Pencil;
import com.shop.model.products.stationery.Stationery;
import com.shop.model.products.vehicle.Bicycle;
import com.shop.model.products.vehicle.Car;
import com.shop.model.products.vehicle.Vehicle;
import com.shop.service.CommentRatingService;
import com.shop.service.ProductFilterService;
import com.shop.service.ProductService;
import com.shop.service.ShopService;
import com.shop.view.ConsoleView;

import java.util.List;
import java.util.Scanner;

/**
 * Controller for the products browsing page with or without login.
 */
public class ProductController {
    private static final int PAGE_SIZE = 10;

    private final ConsoleView view;
    private final ProductService productService;
    private final ShopService shopService;
    private final CommentRatingService commentRatingService;
    private final ProductFilterService filterService;
    private final Scanner scanner;

    public ProductController(ConsoleView view,
                             ProductService productService,
                             ShopService shopService,
                             CommentRatingService commentRatingService,
                             Scanner scanner) {
        this.view = view;
        this.productService = productService;
        this.shopService = shopService;
        this.commentRatingService = commentRatingService;
        this.filterService = new ProductFilterService();
        this.scanner = scanner;
    }

    /**
     * Main products page loop.
     * @param buyer - null if guest user
     */
    public void showProductsPage(Buyer buyer) {
        List<Product> currentList = productService.getAllProducts();
        int currentPage = 1;
        boolean inPage = true;

        while (inPage) {
            int totalPages = productService.getTotalPages(currentList, PAGE_SIZE);
            if (totalPages == 0) totalPages = 1;

            List<Product> pageItems = productService.getPage(currentList, currentPage, PAGE_SIZE);
            view.showProductsHeader();
            view.showProductList(pageItems, currentPage, totalPages);
            view.showProductsMenu();

            String input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "1":
                    currentList = productService.getAllProducts();
                    currentPage = 1;
                    view.printSuccess("Showing all products.");
                    break;
                case "2":
                    view.prompt("Search term");
                    String keyword = scanner.nextLine().trim();
                    currentList = productService.searchProducts(keyword);
                    currentPage = 1;
                    break;
                case "3":
                    currentList = openFilterMenu();
                    currentPage = 1;
                    break;
                case "4":
                    if (currentList.equals(productService.getAllProducts())) {
                        view.print("Filters are currently cleared. Showing all products.");
                    } else {
                        currentList = productService.getAllProducts();
                        currentPage = 1;
                        view.printSuccess("Filters cleared.");
                    }
                    break;
                case "5":
                    showProductDetail(buyer);
                    break;
                case "6":
                    handleAddToCart(buyer);
                    break;
                case "n":
                    if (currentPage < totalPages) currentPage++;
                    else view.print("You are already on the last page.");
                    break;
                case "p":
                    if (currentPage > 1) currentPage--;
                    else view.print("You are already on the first page.");
                    break;
                case "0":
                    inPage = false;
                    break;
                default:
                    view.printError("Invalid option.");
            }
        }
    }

    // Filter with 3 levels as mentioned in the instruction - part

    private List<Product> openFilterMenu() {
        List<Product> workingSet = productService.getAllProducts();
        boolean inFilterMenu = true;

        while (inFilterMenu) {
            System.out.println("\n=========== Filter Products ===========");
            System.out.println("Current result count: " + workingSet.size());
            System.out.println("  1. General filters (availability / price range / rating range)");
            System.out.println("  2. Filter by category and subcategory");
            System.out.println("  3. Subcategory-specific filters");
            System.out.println("  4. Apply and return to product list");
            System.out.println("  0. Cancel (discard filters, return to product list)");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    workingSet = applyGeneralFilters(workingSet);
                    break;
                case "2":
                    workingSet = applyCategoryFilter(workingSet);
                    break;
                case "3":
                    workingSet = applySubcategoryFilter(workingSet);
                    break;
                case "4":
                    inFilterMenu = false;
                    break;
                case "0":
                    return productService.getAllProducts();
                default:
                    view.printError("Invalid option.");
            }
        }
        return workingSet;
    }

    // General attribute filters

    private List<Product> applyGeneralFilters(List<Product> current) {
        System.out.println("\n--- General Filters ---");
        System.out.println("  1. Availability (in stock only) [selective]");
        System.out.println("  2. Price range [range]");
        System.out.println("  3. Rating range [range]");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                view.printSuccess("Filtered to in-stock products only.");
                return current.stream().filter(Product::isAvailable).toList();
            case "2":
                return filterByPrice(current);
            case "3":
                return filterByRating(current);
            case "0":
                return current;
            default:
                view.printError("Invalid option.");
                return current;
        }
    }

    private List<Product> filterByPrice(List<Product> current) {
        try {
            view.prompt("Minimum price");
            double min = Double.parseDouble(scanner.nextLine().trim());
            view.prompt("Maximum price");
            double max = Double.parseDouble(scanner.nextLine().trim());
            return current.stream()
                    .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                    .toList();
        } catch (NumberFormatException e) {
            view.printError("Invalid price.");
            return current;
        }
    }

    private List<Product> filterByRating(List<Product> current) {
        try {
            view.prompt("Minimum rating (0 to 5)");
            double min = Double.parseDouble(scanner.nextLine().trim());
            view.prompt("Maximum rating (0 to 5)");
            double max = Double.parseDouble(scanner.nextLine().trim());
            return current.stream()
                    .filter(p -> p.getAverageRating() >= min && p.getAverageRating() <= max)
                    .toList();
        } catch (NumberFormatException e) {
            view.printError("Invalid rating.");
            return current;
        }
    }

    // Category / subcategory filter

    private List<Product> applyCategoryFilter(List<Product> current) {
        System.out.println("\n--- Filter by Category ---");
        System.out.println("  1. Digital");
        System.out.println("  2. Stationery");
        System.out.println("  3. Vehicle");
        System.out.println("  4. Food");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine().trim();
        List<Product> byMainCategory;
        switch (choice) {
            case "1": byMainCategory = current.stream().filter(p -> p instanceof DigitalProduct).toList(); break;
            case "2": byMainCategory = current.stream().filter(p -> p instanceof Stationery).toList(); break;
            case "3": byMainCategory = current.stream().filter(p -> p instanceof Vehicle).toList(); break;
            case "4": byMainCategory = current.stream().filter(p -> p instanceof Food).toList(); break;
            case "0": return current;
            default:
                view.printError("Invalid option.");
                return current;
        }

        return refineBySubcategory(byMainCategory, choice);
    }

    private List<Product> refineBySubcategory(List<Product> current, String mainCategoryChoice) {
        System.out.println("\n--- Refine by Subcategory (optional) ---");
        switch (mainCategoryChoice) {
            case "1":
                System.out.println("  1. Flash Memory");
                System.out.println("  2. SSD");
                System.out.println("  3. Personal Computer");
                System.out.println("  0. Skip - keep all Digital products");
                System.out.print("Your choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": return current.stream().filter(p -> p instanceof FlashMemory).toList();
                    case "2": return current.stream().filter(p -> p instanceof SSD).toList();
                    case "3": return current.stream().filter(p -> p instanceof PersonalComputer).toList();
                    default: return current;
                }
            case "2":
                System.out.println("  1. Pencil");
                System.out.println("  2. Pen");
                System.out.println("  3. Notebook");
                System.out.println("  0. Skip - keep all Stationery products");
                System.out.print("Your choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": return current.stream().filter(p -> p instanceof Pencil).toList();
                    case "2": return current.stream().filter(p -> p instanceof Pen).toList();
                    case "3": return current.stream().filter(p -> p instanceof Notebook).toList();
                    default: return current;
                }
            case "3":
                System.out.println("  1. Car");
                System.out.println("  2. Bicycle");
                System.out.println("  0. Skip - keep all Vehicle products");
                System.out.print("Your choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": return current.stream().filter(p -> p instanceof Car).toList();
                    case "2": return current.stream().filter(p -> p instanceof Bicycle).toList();
                    default: return current;
                }
            default:
                // Food has no further subcategories
                return current;
        }
    }

    //Subcategory-specific attribute filters(min 5 attribute)

    private List<Product> applySubcategoryFilter(List<Product> current) {
        System.out.println("\n--- Subcategory-Specific Filters ---");
        System.out.println("  1. Flash Memory - USB version [selective]");
        System.out.println("  2. SSD - read speed range (MB/s) [range]");
        System.out.println("  3. SSD - write speed range (MB/s) [range]");
        System.out.println("  4. Personal Computer - RAM range (GB) [range]");
        System.out.println("  5. Pencil - pencil type [selective]");
        System.out.println("  6. Pen - color [selective]");
        System.out.println("  7. Notebook - page count range [range]");
        System.out.println("  8. Bicycle - bike type [selective]");
        System.out.println("  9. Car - transmission (automatic/manual) [selective]");
        System.out.println(" 10. Car - engine volume range (CC) [range]");
        System.out.println(" 11. Food - expired / not expired [selective]");
        System.out.println("  0. Back");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine().trim();
        try {
            switch (choice) {
                case "1":
                    view.prompt("USB version (e.g. USB 3.0, USB 3.1)");
                    return filterService.filterFlashByUsbVersion(current, scanner.nextLine().trim());
                case "2": {
                    view.prompt("Minimum read speed (MB/s)");
                    int min = Integer.parseInt(scanner.nextLine().trim());
                    view.prompt("Maximum read speed (MB/s)");
                    int max = Integer.parseInt(scanner.nextLine().trim());
                    return filterService.filterSsdByReadSpeed(current, min, max);
                }
                case "3": {
                    view.prompt("Minimum write speed (MB/s)");
                    int min = Integer.parseInt(scanner.nextLine().trim());
                    view.prompt("Maximum write speed (MB/s)");
                    int max = Integer.parseInt(scanner.nextLine().trim());
                    return filterService.filterSsdByWriteSpeed(current, min, max);
                }
                case "4": {
                    view.prompt("Minimum RAM (GB)");
                    int min = Integer.parseInt(scanner.nextLine().trim());
                    view.prompt("Maximum RAM (GB)");
                    int max = Integer.parseInt(scanner.nextLine().trim());
                    return filterService.filterPcByRam(current, min, max);
                }
                case "5": {
                    view.print("Pencil types: HB, B, F, H, H2");
                    view.prompt("Pencil type");
                    PencilType type = PencilType.valueOf(scanner.nextLine().trim().toUpperCase());
                    return filterService.filterPencilByType(current, type);
                }
                case "6":
                    view.prompt("Color");
                    return filterService.filterPenByColor(current, scanner.nextLine().trim());
                case "7": {
                    view.prompt("Minimum page count");
                    int min = Integer.parseInt(scanner.nextLine().trim());
                    view.prompt("Maximum page count");
                    int max = Integer.parseInt(scanner.nextLine().trim());
                    return filterService.filterNotebookByPageCount(current, min, max);
                }
                case "8": {
                    view.print("Bike types: MOUNTAIN, ROAD, CITY, HYBRID");
                    view.prompt("Bike type");
                    BikeType type = BikeType.valueOf(scanner.nextLine().trim().toUpperCase());
                    return filterService.filterBicycleByType(current, type);
                }
                case "9":
                    view.prompt("Automatic? (true/false)");
                    boolean automatic = Boolean.parseBoolean(scanner.nextLine().trim());
                    return filterService.filterCarByTransmission(current, automatic);
                case "10": {
                    view.prompt("Minimum engine volume (CC)");
                    int min = Integer.parseInt(scanner.nextLine().trim());
                    view.prompt("Maximum engine volume (CC)");
                    int max = Integer.parseInt(scanner.nextLine().trim());
                    return filterService.filterCarByEngineVolume(current, min, max);
                }
                case "11":
                    view.prompt("Show expired products? (true/false)");
                    boolean expired = Boolean.parseBoolean(scanner.nextLine().trim());
                    return filterService.filterFoodByExpiry(current, expired);
                case "0":
                    return current;
                default:
                    view.printError("Invalid option.");
                    return current;
            }
        } catch (IllegalArgumentException e) {
            view.printError("Invalid input: " + e.getMessage());
            return current;
        }
    }

    //  Product Detail - part

    private void showProductDetail(Buyer buyer) {
        view.prompt("Product ID");
        String id = scanner.nextLine().trim();
        Product product = productService.getProductById(id);
        if (product == null) {
            view.printError("No product found with this ID.");
            return;
        }

        view.showProductDetails(product);
        commentRatingService.showApprovedComments(product);

        // Action menu on product page
        System.out.println("\n--- Available actions: ---");
        System.out.println("  1. Add to cart");
        if (buyer != null) {
            System.out.println("  2. Write a comment");
            System.out.println("  3. Submit a rating (only for buyers who purchased this product)");
        }
        System.out.println("  0. Back");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                handleAddToCartForProduct(buyer, product);
                break;
            case "2":
                if (buyer != null) handleComment(buyer, product);
                else view.print("Please log in to write a comment.");
                break;
            case "3":
                if (buyer != null) handleRating(buyer, product);
                else view.print("Please log in to submit a rating.");
                break;
            case "0":
                break;
            default:
                view.printError("Invalid option.");
        }
    }

    private void handleAddToCartForProduct(Buyer buyer, Product product) {
        if (buyer == null) {
            view.print("Please log in to add items to your cart.");
            return;
        }
        if (!product.isAvailable()) {
            view.printError("This product is out of stock.");
            return;
        }
        try {
            view.prompt("Quantity");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            shopService.addToCart(buyer, product.getId(), qty);
        } catch (NumberFormatException e) {
            view.printError("Invalid quantity.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleAddToCart(Buyer buyer) {
        if (buyer == null) {
            view.print("Please log in to add items to your cart.");
            return;
        }
        view.prompt("Product ID");
        String id = scanner.nextLine().trim();
        try {
            view.prompt("Quantity");
            int qty = Integer.parseInt(scanner.nextLine().trim());
            shopService.addToCart(buyer, id, qty);
        } catch (NumberFormatException e) {
            view.printError("Invalid quantity.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleComment(Buyer buyer, Product product) {
        view.prompt("Write your comment");
        String text = scanner.nextLine().trim();
        try {
            commentRatingService.submitComment(buyer, product, text);
        } catch (IllegalArgumentException e) {
            view.printError(e.getMessage());
        }
    }

    private void handleRating(Buyer buyer, Product product) {
        view.prompt("Enter your rating (1 to 5)");
        try {
            int score = Integer.parseInt(scanner.nextLine().trim());
            commentRatingService.submitRating(buyer, product, score);
        } catch (NumberFormatException e) {
            view.printError("Invalid rating.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.printError(e.getMessage());
        }
    }
}
