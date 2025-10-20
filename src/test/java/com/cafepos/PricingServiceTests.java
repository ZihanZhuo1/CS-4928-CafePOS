package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PricingServiceTests {

    @Test
    void pricing_pipeline() {
        var pricing = new PricingService(
                new LoyaltyPercentDiscount(5),
                new FixedRateTaxPolicy(10));
        var pr = pricing.price(Money.of(7.80));

        assertEquals(Money.of(7.80), pr.subtotal());
        assertEquals(Money.of(0.39), pr.discount());
        assertEquals(Money.of(0.74), pr.tax());
        assertEquals(Money.of(8.15), pr.total());
    }

    @Test
    void pricing_with_no_discount() {
        var pricing = new PricingService(
                new NoDiscount(),
                new FixedRateTaxPolicy(10));
        var pr = pricing.price(Money.of(3.80));

        assertEquals(Money.of(3.80), pr.subtotal());
        assertEquals(Money.of(0.0), pr.discount());
        assertEquals(Money.of(0.38), pr.tax());
        assertEquals(Money.of(4.18), pr.total());
    }
}