package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxPolicyTests {

    @Test
    void fixed_rate_tax_10_percent() {
        TaxPolicy t = new FixedRateTaxPolicy(10);
        assertEquals(Money.of(0.74), t.taxOn(Money.of(7.41)));
    }

    @Test
    void fixed_rate_tax_0_percent() {
        TaxPolicy t = new FixedRateTaxPolicy(0);
        assertEquals(Money.of(0.0), t.taxOn(Money.of(10.00)));
    }
}