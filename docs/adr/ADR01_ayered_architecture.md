# ADR-001: Layered Monolith Architecture

## Status
Accepted

## Context
The Café POS & Delivery system initially started as a small project, covering a range of aspects. As functionality increased (payment, receipts, menus, orders), the maintenance and testing processes for the codebase also became increasingly complex. At this point, we needed to add a voice-based architecture.

## Decision
We chose a Layered Monolith architecture with four layers:

- **Presentation (UI)**: Controllers and Views handling console I/O
- **Application**: Use case services like CheckoutService
- **Domain**: Core business entities (Order, LineItem, Product)
- **Infrastructure**: Adapters like InMemoryOrderRepository

Dependencies flow : UI → Application → Domain ← Infrastructure.

## Alternatives Considered

1. **No formal architecture**: Continue using mixed code. However, as the project grows, the code will become difficult to maintain, and the maintenance costs will increase significantly, making it impractical. 

2. **Microservices**: Split into separate services (OrderService, PaymentService).Do not fit for Café POS & Delivery system.
reason:
   - This is a bit over-designed for a small project.
   - AIt will increase network latency and deployment complexity.
   - It is more difficult to maintain data consistency

## Consequences

### Pros
- Clear separation of concerns.
- The domain layer has no external dependencies.
- Each layer is easy to test individually.
- Deployment is simple, requiring only a single. application.

### Cons
- All components must be deployed together.
- Unable to scale layers independently.
- It is important to maintain layer boundaries.