package com.cafepos.demo;

import com.cafepos.command.*;
import com.cafepos.domain.*;
import com.cafepos.payment.*;
import java.util.Scanner;

public final class Week8InteractiveDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(5);

        System.out.println("=== Week 8: Command Pattern Interactive Demo ===\n");

        // 预设按钮
        remote.setSlot(0, new AddItemCommand(service, "ESP+SHOT", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT+L", 2));
        remote.setSlot(2, new AddItemCommand(service, "CAP", 1));
        remote.setSlot(3, new PayOrderCommand(service, new CardPayment("1234567890123456"), 10));

        System.out.println("Available buttons:");
        System.out.println("  [0] Add Espresso + Extra Shot x1");
        System.out.println("  [1] Add Latte (Large) x2");
        System.out.println("  [2] Add Cappuccino x1");
        System.out.println("  [3] Pay with Card (10% tax)");
        System.out.println("  [U] Undo last command");
        System.out.println("  [Q] Quit\n");

        while (true) {
            System.out.print("Press button (0-3, U, Q): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Q")) {
                System.out.println("\nGoodbye!");
                break;
            } else if (input.equals("U")) {
                remote.undo();
            } else {
                try {
                    int slot = Integer.parseInt(input);
                    if (slot >= 0 && slot <= 3) {
                        remote.press(slot);
                    } else {
                        System.out.println("Invalid button! Use 0-3.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Use 0-3, U, or Q.");
                }
            }
            System.out.println();
        }

        scanner.close();
    }
}