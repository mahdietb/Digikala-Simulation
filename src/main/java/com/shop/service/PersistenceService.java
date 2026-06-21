package com.shop.service;

import com.shop.model.Admin;
import com.shop.model.Buyer;
import com.shop.model.Product;
import com.shop.model.Request;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;

import java.io.*;
import java.util.List;

/**
 * Handles saving and loading the entire application state to/from disk,
 * so data  of registered buyers, products, invoices, pending requests and all of them
 * survives between program runs.
 */
public class PersistenceService {

    private static final String DATA_FILE = "shop_data.ser";

    /**
     * Plain data holder bundling everything that needs to survive a restart.
     * Kept as a private static nested class so it can be serialized as one
     * unit without needing to touch the repository/service classes.
     */
    private static class ShopState implements Serializable {
        private static final long serialVersionUID = 1L;

        List<Buyer> buyers;
        List<Product> products;
        List<Request> requests;
    }

    /**
     * Saves the current state of the shop to disk.
     */
    public void saveState(UserRepository userRepository, ProductRepository productRepository) {
        ShopState state = new ShopState();
        state.buyers = userRepository.getAllBuyers();
        state.products = productRepository.findAll();
        state.requests = Admin.getInstance().getRequestList();

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(state);
            System.out.println("Shop data saved (" + state.buyers.size() + " users, "
                    + state.products.size() + " products, " + state.requests.size() + " requests).");
        } catch (IOException e) {
            System.out.println("Warning: could not save shop data. " + e.getMessage());
        }
    }

    /**
     * Returns true if a saved data file exists on disk.
     */
    public boolean hasSavedState() {
        return new File(DATA_FILE).exists();
    }

    /**
     * Loads previously saved state from disk into the given repositories.
     * Also restores the Admin singleton's product/request lists.
     * @return true if state was successfully loaded, false otherwise
     */
    public boolean loadState(UserRepository userRepository, ProductRepository productRepository) {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            ShopState state = (ShopState) in.readObject();

            for (Buyer buyer : state.buyers) {
                userRepository.addBuyer(buyer);
            }
            for (Product product : state.products) {
                productRepository.addProduct(product);
            }

            // Restore the Admin singleton's internal lists so they match what was saved.
            Admin admin = Admin.getInstance();
            admin.getProductList().clear();
            admin.getProductList().addAll(state.products);
            admin.getRequestList().clear();
            admin.getRequestList().addAll(state.requests);

            System.out.println("Loaded saved shop data (" + state.buyers.size() + " users, "
                    + state.products.size() + " products, " + state.requests.size() + " requests).");
            return true;

        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.out.println("Warning: could not load saved shop data (" + e.getMessage() +
                    "). Starting with a fresh shop instead.");
            return false;
        }
    }

    /**
     * Deletes the saved data file, if any. Useful for resetting the shop
     * to a clean state on the next run.
     */
    public boolean clearSavedState() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
