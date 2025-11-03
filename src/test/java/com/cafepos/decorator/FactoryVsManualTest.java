package com.cafepos.decorator;

import com.cafepos.domain.*;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryVsManualTest {

    @Test
    void factory_and_manual_produce_identical_drinks() {
        Product viaFactory = new ProductFactory().create("ESP+SHOT+OAT+L");

        Product viaManual = new SizeLarge(
                new OatMilk(
                        new ExtraShot(
                                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
                        )
                )
        );

        assertEquals(viaFactory.name(), viaManual.name());

        assertEquals(((Priced) viaFactory).price(), ((Priced) viaManual).price());
    }

    @Test
    void factory_and_manual_produce_equal_order_totals() {
        Product viaFactory = new ProductFactory().create("ESP+SHOT+OAT+L");

        Product viaManual = new SizeLarge(
                new OatMilk(
                        new ExtraShot(
                                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
                        )
                )
        );

        Order orderFactory = new Order(1);
        orderFactory.addItem(new LineItem(viaFactory, 1));

        Order orderManual = new Order(2);
        orderManual.addItem(new LineItem(viaManual, 1));

        assertEquals(orderFactory.subtotal(), orderManual.subtotal());

        assertEquals(orderFactory.totalWithTax(10), orderManual.totalWithTax(10));
    }

    @Test
    void factory_and_manual_latte_are_identical() {
        Product viaFactory = new ProductFactory().create("LAT+L");

        Product viaManual = new SizeLarge(
                new SimpleProduct("P-LAT", "Latte", Money.of(3.20))
        );

        assertEquals(viaFactory.name(), viaManual.name());
        assertEquals(((Priced) viaFactory).price(), ((Priced) viaManual).price());
    }

    @Test
    void factory_and_manual_complex_drink_are_identical() {
        Product viaFactory = new ProductFactory().create("CAP+SYP+SHOT+L");

        Product viaManual = new SizeLarge(
                new ExtraShot(
                        new Syrup(
                                new SimpleProduct("P-CAP", "Cappuccino", Money.of(3.00))
                        )
                )
        );

        assertEquals(viaFactory.name(), viaManual.name());
        assertEquals(((Priced) viaFactory).price(), ((Priced) viaManual).price());

        assertEquals(Money.of(4.90), ((Priced) viaFactory).price());
    }
}
