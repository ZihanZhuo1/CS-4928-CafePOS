# ADR-002: EventBus for Component Communication

## Status
Accepted

## Context
When an order is created or payment is made, multiple parts of the system need to respond (user interface updates, notifications, logging). Directly calling methods can lead to overly tight coupling between components.

## Decision
We introduced an in-process EventBus as a connector between components. Components publish domain events (order creation, order payment), and interested subscribers can receive these events.

## Alternatives Considered

1. **Direct method calls**: The controller directly calls all related components, which leads to high coupling and violates the open/closed principle.

2. **Observer pattern on Order**: This tightly couples the observer to the Order class.

3. **External message queue (Kafka/RabbitMQ)**:
Over-designing, which is not suitable for the current system scale, will increase the complexity of the infrastructure.

## Consequences

### Pros
- Low coupling between publishers and subscribers.
- New subscribers can be easily added without modifying existing code, offering high flexibility.
- Preparing for future data extraction from microservices.
- Can be tested in isolation.

### Cons
- It is more difficult to trace the event flow during debugging.
- Events are stored only in memory and are easily lost.
- No guaranteed delivery order.