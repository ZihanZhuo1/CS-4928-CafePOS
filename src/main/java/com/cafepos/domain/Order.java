package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.observer.OrderObserver;

import java.util.*;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) { this.id = id; }

    public long id() { return id; }
    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public void register(OrderObserver o) {
        if (o == null) throw new IllegalArgumentException("observer needed");
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    private void notifyObservers(String eventType) {
        for (OrderObserver observer : observers) {
            observer.updated(this, eventType);
        }
    }

    public void addItem(LineItem li) {
        if (li == null) throw new IllegalArgumentException("line item needed");
        items.add(li);
        notifyObservers("itemAdded");
    }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        double rate = percent / 100.0;
        return subtotal().multiply(rate);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }
    public void pay(PaymentStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers("paid");
    }

    public void markReady() {
        notifyObservers("ready");
    }

}
