package com.cafepos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.cafepos.command.*;
import com.cafepos.domain.*;
import com.cafepos.printing.*;
import vendor.legacy.LegacyThermalPrinter;

class Week8Tests {

    @Test
    void testCommandUndo() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        service.addItem("ESP", 1);
        int sizeAfterAdd = order.items().size();
        assertEquals(1, sizeAfterAdd);

        service.removeLastItem();
        int sizeAfterUndo = order.items().size();
        assertEquals(0, sizeAfterUndo);
    }

    @Test
    void testRemotePress() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(2);

        remote.setSlot(0, new AddItemCommand(service, "LAT", 1));
        remote.press(0);

        assertEquals(1, order.items().size());
    }

    @Test
    void testMacroCommand() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        Command macro = new MacroCommand(
                new AddItemCommand(service, "ESP", 1),
                new AddItemCommand(service, "LAT", 1));

        macro.execute();
        assertEquals(2, order.items().size());

        macro.undo();
        assertEquals(0, order.items().size());
    }

    @Test
    void testAdapter() {

        TestLegacyPrinter testPrinter = new TestLegacyPrinter();

        Printer adapter = new LegacyPrinterAdapter(testPrinter);

        adapter.print("ABC");

        assertTrue(testPrinter.lastLen >= 3);
    }
}

class TestLegacyPrinter extends LegacyThermalPrinter {
    int lastLen = -1;

    @Override
    public void legacyPrint(byte[] payload) {
        lastLen = payload.length;
    }
}