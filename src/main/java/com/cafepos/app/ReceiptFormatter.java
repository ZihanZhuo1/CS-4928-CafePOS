package com.cafepos.app;

import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.pricing.PricingService.PricingResult;

public final class ReceiptFormatter {

    public String format(long id, Order order, PricingResult pr) {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(id).append("\n");

        for (LineItem li : order.items()) {
            sb.append("  - ").append(li.product().name())
                    .append(" x").append(li.quantity())
                    .append(" = ").append(li.lineTotal()).append("\n");
        }

        sb.append("Subtotal: ").append(pr.subtotal()).append("\n");

        if (pr.discount().asBigDecimal().signum() > 0) {
            sb.append("Discount: -").append(pr.discount()).append("\n");
        }

        sb.append("Tax: ").append(pr.tax()).append("\n");
        sb.append("Total: ").append(pr.total());

        return sb.toString();
    }
}