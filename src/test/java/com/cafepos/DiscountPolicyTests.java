package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountPolicyTests {

    @Test
    void loyalty_discount_5_percent() {
        DiscountPolicy d = new LoyaltyPercentDiscount(5);
        assertEquals(Money.of(0.39), d.discountOf(Money.of(7.80)));
    }

    @Test
    void no_discount() {
        DiscountPolicy d = new NoDiscount();
        assertEquals(Money.of(0.0), d.discountOf(Money.of(10.00)));
    }

    @Test
    void fixed_coupon_discount() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(1.00));
        assertEquals(Money.of(1.00), d.discountOf(Money.of(5.00)));
    }

    @Test
    void fixed_coupon_capped_at_subtotal() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(10.00));
        assertEquals(Money.of(3.30), d.discountOf(Money.of(3.30)));
    }
}