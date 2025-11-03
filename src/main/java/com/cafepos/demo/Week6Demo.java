package com.cafepos.demo;

import com.cafepos.checkout.CheckoutService;
import com.cafepos.checkout.ReceiptPrinter;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.smells.OrderManagerGod;
import com.cafepos.common.Money;

import java.util.Scanner;

public final class Week6Demo {
        public static void main(String[] args) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("╔════════════════════════════════════════════════════╗");
                System.out.println("║   Week 6 Lab - Refactoring Demonstration          ║");
                System.out.println("║   Old (Smelly) vs New (Clean) Implementation      ║");
                System.out.println("╚════════════════════════════════════════════════════╝");

                // Get user input
                System.out.print("\n Enter recipe (e.g., LAT+L, ESP+SHOT+OAT): ");
                String recipe = scanner.nextLine().trim();

                System.out.print(" Enter quantity: ");
                int qty = Integer.parseInt(scanner.nextLine().trim());

                System.out.print(" Enter payment type (CASH/CARD/WALLET): ");
                String paymentType = scanner.nextLine().trim();

                System.out.print(" Enter discount code (LOYAL5/COUPON1/NONE): ");
                String discountCode = scanner.nextLine().trim();

                System.out.println("\n" + "━".repeat(60));

                // Old implementation (smelly code)
                System.out.println("\n Old implementation (Smelly Code)");
                System.out.println("   - 100+ line God Class method");
                System.out.println("   - Global static state");
                System.out.println("   - String-based conditionals\n");
                String oldReceipt = OrderManagerGod.process(recipe, qty,
                                paymentType, discountCode, false);

                // New implementation (clean code)
                System.out.println("\n New implementation (Clean Code)");
                System.out.println("   Strategy Pattern for discounts & tax");
                System.out.println("   Dependency Injection");
                System.out.println("   SOLID Principles\n");

                // Choose discount policy based on code
                DiscountPolicy discountPolicy = switch (discountCode.toUpperCase()) {
                        case "LOYAL5" -> new LoyaltyPercentDiscount(5);
                        case "COUPON1" -> new FixedCouponDiscount(Money.of(1.00));
                        default -> new NoDiscount();
                };

                var pricing = new PricingService(discountPolicy, new FixedRateTaxPolicy(10));
                var printer = new ReceiptPrinter();
                var checkout = new CheckoutService(new ProductFactory(), pricing, printer, 10);
                String newReceipt = checkout.checkout(recipe, qty);

                // Comparison
                System.out.println("\n" + "━".repeat(60));
                System.out.println("\n Comparation\n");

                System.out.println("Old receipt:");
                System.out.println(oldReceipt);

                System.out.println("\n New receipt:");
                System.out.println(newReceipt);

                System.out.println("\n" + "━".repeat(60));
                boolean match = oldReceipt.equals(newReceipt);
                System.out.println(match
                                ? "\n RESULT: Match = true (Refactoring successful!)"
                                : "\n RESULT: Match = false (Behavior changed!)");
                System.out.println("━".repeat(60) + "\n");

                scanner.close();
        }
}