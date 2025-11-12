package com.cafepos.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatePatternTests {

	@Test
	void order_fsm_starts_in_new_state() {
		OrderFSM fsm = new OrderFSM();
		assertEquals("NEW", fsm.status());
	}

	@Test
	void order_fsm_happy_path() {
		OrderFSM fsm = new OrderFSM();
		assertEquals("NEW", fsm.status());

		fsm.pay();
		assertEquals("PREPARING", fsm.status());

		fsm.markReady();
		assertEquals("READY", fsm.status());

		fsm.deliver();
		assertEquals("DELIVERED", fsm.status());
	}

	@Test
	void new_state_cannot_prepare_before_pay() {
		OrderFSM fsm = new OrderFSM();
		fsm.prepare(); // should print rejection
		assertEquals("NEW", fsm.status()); // stays in NEW
	}

	@Test
	void new_state_can_cancel() {
		OrderFSM fsm = new OrderFSM();
		fsm.cancel();
		assertEquals("CANCELLED", fsm.status());
	}

	@Test
	void preparing_state_cannot_deliver() {
		OrderFSM fsm = new OrderFSM();
		fsm.pay();
		fsm.deliver(); // should be rejected
		assertEquals("PREPARING", fsm.status()); // stays in PREPARING
	}

	@Test
	void preparing_state_can_cancel() {
		OrderFSM fsm = new OrderFSM();
		fsm.pay();
		fsm.cancel();
		assertEquals("CANCELLED", fsm.status());
	}

	@Test
	void ready_state_cannot_cancel() {
		OrderFSM fsm = new OrderFSM();
		fsm.pay();
		fsm.markReady();
		fsm.cancel(); // should be rejected
		assertEquals("READY", fsm.status()); // stays in READY
	}

	@Test
	void delivered_state_is_terminal() {
		OrderFSM fsm = new OrderFSM();
		fsm.pay();
		fsm.markReady();
		fsm.deliver();
		fsm.cancel(); // should do nothing
		assertEquals("DELIVERED", fsm.status());
	}

	@Test
	void cancelled_state_is_terminal() {
		OrderFSM fsm = new OrderFSM();
		fsm.cancel();
		fsm.pay(); // should do nothing
		assertEquals("CANCELLED", fsm.status());
	}
}
