package com.cafepos.menu;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompositeIteratorTests {

	@Test
	void depth_first_iteration_collects_all_nodes() {
		Menu root = new Menu("ROOT");
		Menu a = new Menu("A");
		Menu b = new Menu("B");
		root.add(a);
		root.add(b);
		a.add(new MenuItem("x", Money.of(1.0), true));
		b.add(new MenuItem("y", Money.of(2.0), false));

		List<String> names = root.allItems().stream()
			.map(MenuComponent::name)
			.toList();

		assertTrue(names.contains("x"));
		assertTrue(names.contains("y"));
	}

	@Test
	void vegetarian_items_filters_correctly() {
		Menu root = new Menu("MENU");
		root.add(new MenuItem("Veg1", Money.of(1.0), true));
		root.add(new MenuItem("NonVeg", Money.of(2.0), false));
		root.add(new MenuItem("Veg2", Money.of(3.0), true));

		List<MenuItem> vegItems = root.vegetarianItems();

		assertEquals(2, vegItems.size());
		assertTrue(vegItems.stream().allMatch(MenuItem::vegetarian));
	}

	@Test
	void nested_menu_traversal() {
		Menu root = new Menu("ROOT");
		Menu drinks = new Menu("DRINKS");
		Menu coffee = new Menu("COFFEE");

		coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
		coffee.add(new MenuItem("Latte", Money.of(3.90), true));
		drinks.add(coffee);
		root.add(drinks);

		List<MenuComponent> allItems = root.allItems();

		// Should contain: DRINKS, COFFEE, Espresso, Latte
		assertTrue(allItems.stream().anyMatch(mc -> mc.name().equals("Espresso")));
		assertTrue(allItems.stream().anyMatch(mc -> mc.name().equals("Latte")));
		assertEquals(4, allItems.size());
	}

	@Test
	void empty_menu_iterator() {
		Menu empty = new Menu("EMPTY");

		List<MenuComponent> items = empty.allItems();

		assertEquals(0, items.size()); // only the menu itself has no children
	}

	@Test
	void menu_item_empty_iterator() {
		MenuItem item = new MenuItem("Item", Money.of(1.0), true);

		assertFalse(item.iterator().hasNext());
	}
}
