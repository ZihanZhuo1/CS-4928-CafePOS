# Order State Transition Table

| State      | pay            | prepare            | markReady          | deliver            | cancel            |
|------------|----------------|--------------------|--------------------|--------------------|-------------------|
| NEW        | ✓ → PREPARING  | ✗                  | ✗                  | ✗                  | ✓ → CANCELLED     |
| PREPARING  | ✗              | ✗                  | ✓ → READY          | ✗                  | ✓ → CANCELLED     |
| READY      | ✗              | ✗                  | ✗                  | ✓ → DELIVERED      | ✗                 |
| DELIVERED  | ✗              | ✗                  | ✗                  | ✗                  | ✗                 |
| CANCELLED  | ✗              | ✗                  | ✗                  | ✗                  | ✗                 |

## Legend
- **✓ → STATE** = Valid transition (moves to STATE)
- **✗** = Invalid transition (stays in current state)

## Explanation

### NEW State
- **pay()**: Valid → transitions to PREPARING state (order has been paid for)
- **prepare()**: Invalid → cannot prepare without payment
- **markReady()**: Invalid → must be in PREPARING first
- **deliver()**: Invalid → must be ready first
- **cancel()**: Valid → can cancel a new order

### PREPARING State
- **pay()**: Invalid → already paid
- **prepare()**: Invalid → idempotent, stays preparing
- **markReady()**: Valid → transitions to READY state (preparation complete)
- **deliver()**: Invalid → must be ready first
- **cancel()**: Valid → can cancel during preparation

### READY State
- **pay()**: Invalid → already paid
- **prepare()**: Invalid → already prepared
- **markReady()**: Invalid → already ready
- **deliver()**: Valid → transitions to DELIVERED state (order delivered)
- **cancel()**: Invalid → cannot cancel after order is ready

### DELIVERED State
- **pay()**: Invalid → order complete
- **prepare()**: Invalid → order complete
- **markReady()**: Invalid → order complete
- **deliver()**: Invalid → already delivered
- **cancel()**: Invalid → order complete

### CANCELLED State
- **pay()**: Invalid → order cancelled
- **prepare()**: Invalid → order cancelled
- **markReady()**: Invalid → order cancelled
- **deliver()**: Invalid → order cancelled
- **cancel()**: Invalid → already cancelled
