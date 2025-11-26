package com.cafepos.demo;

import com.cafepos.infra.Wiring;
import com.cafepos.ui.ConsoleView;
import com.cafepos.ui.OrderController;

public final class Week10DemoMVC {
    public static void main(String[] args) {
        var c = Wiring.createDefault();

        var controller = new OrderController(c.repo(), c.checkout());
        var view = new ConsoleView();

        long id = 4101L;
        controller.createOrder(id);
        controller.addItem(id, "ESP+SHOT+OAT", 1); // Espresso + Extra Shot + Oat Milk
        controller.addItem(id, "LAT+L", 2); // Latte Large x2

        String receipt = controller.checkout(id, 10);
        view.print(receipt);
    }
}