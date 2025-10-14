package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.observer.OrderObserver;
import com.cafepos.payment.CashPayment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverPatternTest {

    @Test
    void observers_notified_on_item_add() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(1);
        o.addItem(new LineItem(p, 1)); // baseline

        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));

        o.addItem(new LineItem(p, 1));
        assertTrue(events.contains("itemAdded"));
    }

    @Test
    void observers_notified_on_payment() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(2);
        o.addItem(new LineItem(p, 1));

        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));

        o.pay(new CashPayment());
        assertTrue(events.contains("paid"));
    }

    @Test
    void observers_notified_on_ready() {
        var o = new Order(3);

        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));

        o.markReady();
        assertTrue(events.contains("ready"));
    }

    @Test
    void multiple_observers_receive_same_event() {
        var o = new Order(4);

        List<String> events1 = new ArrayList<>();
        List<String> events2 = new ArrayList<>();

        OrderObserver observer1 = (order, evt) -> events1.add(evt);
        OrderObserver observer2 = (order, evt) -> events2.add(evt);

        o.register(observer1);
        o.register(observer2);

        o.markReady();

        assertTrue(events1.contains("ready"));
        assertTrue(events2.contains("ready"));
    }

    @Test
    void observer_can_be_unregistered() {
        var o = new Order(5);

        List<String> events = new ArrayList<>();
        OrderObserver observer = (order, evt) -> events.add(evt);

        o.register(observer);
        o.unregister(observer);

        o.markReady();

        assertFalse(events.contains("ready"));
    }

    @Test
    void null_observer_throws_exception() {
        var o = new Order(6);
        assertThrows(IllegalArgumentException.class, () -> o.register(null));
    }

    @Test
    void duplicate_observer_not_added_twice() {
        var o = new Order(7);

        List<String> events = new ArrayList<>();
        OrderObserver observer = (order, evt) -> events.add(evt);

        o.register(observer);
        o.register(observer);

        o.markReady();

        assertEquals(1, events.size());
    }
}
