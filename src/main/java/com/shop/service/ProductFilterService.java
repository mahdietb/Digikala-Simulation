package com.shop.service;

import com.shop.model.Product;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service providing category-specific filters.
 * Filters specific to each category, on different attributes of that category.
 * Each subcategory below exposes at least one filter, selective or range-based,
 */
public class ProductFilterService {

    //Flash Memory

    public List<Product> filterFlashByUsbVersion(List<Product> products, String usbVersion) {
        return products.stream()
                .filter(p -> p instanceof FlashMemory)
                .filter(p -> ((FlashMemory) p).getUsbVersion().equalsIgnoreCase(usbVersion.trim()))
                .collect(Collectors.toList());
    }

    //SSD

    public List<Product> filterSsdByReadSpeed(List<Product> products, int minSpeed, int maxSpeed) {
        return products.stream()
                .filter(p -> p instanceof SSD)
                .filter(p -> {
                    int speed = ((SSD) p).getReadSpeedMBps();
                    return speed >= minSpeed && speed <= maxSpeed;
                })
                .collect(Collectors.toList());
    }

    public List<Product> filterSsdByWriteSpeed(List<Product> products, int minSpeed, int maxSpeed) {
        return products.stream()
                .filter(p -> p instanceof SSD)
                .filter(p -> {
                    int speed = ((SSD) p).getWriteSpeedMBps();
                    return speed >= minSpeed && speed <= maxSpeed;
                })
                .collect(Collectors.toList());
    }

    //Personal Computer

    public List<Product> filterPcByRam(List<Product> products, int minRamGB, int maxRamGB) {
        return products.stream()
                .filter(p -> p instanceof PersonalComputer)
                .filter(p -> {
                    int ram = ((PersonalComputer) p).getRamCapacityGB();
                    return ram >= minRamGB && ram <= maxRamGB;
                })
                .collect(Collectors.toList());
    }

    //Pencil

    public List<Product> filterPencilByType(List<Product> products, PencilType type) {
        return products.stream()
                .filter(p -> p instanceof Pencil)
                .filter(p -> ((Pencil) p).getPencilType() == type)
                .collect(Collectors.toList());
    }

    //Pen

    public List<Product> filterPenByColor(List<Product> products, String color) {
        return products.stream()
                .filter(p -> p instanceof Pen)
                .filter(p -> ((Pen) p).getColor().equalsIgnoreCase(color.trim()))
                .collect(Collectors.toList());
    }

    //Notebook

    public List<Product> filterNotebookByPageCount(List<Product> products, int minPages, int maxPages) {
        return products.stream()
                .filter(p -> p instanceof Notebook)
                .filter(p -> {
                    int pages = ((Notebook) p).getPageCount();
                    return pages >= minPages && pages <= maxPages;
                })
                .collect(Collectors.toList());
    }

    //Bicycle

    public List<Product> filterBicycleByType(List<Product> products, BikeType type) {
        return products.stream()
                .filter(p -> p instanceof Bicycle)
                .filter(p -> ((Bicycle) p).getBikeType() == type)
                .collect(Collectors.toList());
    }

    //Car

    public List<Product> filterCarByTransmission(List<Product> products, boolean automatic) {
        return products.stream()
                .filter(p -> p instanceof Car)
                .filter(p -> ((Car) p).isAutomatic() == automatic)
                .collect(Collectors.toList());
    }

    public List<Product> filterCarByEngineVolume(List<Product> products, int minCC, int maxCC) {
        return products.stream()
                .filter(p -> p instanceof Car)
                .filter(p -> {
                    int cc = ((Car) p).getEngineVolumeCC();
                    return cc >= minCC && cc <= maxCC;
                })
                .collect(Collectors.toList());
    }

    //Food

    public List<Product> filterFoodByExpiry(List<Product> products, boolean expired) {
        return products.stream()
                .filter(p -> p instanceof Food)
                .filter(p -> ((Food) p).isExpired() == expired)
                .collect(Collectors.toList());
    }
}
