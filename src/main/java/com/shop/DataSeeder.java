package com.shop;

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
import com.shop.service.ProductService;

import java.time.LocalDate;

/**
 * Seeds the store with initial demo products.
 */
public class DataSeeder {

    private DataSeeder() {}

    public static void seed(ProductService productService) {

        productService.addProduct(new Car(
                "BMW X5", 66000, 3, "BMW", 4300, true));
        productService.addProduct(new Car(
                "Toyota Camry", 42000, 5, "Toyota", 2500, true));

        productService.addProduct(new Bicycle(
                "Camp Mountain Bike", 15000, 10, "Camp", BikeType.MOUNTAIN));
        productService.addProduct(new Bicycle(
                "Giant Hybrid Bike", 22000, 4, "Giant", BikeType.HYBRID));

        productService.addProduct(new FlashMemory(
                "Samsung FIT Plus", 850, 50, 3.1, "23x15x7 mm", 128, "USB 3.1"));
        productService.addProduct(new FlashMemory(
                "Kingston DataTraveler", 450, 30, 4.2, "60x20x10 mm", 64, "USB 3.0"));

        productService.addProduct(new SSD(
                "Samsung 870 EVO", 4500, 20, 58, "100x70x7 mm", 500, 560, 530));
        productService.addProduct(new SSD(
                "WD Blue 3D", 3800, 15, 55, "100x70x7 mm", 250, 550, 525));

        productService.addProduct(new PersonalComputer(
                "Asus VivoMini", 35000, 8, 1200, "190x190x35 mm", "Intel Core i7-12700", 16));


        productService.addProduct(new Pencil(
                "Faber-Castell HB Pencil", 120, 200, "Germany", PencilType.HB));
        productService.addProduct(new Pencil(
                "Staedtler B Pencil", 95, 150, "Germany", PencilType.B));

        productService.addProduct(new Pen(
                "Bic Blue Pen", 80, 500, "France", "Blue"));
        productService.addProduct(new Pen(
                "Bic Black Pen", 80, 400, "France", "Black"));
        productService.addProduct(new Pen(
                "Pilot Red Pen", 150, 200, "Japan", "Red"));

        productService.addProduct(new Notebook(
                "80-Sheet Accounting Notebook", 350, 100, "Iran", 80, "Kraft"));
        productService.addProduct(new Notebook(
                "200-Sheet Spiral Notebook", 750, 60, "Iran", 200, "Glossy"));


        productService.addProduct(new Food(
                "Dark Milk Chocolate", 250, 80,
                LocalDate.of(2025, 1, 1), LocalDate.of(2026, 12, 31)));
        productService.addProduct(new Food(
                "Premium Mixed Nuts", 1200, 40,
                LocalDate.of(2025, 6, 1), LocalDate.of(2026, 6, 30)));
        productService.addProduct(new Food(
                "Oreo Cookies", 180, 120,
                LocalDate.of(2025, 3, 1), LocalDate.of(2026, 9, 15)));

        System.out.println("Shop ready with " + productService.getAllProducts().size() + " products.\n");
    }
}
