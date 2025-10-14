package com.cafepos.demo;

import com.cafepos.catalog.Catalog;
import com.cafepos.catalog.InMemoryCatalog;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.observer.CustomerNotifier;
import com.cafepos.observer.DeliveryDesk;
import com.cafepos.observer.KitchenDisplay;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.WalletPayment;

import java.util.Scanner;

public final class Week4Demo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-LAT", "Latte", Money.of(3.50)));
        catalog.add(new SimpleProduct("P-CAP", "Cappuccino", Money.of(3.00)));
        catalog.add(new SimpleProduct("P-MOC", "Mocha", Money.of(4.00)));

        System.out.println("=== Welcome to Café POS System ===\n");

        Order order = new Order(OrderIds.next());

        order.register(new KitchenDisplay());
        order.register(new DeliveryDesk());
        order.register(new CustomerNotifier());

        System.out.println("Observers registered: KitchenDisplay, DeliveryDesk, CustomerNotifier\n");

        boolean continueOrdering = true;

        while (continueOrdering) {
            System.out.println("\n========================================");
            System.out.println("           CAFÉ POS MENU");
            System.out.println("========================================");
            System.out.println("Available Products:");
            System.out.println("  P-ESP - Espresso    (€2.50)");
            System.out.println("  P-LAT - Latte       (€3.50)");
            System.out.println("  P-CAP - Cappuccino  (€3.00)");
            System.out.println("  P-MOC - Mocha       (€4.00)");
            System.out.println("========================================");
            System.out.println("Available Actions (type ONE letter):");
            System.out.println("  [a] - Add item to order");
            System.out.println("  [p] - Pay for order");
            System.out.println("  [r] - Mark order as ready");
            System.out.println("  [q] - Quit system");
            System.out.println("========================================");
            System.out.print(">>> Enter a, p, r, or q: ");

            String choice = scanner.nextLine().trim().toLowerCase();
            System.out.println();

            switch (choice) {
                case "a":
                    addItemToOrder(scanner, catalog, order);
                    break;

                case "p":
                    payOrder(scanner, order);
                    break;

                case "r":
                    System.out.println("--- Mark Order Ready ---");
                    order.markReady();
                    break;

                case "q":
                    continueOrdering = false;
                    System.out.println("\n========================================");
                    System.out.println("  Thank you for using Café POS System!");
                    System.out.println("========================================");
                    break;

                default:
                    System.out.println("\n❌ ERROR: Invalid input!");
                    System.out.println("   Please type only ONE letter: a, p, r, or q");
            }
        }

        scanner.close();
    }

    private static void addItemToOrder(Scanner scanner, Catalog catalog, Order order) {
        System.out.println("\n========================================");
        System.out.println("         ADD ITEM TO ORDER");
        System.out.println("========================================");
        System.out.println("Enter product code from list above:");
        System.out.println("  Examples: P-ESP, P-LAT, P-CAP, P-MOC");
        System.out.print(">>> Product code: ");
        String productId = scanner.nextLine().trim().toUpperCase();

        System.out.print(">>> Quantity: ");
        try {
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            if (quantity <= 0) {
                System.out.println("❌ ERROR: Quantity must be greater than 0!");
                return;
            }

            var product = catalog.findById(productId);
            if (product.isPresent()) {
                order.addItem(new LineItem(product.get(), quantity));
                System.out.println("✓ Success: " + quantity + "x " + product.get().name() + " added!");
            } else {
                System.out.println("❌ ERROR: Product code '" + productId + "' not found!");
                System.out.println("   Valid codes: P-ESP, P-LAT, P-CAP, P-MOC");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ ERROR: Please enter a valid number for quantity!");
        }
    }

    private static void payOrder(Scanner scanner, Order order) {
        System.out.println("\n========================================");
        System.out.println("           PAYMENT");
        System.out.println("========================================");
        System.out.println("Select payment method (type number):");
        System.out.println("  [1] - Cash Payment");
        System.out.println("  [2] - Card Payment");
        System.out.println("  [3] - Wallet Payment");
        System.out.print(">>> Enter 1, 2, or 3: ");

        String paymentChoice = scanner.nextLine().trim();

        try {
            switch (paymentChoice) {
                case "1":
                    order.pay(new CashPayment());
                    System.out.println("✓ Cash payment completed successfully!");
                    break;

                case "2":
                    order.pay(new CardPayment());
                    System.out.println("✓ Card payment completed successfully!");
                    break;

                case "3":
                    order.pay(new WalletPayment());
                    System.out.println("✓ Wallet payment completed successfully!");
                    break;

                default:
                    System.out.println("❌ ERROR: Invalid payment method!");
                    System.out.println("   Please enter 1, 2, or 3");
            }
        } catch (Exception e) {
            System.out.println("❌ Payment failed: " + e.getMessage());
        }
    }
}