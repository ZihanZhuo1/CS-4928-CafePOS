package com.cafepos.demo;

import com.cafepos.domain.*;
import com.cafepos.payment.*;
import com.cafepos.command.*;
import java.util.Scanner;

public final class Week8Demo_Commands {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to Café POS System (Week 8 - Command Pattern) ===\n");

        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        PosRemote remote = new PosRemote(6);
        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT", 1));
        remote.setSlot(2, new AddItemCommand(service, "CAP", 1));

        // MacroCommand - bundles multiple commands into one button press
        Command espressoCmd = new AddItemCommand(service, "ESP", 1);
        Command latteCmd = new AddItemCommand(service, "LAT", 1);
        remote.setSlot(3, new MacroCommand(espressoCmd, latteCmd));

        remote.setSlot(4, new PayOrderCommand(service, new CardPayment("1234567890123456"), 10));
        remote.setSlot(5, new PayOrderCommand(service, new CashPayment(), 10));

        boolean continueOrdering = true;

        while (continueOrdering) {
            System.out.println("\n========================================");
            System.out.println("      CAFÉ POS - COMMAND REMOTE");
            System.out.println("========================================");
            System.out.println("Command Buttons (Invoker → Command → Receiver):");
            System.out.println("  [0] - Add Espresso         ($2.50)");
            System.out.println("  [1] - Add Latte            ($3.20)");
            System.out.println("  [2] - Add Cappuccino       ($3.00)");
            System.out.println("  [3] - Add Combo      (ESP + LAT)"); // MacroCommand
            System.out.println("  [4] - Pay with Card");
            System.out.println("  [5] - Pay with Cash");
            System.out.println("\nSpecial Actions:");
            System.out.println("  [u] - Undo last command (reverses last action)");
            System.out.println("  [v] - View current order");
            System.out.println("  [q] - Quit system");
            System.out.println("========================================");
            System.out.print(">>> Enter 0-5, u, v, or q: ");

            String choice = scanner.nextLine().trim().toLowerCase();
            System.out.println();

            switch (choice) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    pressButton(remote, Integer.parseInt(choice));
                    break;

                case "u":
                    undoLastCommand(remote);
                    break;

                case "v":
                    viewOrder(order);
                    break;

                case "q":
                    continueOrdering = false;
                    System.out.println("\n========================================");
                    System.out.println("  Thank you for using Café POS System!");
                    System.out.println("========================================");
                    break;

                default:
                    System.out.println("\n ERROR: Invalid input!");
                    System.out.println("   Please type: 0-5, u, v, or q");
            }
        }

        scanner.close();
    }

    private static void pressButton(PosRemote remote, int slot) {
        System.out.println("========================================");
        System.out.println("  INVOKER: Pressing button [" + slot + "]...");
        System.out.println("========================================");
        remote.press(slot);
        System.out.println("\n✓ Command executed successfully!");
    }

    private static void undoLastCommand(PosRemote remote) {
        System.out.println("========================================");
        System.out.println("  UNDO: Reversing last command...");
        System.out.println("========================================");
        remote.undo();
        System.out.println("\n✓ Undo completed!");
    }

    private static void viewOrder(Order order) {
        System.out.println("\n========================================");
        System.out.println(" CURRENT ORDER");
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
}
