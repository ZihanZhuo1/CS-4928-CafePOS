package com.cafepos.domain;

import com.cafepos.common.Money;

public final class SimpleProduct implements Product, Priced {
    private final String id;
    private final String name;
    private final Money basePrice;

    public SimpleProduct(String id, String name, Money basePrice) {
        if (basePrice == null) throw new IllegalArgumentException("price required");
        if (basePrice.compareTo(Money.zero()) < 0) throw new IllegalArgumentException("price must be >= 0");
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override public String id() { return id; }
    @Override public String name() { return name; }
    @Override public Money basePrice() { return basePrice; }

    @Override
    public Money price() {
        return basePrice;
    }
}

