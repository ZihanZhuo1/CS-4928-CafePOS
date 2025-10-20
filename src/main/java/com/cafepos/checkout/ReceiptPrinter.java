package com.cafepos.checkout;

import com.cafepos.common.Money;

public final class ReceiptPrinter {

    public String format(String recipe, int qty, Money subtotal,
            Money discount, Money tax, Money total, int taxPercent) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x")
                .append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");

        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }

        receipt.append("Tax (").append(taxPercent).append("%): ")
                .append(tax).append("\n");
        receipt.append("Total: ").append(total);

        return receipt.toString();
    }
}