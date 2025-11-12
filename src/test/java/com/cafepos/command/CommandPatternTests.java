package com.cafepos.command;

import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.payment.CardPayment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandPatternTests {

    @Test
    void addItemCommand_executesAndAddsItem() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        AddItemCommand cmd = new AddItemCommand(service, "ESP", 1);
        cmd.execute();

        assertEquals(1, order.items().size());
    }

    @Test
    void addItemCommand_undoRemovesLastItem() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        AddItemCommand cmd = new AddItemCommand(service, "LAT", 2);
        cmd.execute();
        assertEquals(1, order.items().size());

        cmd.undo();
        assertEquals(0, order.items().size());
    }

    @Test
    void posRemote_pressesCommandInSlot() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(2);

        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));
        remote.press(0);

        assertEquals(1, order.items().size());
    }

    @Test
    void posRemote_undoReversesLastCommand() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(2);

        remote.setSlot(0, new AddItemCommand(service, "CAP", 1));
        remote.press(0);
        assertEquals(1, order.items().size());

        remote.undo();
        assertEquals(0, order.items().size());
    }

    @Test
    void macroCommand_executesMultipleCommands() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        Command cmd1 = new AddItemCommand(service, "ESP", 1);
        Command cmd2 = new AddItemCommand(service, "LAT", 1);
        MacroCommand macro = new MacroCommand(cmd1, cmd2);

        macro.execute();

        assertEquals(2, order.items().size());
    }

    @Test
    void macroCommand_undoReversesInReverseOrder() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        Command cmd1 = new AddItemCommand(service, "ESP", 1);
        Command cmd2 = new AddItemCommand(service, "LAT", 1);
        MacroCommand macro = new MacroCommand(cmd1, cmd2);

        macro.execute();
        assertEquals(2, order.items().size());

        macro.undo();
        assertEquals(0, order.items().size());
    }

    @Test
    void payOrderCommand_executesPayment() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);

        service.addItem("ESP", 1);

        PayOrderCommand payCmd = new PayOrderCommand(service, new CardPayment(), 10);
        assertDoesNotThrow(() -> payCmd.execute());
    }

    @Test
    void posRemote_integrationTest() {
        Order order = new Order(OrderIds.next());
        OrderService service = new OrderService(order);
        PosRemote remote = new PosRemote(3);

        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT", 2));
        remote.setSlot(2, new PayOrderCommand(service, new CardPayment(), 10));

        remote.press(0);
        remote.press(1);
        assertEquals(2, order.items().size());

        remote.undo();
        assertEquals(1, order.items().size());

        remote.press(1);
        assertEquals(2, order.items().size());

        assertDoesNotThrow(() -> remote.press(2));
    }
}
