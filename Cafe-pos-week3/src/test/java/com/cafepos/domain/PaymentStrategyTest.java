package com.cafepos.domain;
import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentStrategyTest {
    @Test
    void payment_strategy_called() {
        var order = new Order(42);
        order.addItem(new LineItem(new SimpleProduct("A", "A", Money.of(5)), 1));

        final boolean[] called = {false};
        PaymentStrategy fake = o -> called[0] = true;

        order.pay(fake);
        assertTrue(called[0], "Payment strategy should be called");
    }
}
