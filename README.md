# Digikala E-Commerce Simulation

A back-end simulation of a large-scale e-commerce platform inspired by Digikala, built with Java using object-oriented design principles.

## Overview

This project models the core functionality of an online shopping platform through a fully Java-based back-end system. The focus is on clean architecture, OOP design patterns, and realistic entity modeling — without a front-end or database dependency.

## Features

- User account management (registration, login, profile)
- Product catalog with categories and inventory tracking
- Shopping cart and order placement flow
- Seller and buyer role separation
- Order status management (pending, confirmed, shipped, delivered)

## Tech Stack

- **Language:** Java
- **Paradigm:** Object-Oriented Programming (OOP)
- **IDE:** IntelliJ IDEA
- **Build:** Manual / Javac

## Project Structure

```
Digikala-Simulation/
├── src/
│   ├── models/        # User, Product, Order, Cart, ...
│   ├── services/      # Business logic layer
│   ├── exceptions/    # Custom exception handling
│   └── Main.java      # Entry point
```

## How to Run

```bash
# Clone the repository
git clone https://github.com/mahdietb/Digikala-Simulation.git
cd Digikala-Simulation

# Compile
javac src/**/*.java -d out/

# Run
java -cp out/ Main
```

## Design Highlights

- Separation of concerns between models, services, and presentation logic
- Use of inheritance and interfaces to model user roles
- Exception handling for invalid operations (e.g., out-of-stock orders)

## Author

**Mahdieh Torabi** — [github.com/mahdietb](https://github.com/mahdietb)
