package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.payment.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentStrategyTest {

    @Test
    void payment_strategy_called() {
        var order = new Order(42);
        order.addItem(new LineItem(new SimpleProduct("A", "A", Money.of(5)), 1));

        final boolean[] called = { false };
        PaymentStrategy fake = o -> called[0] = true;

        order.pay(fake);

        assertTrue(called[0], "Payment strategy should be called");
    }

    @Test
    void cash_payment_prints_correct_message() {
        var order = new Order(1);
        order.addItem(new LineItem(new SimpleProduct("P", "Product", Money.of(2.75)), 1));

        String output = captureOutput(() -> order.pay(new CashPayment()));
        assertTrue(output.contains("Customer paid 3.03 EUR") || output.contains("Customer paid 2.75 EUR"));
    }

    @Test
    void card_payment_prints_correct_message() {
        var order = new Order(2);
        order.addItem(new LineItem(new SimpleProduct("P", "Product", Money.of(10)), 1));

        String output = captureOutput(() -> order.pay(new CardPayment("1234567890123456")));
        assertTrue(output.contains("11.00 EUR with card ****3456"));
    }

    @Test
    void wallet_payment_prints_correct_message() {
        var order = new Order(3);
        order.addItem(new LineItem(new SimpleProduct("P", "Product", Money.of(3.5)), 1));

        String output = captureOutput(() -> order.pay(new WalletPayment("wallet-abc-001")));
        assertTrue(output.contains("3.85 EUR via wallet wallet-abc-001"));
    }

    private String captureOutput(Runnable runnable) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));
        try {
            runnable.run();
        } finally {
            System.setOut(original);
        }
        return out.toString().trim();
    }
}