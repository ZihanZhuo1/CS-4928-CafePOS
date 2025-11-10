package com.cafepos.demo;

import com.cafepos.state.OrderFSM;

public final class Week9Demo_State {
	public static void main(String[] args) {
		OrderFSM fsm = new OrderFSM();

		System.out.println("========================================");
		System.out.println("     ORDER LIFECYCLE - STATE DEMO");
		System.out.println("========================================");
		System.out.println("Status = " + fsm.status());
		System.out.println();

		// Attempt invalid transition (prepare before pay)
		System.out.println(">>> Attempting to prepare (invalid - should reject)...");
		fsm.prepare();
		System.out.println();

		// Happy path: NEW → PREPARING
		System.out.println(">>> Attempting to pay...");
		fsm.pay();
		System.out.println("Status = " + fsm.status());
		System.out.println();

		// PREPARING → READY
		System.out.println(">>> Attempting to prepare (still preparing)...");
		fsm.prepare();
		System.out.println();

		System.out.println(">>> Attempting to mark ready...");
		fsm.markReady();
		System.out.println("Status = " + fsm.status());
		System.out.println();

		// READY → DELIVERED
		System.out.println(">>> Attempting to deliver...");
		fsm.deliver();
		System.out.println("Status = " + fsm.status());
		System.out.println();

		System.out.println("========================================");
		System.out.println("Order lifecycle complete!");
		System.out.println("Final Status: " + fsm.status());
		System.out.println("========================================");
	}
}
