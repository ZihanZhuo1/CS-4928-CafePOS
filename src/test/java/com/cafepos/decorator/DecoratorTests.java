package com.cafepos.decorator;

import com.cafepos.domain.*;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTests {

    @Test
    void decorator_single_addon() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);

        assertEquals("Espresso + Extra Shot", withShot.name());
        assertEquals(Money.of(3.30), ((Priced) withShot).price());
    }

    @Test
    void decorator_stacks() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        assertEquals(Money.of(4.50), ((Priced) decorated).price());
    }

    @Test
    void factory_parses_recipe() {
        ProductFactory f = new ProductFactory();
        Product p = f.create("ESP+SHOT+OAT");

        assertTrue(p.name().contains("Espresso") && p.name().contains("Oat Milk"));
    }

    @Test
    void order_uses_decorated_price() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso); // 3.30

        Order o = new Order(1);
        o.addItem(new LineItem(withShot, 2));

        assertEquals(Money.of(6.60), o.subtotal());
    }

    @Test
    void oatMilk_decorator_adds_correct_surcharge() {
        Product latte = new SimpleProduct("P-LAT", "Latte", Money.of(3.20));
        Product withOatMilk = new OatMilk(latte);

        assertEquals("Latte + Oat Milk", withOatMilk.name());
        assertEquals(Money.of(3.70), ((Priced) withOatMilk).price());
    }

    @Test
    void syrup_decorator_adds_correct_surcharge() {
        Product cappuccino = new SimpleProduct("P-CAP", "Cappuccino", Money.of(3.00));
        Product withSyrup = new Syrup(cappuccino);

        assertEquals("Cappuccino + Syrup", withSyrup.name());
        assertEquals(Money.of(3.40), ((Priced) withSyrup).price());
    }

    @Test
    void sizeLarge_decorator_adds_correct_surcharge() {
        Product latte = new SimpleProduct("P-LAT", "Latte", Money.of(3.20));
        Product large = new SizeLarge(latte);

        assertEquals("Latte (Large)", large.name());
        assertEquals(Money.of(3.90), ((Priced) large).price());
    }

    @Test
    void multiple_decorators_price_is_commutative() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));

        Product order1 = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        Product order2 = new ExtraShot(new SizeLarge(new OatMilk(espresso)));

        assertEquals(((Priced) order1).price(), ((Priced) order2).price());
    }

    @Test
    void factory_creates_large_latte() {
        ProductFactory f = new ProductFactory();
        Product p = f.create("LAT+L");

        assertTrue(p.name().contains("Latte"));
        assertTrue(p.name().contains("Large"));
        assertEquals(Money.of(3.90), ((Priced) p).price());
    }

    @Test
    void factory_rejects_invalid_base() {
        ProductFactory f = new ProductFactory();

        assertThrows(IllegalArgumentException.class, () -> f.create("INVALID"));
    }

    @Test
    void factory_rejects_invalid_addon() {
        ProductFactory f = new ProductFactory();

        assertThrows(IllegalArgumentException.class, () -> f.create("ESP+INVALID"));
    }

    @Test
    void factory_handles_case_insensitive_input() {
        ProductFactory f = new ProductFactory();
        Product p1 = f.create("esp+shot+oat");
        Product p2 = f.create("ESP+SHOT+OAT");

        assertEquals(p1.name(), p2.name());
        assertEquals(((Priced) p1).price(), ((Priced) p2).price());
    }
}
