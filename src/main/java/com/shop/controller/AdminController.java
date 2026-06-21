package com.shop.controller;

import com.shop.model.*;
import com.shop.model.enums.BikeType;
import com.shop.model.enums.PencilType;
import com.shop.model.products.digital.PersonalComputer;
import com.shop.model.products.digital.storage.FlashMemory;
import com.shop.model.products.digital.storage.SSD;
import com.shop.model.products.food.Food;
import com.shop.model.products.stationery.Notebook;
import com.shop.model.products.stationery.Pen;
import com.shop.model.products.stationery.Pencil;
import com.shop.model.products.vehicle.Bicycle;
import com.shop.model.products.vehicle.Car;
import com.shop.repository.UserRepository;
import com.shop.service.ProductService;
import com.shop.service.RequestService;
import com.shop.view.ConsoleView;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Parses and executes admin commands.
 */
public class AdminController {
    private final ConsoleView view;
    private final ProductService productService;
    private final RequestService requestService;
    private final UserRepository userRepository;
    private final Scanner scanner;

    public AdminController(ConsoleView view, ProductService productService,
                           RequestService requestService, UserRepository userRepository,
                           Scanner scanner) {
        this.view = view;
        this.productService = productService;
        this.requestService = requestService;
        this.userRepository = userRepository;
        this.scanner = scanner;
    }

    /**
     * Admin session loop. Returns when admin logs out.
     */
    public void startAdminSession() {
        view.showAdminWelcome();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print(">>> ");
                continue;
            }
            if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("logout")) {
                view.print("Exiting admin panel.");
                break;
            }
            processCommand(line);
            System.out.print(">>> ");
        }
    }

    private void processCommand(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length == 0) return;

        String cmd = parts[0].toLowerCase();

        try {
            switch (cmd) {
                case "help":
                    view.showAdminHelp();
                    break;
                case "add":
                    handleAdd(parts);
                    break;
                case "remove":
                    handleRemove(parts);
                    break;
                case "edit":
                    handleEdit(line, parts);
                    break;
                case "list":
                    handleList();
                    break;
                case "users":
                    view.showUserList(userRepository.getAllBuyers());
                    break;
                case "requests":
                    view.showRequests(requestService.getAllRequests());
                    break;
                case "approve":
                    handleApprove(parts);
                    break;
                case "reject":
                    handleReject(parts);
                    break;
                default:
                    view.printError("Invalid command. Type 'help' for a list of commands.");
            }
        } catch (IllegalArgumentException | IllegalStateException
                 | ArrayIndexOutOfBoundsException | java.time.format.DateTimeParseException e) {
            view.printError(e.getMessage());
        } catch (RuntimeException e) {
            // Safety net: an unexpected error in a single command should
            // never crash the whole admin session.
            view.printError("Could not process command: " + e.getMessage());
        }
    }

    // Add -part

    private void handleAdd(String[] parts) {
        if (parts.length < 2) {
            view.printError("Please specify a product type. Example: Add Car ...");
            return;
        }
        String type = parts[1].toLowerCase();
        switch (type) {
            case "car":
                addCar(parts);
                break;
            case "bicycle":
            case "bike":
                addBicycle(parts);
                break;
            case "flash":
                addFlash(parts);
                break;
            case "ssd":
                addSSD(parts);
                break;
            case "pc":
                addPC(parts);
                break;
            case "pencil":
                addPencil(parts);
                break;
            case "pen":
                addPen(parts);
                break;
            case "notebook":
                addNotebook(parts);
                break;
            case "food":
                addFood(parts);
                break;
            default:
                view.printError("Invalid product type: " + parts[1]);
        }
    }


    private void addCar(String[] parts) {
        // parts[0]=Add, parts[1]=Car, parts[2]=engineCC, parts[3]=auto,
        // parts[4]=manufacturer, parts[5]=name, parts[6]=price, parts[7]=stock
        if (parts.length < 8) {
            throw new IllegalArgumentException(
                    "Format: Add Car <engineCC> <true/false> <manufacturer> <name> <price> <stock>");
        }
        int engineCC = Integer.parseInt(parts[2]);
        boolean auto = Boolean.parseBoolean(parts[3]);
        String manufacturer = parts[4];
        String name = parts[5];
        double price = Double.parseDouble(parts[6]);
        int stock = Integer.parseInt(parts[7]);

        Car car = new Car(name, price, stock, manufacturer, engineCC, auto);
        productService.addProduct(car);
    }

    /**
     * Add Bicycle: Add Bicycle <manufacturer> <type> <name> <price> <stock>
     */
    private void addBicycle(String[] parts) {
        if (parts.length < 7) {
            throw new IllegalArgumentException(
                    "Format: Add Bicycle <manufacturer> <MOUNTAIN/ROAD/CITY/HYBRID> <name> <price> <stock>");
        }
        String manufacturer = parts[2];
        BikeType type = BikeType.valueOf(parts[3].toUpperCase());
        String name = parts[4];
        double price = Double.parseDouble(parts[5]);
        int stock = Integer.parseInt(parts[6]);

        Bicycle bicycle = new Bicycle(name, price, stock, manufacturer, type);
        productService.addProduct(bicycle);
    }

    /**
     * Add Flash: Add Flash <name> <price> <stock> <weight> <dims> <capacityGB> <usbVersion>
     */
    private void addFlash(String[] parts) {
        if (parts.length < 9) {
            throw new IllegalArgumentException(
                    "Format: Add Flash <name> <price> <stock> <weight> <dims> <capacityGB> <usbVersion>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        double weight = Double.parseDouble(parts[5]);
        String dims = parts[6];
        int capacity = Integer.parseInt(parts[7]);
        String usbVersion = parts[8];

        FlashMemory flash = new FlashMemory(name, price, stock, weight, dims, capacity, usbVersion);
        productService.addProduct(flash);
    }

    /**
     * Add SSD: Add SSD <name> <price> <stock> <weight> <dims> <capacityGB> <readSpeed> <writeSpeed>
     */
    private void addSSD(String[] parts) {
        if (parts.length < 10) {
            throw new IllegalArgumentException(
                    "Format: Add SSD <name> <price> <stock> <weight> <dims> <capacityGB> <readSpeed> <writeSpeed>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        double weight = Double.parseDouble(parts[5]);
        String dims = parts[6];
        int capacity = Integer.parseInt(parts[7]);
        int readSpeed = Integer.parseInt(parts[8]);
        int writeSpeed = Integer.parseInt(parts[9]);

        SSD ssd = new SSD(name, price, stock, weight, dims, capacity, readSpeed, writeSpeed);
        productService.addProduct(ssd);
    }

    /**
     * Add PC: Add PC <name> <price> <stock> <weight> <dims> <processor> <ramGB>
     */
    private void addPC(String[] parts) {
        if (parts.length < 9) {
            throw new IllegalArgumentException(
                    "Format: Add PC <name> <price> <stock> <weight> <dims> <processor> <ramGB>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        double weight = Double.parseDouble(parts[5]);
        String dims = parts[6];
        String processor = parts[7];
        int ramGB = Integer.parseInt(parts[8]);

        PersonalComputer pc = new PersonalComputer(name, price, stock, weight, dims, processor, ramGB);
        productService.addProduct(pc);
    }

    /**
     * Add Pencil: Add Pencil <name> <price> <stock> <country> <HB/B/F/H/H2>
     */
    private void addPencil(String[] parts) {
        if (parts.length < 7) {
            throw new IllegalArgumentException(
                    "Format: Add Pencil <name> <price> <stock> <country> <HB/B/F/H/H2>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        String country = parts[5];
        PencilType pencilType = PencilType.valueOf(parts[6].toUpperCase());

        Pencil pencil = new Pencil(name, price, stock, country, pencilType);
        productService.addProduct(pencil);
    }

    /**
     * Add Pen: Add Pen <name> <price> <stock> <country> <color>
     */
    private void addPen(String[] parts) {
        if (parts.length < 7) {
            throw new IllegalArgumentException(
                    "Format: Add Pen <name> <price> <stock> <country> <color>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        String country = parts[5];
        String color = parts[6];

        Pen pen = new Pen(name, price, stock, country, color);
        productService.addProduct(pen);
    }

    /**
     * Add Notebook: Add Notebook <name> <price> <stock> <country> <pages> <paperType>
     */
    private void addNotebook(String[] parts) {
        if (parts.length < 8) {
            throw new IllegalArgumentException(
                    "Format: Add Notebook <name> <price> <stock> <country> <pages> <paperType>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        String country = parts[5];
        int pages = Integer.parseInt(parts[6]);
        String paperType = parts[7];

        Notebook notebook = new Notebook(name, price, stock, country, pages, paperType);
        productService.addProduct(notebook);
    }

    /**
     * Add Food: Add Food <name> <price> <stock> <prodDate:YYYY-MM-DD> <expDate:YYYY-MM-DD>
     */
    private void addFood(String[] parts) {
        if (parts.length < 7) {
            throw new IllegalArgumentException(
                    "Format: Add Food <name> <price> <stock> <prodDate:YYYY-MM-DD> <expDate:YYYY-MM-DD>");
        }
        String name = parts[2];
        double price = Double.parseDouble(parts[3]);
        int stock = Integer.parseInt(parts[4]);
        LocalDate prodDate = parseDate(parts[5], "production date");
        LocalDate expDate = parseDate(parts[6], "expiry date");

        Food food = new Food(name, price, stock, prodDate, expDate);
        productService.addProduct(food);
    }

    /**
     * Parses a date string in YYYY-MM-DD format, converting any parse
     * failure (wrong format, invalid year/month/day, typos like "204"
     * instead of "2024") into a clear, catchable IllegalArgumentException
     * instead of letting DateTimeParseException crash the program.
     */
    private LocalDate parseDate(String text, String fieldLabel) {
        try {
            return LocalDate.parse(text);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid " + fieldLabel + ": \"" + text + "\". Expected format: YYYY-MM-DD (e.g. 2024-01-31).");
        }
    }

    // Remove-part

    private void handleRemove(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Format: Remove <productId>");
        }
        String id = parts[1];
        if (!productService.removeProduct(id)) {
            view.printError("No product found with ID " + id + ".");
        }
    }

    // Edit-part
    //edit the common problem with the multipart names
    /**
     * Edit <id> name=<name> price=<price> stock=<stock>
     * Supports multi-word names
     * Edit 61070414 name=Samsung FIT Plus price=900 stock=40
     * Field order does not matter; any subset of name/price/stock may be given.
     */
    private void handleEdit(String fullLine, String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException("Format: Edit <id> name=<name> price=<price> stock=<stock>");
        }
        String id = parts[1];

        // Everything after Edit <id> — parsed as a whole so multi-word values like Samsung FIT Plus aren't broken apart.
        String rest = fullLine.substring(fullLine.indexOf(id) + id.length()).trim();

        String newName = null;
        Double newPrice = null;
        Integer newStock = null;

        // Matches key=value where value runs until the next "key=" or end of string.
        java.util.regex.Matcher matcher = java.util.regex.Pattern
                .compile("(name|price|stock)=(.*?)(?=\\s+(?:name|price|stock)=|$)")
                .matcher(rest);

        boolean foundAny = false;
        while (matcher.find()) {
            foundAny = true;
            String key = matcher.group(1);
            String value = matcher.group(2).trim();

            switch (key) {
                case "name":
                    if (value.isEmpty()) {
                        throw new IllegalArgumentException("Name cannot be empty.");
                    }
                    newName = value;
                    break;
                case "price":
                    try {
                        newPrice = Double.parseDouble(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid price: \"" + value + "\"");
                    }
                    break;
                case "stock":
                    try {
                        newStock = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid stock: \"" + value + "\"");
                    }
                    break;
            }
        }

        if (!foundAny) {
            throw new IllegalArgumentException(
                    "Format: Edit <id> name=<name> price=<price> stock=<stock> — at least one field is required.");
        }

        productService.editProduct(id, newName, newPrice, newStock);
    }

    // List of product

    private void handleList() {
        java.util.List<Product> all = productService.getAllProducts();
        if (all.isEmpty()) {
            view.print("No products registered in the shop.");
            return;
        }
        view.print("\n=========== Product List ===========");
        all.forEach(p -> view.print(String.format("  [%-10s] %-30s | %,8.0f Toman | Stock: %-4d | Rating: %.1f",
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getAverageRating())));
    }

    // Approve or Reject the requests

    private void handleApprove(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Format: Approve <requestId>");
        }
        int id = Integer.parseInt(parts[1]);
        requestService.approveRequest(id);
    }

    private void handleReject(String[] parts) {
        if (parts.length < 2) {
            throw new IllegalArgumentException("Format: Reject <requestId>");
        }
        int id = Integer.parseInt(parts[1]);
        requestService.rejectRequest(id);
    }
}
