package com.cafepos.domain;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTests {
    @Test
    void addition_works() {
        Money m1 = Money.of(2.50);
        Money m2 = Money.of(3.50);
        assertEquals(Money.of(6.00), m1.add(m2));
    }

    @Test
    void multiplication_works() {
        Money m = Money.of(2.50);
        assertEquals(Money.of(7.50), m.multiply(3));
    }
}


