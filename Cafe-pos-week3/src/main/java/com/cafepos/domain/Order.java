package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;

import java.util.*;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();

    public Order(long id) { this.id = id; }

    public long id() { return id; }
    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public void addItem(LineItem li) {
        if (li == null) throw new IllegalArgumentException("line item required");
        items.add(li);
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
}

}
