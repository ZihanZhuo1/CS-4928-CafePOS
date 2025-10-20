package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.domain.Product;
import com.cafepos.domain.Priced;

// God Class: mixes creation, pricing, discounts, tax, payment I/O, receipt formatting
public class OrderManagerGod {
    // Global/Static State: hard to test / shared mutable state
    public static int TAX_PERCENT = 10; // Primitive Obsession: tax as primitive
    public static String LAST_DISCOUNT_CODE = null; // Global/Static State

    // Long Method: multi-responsibility orchestration + I/O in one method
    public static String process(String recipe, int qty, String paymentType,
            String discountCode, boolean printReceipt) {
        ProductFactory factory = new ProductFactory(); // Hidden Dependency: not injected (DI missing)
        Product product = factory.create(recipe);

        Money unitPrice;
        try {
            var priced = product instanceof Priced p
                    ? p.price()
                    : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }

        if (qty <= 0)
            qty = 1; // Defensive logic embedded here (Long Method symptom)
        Money subtotal = unitPrice.multiply(qty);

        // Primitive Obsession: discountCode as String selector
        // Feature Envy / Shotgun Surgery risk: discount rules embedded inline
        Money discount = Money.zero();
        if (discountCode != null) {
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                // Duplicated Logic: inline BigDecimal percentage math
                discount = Money.of(subtotal.asBigDecimal()
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100)));
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                discount = Money.of(1.00); // Magic Number: fixed coupon amount
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode; // Global/Static State write
        }

        // Duplicated Logic: manual Money/BigDecimal manipulation
        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));
        if (discounted.asBigDecimal().signum() < 0)
            discounted = Money.zero();

        // Primitive Obsession: TAX_PERCENT primitive; not a TaxPolicy
        // Shotgun Surgery risk: tax rule hardcoded here
        // Duplicated Logic: percentage math repeated
        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT))
                .divide(java.math.BigDecimal.valueOf(100)));

        var total = discounted.add(tax);

        // Primitive Obsession: paymentType as String
        // Replace Conditional with Polymorphism (Strategy) candidate
        if (paymentType != null) {
            if (paymentType.equalsIgnoreCase("CASH")) {
                System.out.println("[Cash] Customer paid " + total + " EUR"); // I/O side effect in domain flow
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                System.out.println("[Card] Customer paid " + total +
                        " EUR with card ****1234"); // I/O side effect
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                System.out.println("[Wallet] Customer paid " + total +
                        " EUR via wallet user-wallet-789"); // I/O side effect
            } else {
                System.out.println("[UnknownPayment] " + total); // I/O side effect
            }
        }

        // Feature Envy (Presentation): receipt formatting belongs in a separate printer
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x")
                .append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        // Shotgun Surgery risk: printed tax label tied to TAX_PERCENT primitive
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ")
                .append(tax).append("\n");
        receipt.append("Total: ").append(total);
        String out = receipt.toString();

        if (printReceipt) {
            System.out.println(out); // I/O in domain method
        }

        return out;
    }
}
