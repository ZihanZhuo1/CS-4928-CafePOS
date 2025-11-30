package com.cafepos.demo;

import com.cafepos.printing.*;
import vendor.legacy.LegacyThermalPrinter;
import java.util.Scanner;

public final class Week8Demo_Adapter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Printer printer = new LegacyPrinterAdapter(new LegacyThermalPrinter());

        System.out.println("=== Adapter Pattern Demo - Legacy Printer ===");
        System.out.println("This demonstrates the Adapter Pattern.");
        System.out.println("Our system speaks 'String', but the legacy printer speaks 'byte[]'.");
        System.out.println("The adapter translates between them!\n");

        while (true) {
            System.out.println("\n--- Printer Menu ---");
            System.out.println("[1] Print sample receipt #1");
            System.out.println("[2] Print sample receipt #2");
            System.out.println("[3] Print custom text");
            System.out.println("[q] Quit");
            System.out.print("\nYour choice: ");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("q")) {
                System.out.println("\nThank you for testing the Adapter Pattern demo!");
                break;
            } else if (input.equals("1")) {
                String receipt = "Order #1001\n" +
                        "Latte (Large) x2\n" +
                        "Subtotal: 7.80\n" +
                        "Tax (10%): 0.78\n" +
                        "Total: 8.58";
                System.out.println("\n--- Printing Receipt ---");
                printer.print(receipt);
                System.out.println("[Demo] Receipt sent via adapter successfully!");
            } else if (input.equals("2")) {
                String receipt = "Order #1002\n" +
                        "Espresso x1\n" +
                        "Cappuccino x2\n" +
                        "Subtotal: 8.50\n" +
                        "Tax (10%): 0.85\n" +
                        "Total: 9.35";
                System.out.println("\n--- Printing Receipt ---");
                printer.print(receipt);
                System.out.println("[Demo] Receipt sent via adapter successfully!");
            } else if (input.equals("3")) {
                System.out.print("\nEnter text to print: ");
                String customText = scanner.nextLine();
                System.out.println("\n--- Printing Custom Text ---");
                printer.print(customText);
                System.out.println("[Demo] Custom text sent via adapter successfully!");
            } else {
                System.out.println("Invalid choice. Use 1, 2, 3, or q.");
            }
        }
        scanner.close();
    }
}
