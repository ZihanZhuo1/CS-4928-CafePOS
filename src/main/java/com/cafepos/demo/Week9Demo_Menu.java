package com.cafepos.demo;

import com.cafepos.menu.*;
import com.cafepos.common.Money;

public final class Week9Demo_Menu {
	public static void main(String[] args) {
		// Build a hierarchical menu structure
		Menu root = new Menu("CAFÉ MENU");
		Menu drinks = new Menu(" Drinks ");
		Menu coffee = new Menu(" Coffee ");
		Menu desserts = new Menu(" Desserts ");

		coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
		coffee.add(new MenuItem("Latte (Large)", Money.of(3.90), true));
		drinks.add(coffee);

		desserts.add(new MenuItem("Cheesecake", Money.of(3.50), false));
		desserts.add(new MenuItem("Oat Cookie", Money.of(1.20), true));

		root.add(drinks);
		root.add(desserts);

		// Part 1: Print entire menu (demonstrates Composite + uniform API)
		System.out.println("========================================");
		System.out.println("        FULL CAFÉ MENU (Composite)");
		System.out.println("========================================");
		root.print();

		// Part 2: List vegetarian items only (demonstrates Iterator + filtering)
		System.out.println("\n========================================");
		System.out.println("      VEGETARIAN OPTIONS (Filtered)");
		System.out.println("========================================");
		var vegItems = root.vegetarianItems();
		for (MenuItem mi : vegItems) {
			System.out.println(" * " + mi.name() + " = " + mi.price());
		}

		// Part 3: Traverse all items depth-first (demonstrates CompositeIterator)
		System.out.println("\n========================================");
		System.out.println("   ALL ITEMS VIA ITERATOR (Depth-First)");
		System.out.println("========================================");
		var allItems = root.allItems();
		int count = 0;
		for (MenuComponent mc : allItems) {
			System.out.println((++count) + ". " + mc.name());
		}
	}
}
