package com.cafepos.demo;

import com.cafepos.domain.*;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.WalletPayment;

import java.util.Scanner;

public final class Week5Demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductFactory factory = new ProductFactory();

        System.out.println("=== Welcome to Café POS System (Week 5 - Decorator + Factory) ===\n");

        Order order = new Order(OrderIds.next());

        boolean continueOrdering = true;

        while (continueOrdering) {
            System.out.println("\n========================================");
            System.out.println("      CAFÉ POS MENU - CUSTOM DRINKS");
            System.out.println("========================================");
            System.out.println("Base Drinks:");
            System.out.println("  ESP - Espresso      ($2.50)");
            System.out.println("  LAT - Latte         ($3.20)");
            System.out.println("  CAP - Cappuccino    ($3.00)");
            System.out.println("\nAdd-ons (combine with '+'):");
            System.out.println("  SHOT - Extra Shot   (+$0.80)");
            System.out.println("  OAT  - Oat Milk     (+$0.50)");
            System.out.println("  SYP  - Syrup        (+$0.40)");
            System.out.println("  L    - Large Size   (+$0.70)");
            System.out.println("\nExamples:");
            System.out.println("  ESP+SHOT        = Espresso + Extra Shot");
            System.out.println("  LAT+OAT+L       = Large Latte with Oat Milk");
            System.out.println("  CAP+SHOT+SYP+L  = Large Cappuccino + Extra Shot + Syrup");
            System.out.println("========================================");
            System.out.println("Available Actions:");
            System.out.println("  [a] - Add custom drink to order");
            System.out.println("  [v] - View current order");
            System.out.println("  [p] - Pay for order");
            System.out.println("  [q] - Quit system");
            System.out.println("========================================");
            System.out.print(">>> Enter a, v, p, or q: ");

            String choice = scanner.nextLine().trim().toLowerCase();
            System.out.println();

            switch (choice) {
                case "a":
                    addCustomDrink(scanner, factory, order);
                    break;

                case "v":
                    viewOrder(order);
                    break;

                case "p":
                    payOrder(scanner, order);
                    break;

                case "q":
                    continueOrdering = false;
                    System.out.println("\n========================================");
                    System.out.println("  Thank you for using Café POS System!");
                    System.out.println("========================================");
                    break;

                default:
                    System.out.println("\n ERROR: Invalid input!");
                    System.out.println("   Please type only ONE letter: a, v, p, or q");
            }
        }

        scanner.close();
    }

    private static void addCustomDrink(Scanner scanner, ProductFactory factory, Order order) {
        System.out.println("\n========================================");
        System.out.println("       BUILD YOUR CUSTOM DRINK");
        System.out.println("========================================");
        System.out.println("Enter recipe using format: BASE+ADDON1+ADDON2...");
        System.out.println("  Valid bases: ESP, LAT, CAP");
        System.out.println("  Valid add-ons: SHOT, OAT, SYP, L");
        System.out.println("\nExamples:");
        System.out.println("  ESP              (plain espresso)");
        System.out.println("  ESP+SHOT+OAT     (espresso with extra shot and oat milk)");
        System.out.println("  LAT+L            (large latte)");
        System.out.print("\n>>> Recipe: ");
        String recipe = scanner.nextLine().trim();

        System.out.print(">>> Quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            if (quantity <= 0) {
                System.out.println(" ERROR: Quantity must be greater than 0!");
                return;
            }

            Product product = factory.create(recipe);
            order.addItem(new LineItem(product, quantity));

            if (product instanceof Priced priced) {
                System.out.println("\n✓ Success!");
                System.out.println("  Added: " + quantity + "x " + product.name());
                System.out.println("  Unit price: $" + priced.price());
                System.out.println("  Line total: $" + priced.price().multiply(quantity));
            }

        } catch (NumberFormatException e) {
            System.out.println(" ERROR: Please enter a valid number for quantity!");
        } catch (IllegalArgumentException e) {
            System.out.println(" ERROR: " + e.getMessage());
            System.out.println("   Please check your recipe format and try again.");
        }
    }

    private static void viewOrder(Order order) {
        System.out.println("\n========================================");
        System.out.println("           CURRENT ORDER");
        System.out.println("========================================");

        if (order.items().isEmpty()) {
            System.out.println("  (Order is empty)");
        } else {
            System.out.println("Order #" + order.id());
            System.out.println("----------------------------------------");
            for (LineItem li : order.items()) {
                System.out.println(" - " + li.product().name());
                System.out.println("   x" + li.quantity() + " = $" + li.lineTotal());
            }
            System.out.println("----------------------------------------");
            System.out.println("Subtotal: $" + order.subtotal());
            System.out.println("Tax (10%): $" + order.taxAtPercent(10));
            System.out.println("Total: $" + order.totalWithTax(10));
        }
        System.out.println("========================================");
    }

    private static void payOrder(Scanner scanner, Order order) {
        System.out.println("\n========================================");
        System.out.println("           PAYMENT");
        System.out.println("========================================");

        if (order.items().isEmpty()) {
            System.out.println(" ERROR: Cannot pay for an empty order!");
            System.out.println("   Please add items first.");
            return;
        }

        System.out.println("Order Total: $" + order.totalWithTax(10));
        System.out.println("\nSelect payment method (type number):");
        System.out.println("  [1] - Cash Payment");
        System.out.println("  [2] - Card Payment");
        System.out.println("  [3] - Wallet Payment");
        System.out.print(">>> Enter 1, 2, or 3: ");

        String paymentChoice = scanner.nextLine().trim();

        try {
            switch (paymentChoice) {
                case "1":
                    order.pay(new CashPayment());
                    System.out.println("\n✓ Cash payment completed successfully!");
                    printReceipt(order);
                    break;

                case "2":
                    order.pay(new CardPayment());
                    System.out.println("\n✓ Card payment completed successfully!");
                    printReceipt(order);
                    break;

                case "3":
                    order.pay(new WalletPayment());
                    System.out.println("\n✓ Wallet payment completed successfully!");
                    printReceipt(order);
                    break;

                default:
                    System.out.println(" ERROR: Invalid payment method!");
                    System.out.println("   Please enter 1, 2, or 3");
            }
        } catch (Exception e) {
            System.out.println(" Payment failed: " + e.getMessage());
        }
    }

    private static void printReceipt(Order order) {
        System.out.println("\n========================================");
        System.out.println("             RECEIPT");
        System.out.println("========================================");
        System.out.println("Order #" + order.id());
        System.out.println("----------------------------------------");
        for (LineItem li : order.items()) {
            System.out.println(li.product().name());
            System.out.println("  x" + li.quantity() + " @ $" +
                    (li.product() instanceof Priced p ? p.price() : li.product().basePrice()) +
                    " = $" + li.lineTotal());
        }
        System.out.println("----------------------------------------");
        System.out.println("Subtotal:  $" + order.subtotal());
        System.out.println("Tax (10%): $" + order.taxAtPercent(10));
        System.out.println("TOTAL:     $" + order.totalWithTax(10));
        System.out.println("========================================");
        System.out.println("      Thank you for your order!");
        System.out.println("========================================");
    }
}
