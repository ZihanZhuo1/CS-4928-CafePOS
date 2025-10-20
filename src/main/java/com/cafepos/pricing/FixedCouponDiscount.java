package com.cafepos.pricing;

import com.cafepos.common.Money;

public final class FixedCouponDiscount implements DiscountPolicy {
    private final Money amount;

    public FixedCouponDiscount(Money amount) {
        this.amount = amount;
    }

    @Override
    public Money discountOf(Money subtotal) {
        // 折扣不能超过小计金额
        if (amount.asBigDecimal().compareTo(subtotal.asBigDecimal()) > 0)
            return subtotal;
        return amount;
    }
}