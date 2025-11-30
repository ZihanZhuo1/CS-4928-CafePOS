# Café POS & Delivery System

A domain-centric Point of Sale system demonstrating object-oriented design principles, design patterns, and layered architecture.

## Project Structure
```
com.cafepos/
├── common/          # Value objects (Money)
├── domain/          # Core entities (Order, LineItem, Product)
├── payment/         # Payment strategies (Cash, Card, Wallet)
├── observer/        # Observer pattern (Kitchen, Delivery, Customer)
├── decorator/       # Product decorators (ExtraShot, OatMilk, Syrup)
├── factory/         # Product factory
├── pricing/         # Pricing policies (Discount, Tax)
├── command/         # Command pattern (Undo, Macro)
├── menu/            # Composite menu structure
├── state/           # Order lifecycle state machine
├── app/             # Application services (CheckoutService)
│   └── events/      # Domain events and EventBus
├── infra/           # Infrastructure adapters (Repository, Wiring)
├── ui/              # Presentation layer (Controller, View)
└── demo/            # Demo classes for each week
```

## Weekly Progress

### Week 2 — Money & Orders
- Implemented `Money` value object with safe arithmetic operations
- Created `Order` and `LineItem` entities
- Subtotal, tax, and total calculations

### Week 3 — Payment Strategies
- **Strategy Pattern**: Polymorphic payment handling
- Implementations: `CashPayment`, `CardPayment`, `WalletPayment`
- Extensible design for adding new payment methods

### Week 4 — Observer Notifications
- **Observer Pattern**: Decoupled event notifications
- Observers: `KitchenObserver`, `DeliveryObserver`, `CustomerObserver`
- Order broadcasts events without hard-coded dependencies

### Week 5 — Decorator & Factory
- **Decorator Pattern**: Flexible product customization
- Decorators: `ExtraShot`, `OatMilk`, `Syrup`, `SizeLarge`
- **Factory Pattern**: Recipe-driven product creation (e.g., `ESP+SHOT+OAT`)

### Week 6 — Refactored Pricing
- **Extract Class**: Separated `DiscountPolicy` and `TaxPolicy`
- `PricingService` orchestrates discount, tax, and total calculation
- Removed code smells: God Class, Long Method, Primitive Obsession

### Week 8 — Command & Adapter
- **Command Pattern**: Decouples UI from domain logic, supports undo/macro
- **Adapter Pattern**: Integrates legacy printer without modifying core code
- Demo: `Week8Demo_Commands`, `Week8Demo_Adapter`

### Week 9 — Composite, Iterator & State
- **Composite Pattern**: Hierarchical menu structure
- **Iterator Pattern**: Menu filtering (e.g., vegetarian options)
- **State Pattern**: Order lifecycle (NEW → PAID → PREPARING → READY → DELIVERED)
- Demo: `Week9Demo_Menu`, `Week9Demo_State`

### Week 10 — Layered Architecture & MVC
- **Four-layer architecture**: Presentation, Application, Domain, Infrastructure
- **MVC Pattern**: Controller handles input, View handles output
- **EventBus**: Publish/subscribe for loose coupling
- Demo: `Week10Demo_MVC`, `EventWiringDemo`

## Architecture

### Layer Dependencies
```
UI (Presentation) → Application → Domain ← Infrastructure
```

| Layer | Responsibility | Examples |
|-------|----------------|----------|
| Domain | Core business logic | Order, LineItem, Product |
| Application | Use case orchestration | CheckoutService |
| Infrastructure | External adapters | InMemoryOrderRepository |
| Presentation | User I/O | OrderController, ConsoleView |

### Connectors
- **OrderRepository**: Interface in Domain, implemented in Infrastructure
- **EventBus**: Publish/subscribe for decoupled component communication

## Design Patterns Summary

| Pattern | Location | Purpose |
|---------|----------|---------|
| Strategy | `payment/` | Polymorphic payment methods |
| Observer | `observer/` | Decoupled event notifications |
| Decorator | `decorator/` | Flexible product customization |
| Factory | `factory/` | Recipe-driven product creation |
| Command | `command/` | Undo/redo, macro commands |
| Adapter | `printing/` | Legacy printer integration |
| Composite | `menu/` | Hierarchical menu structure |
| Iterator | `menu/` | Menu filtering |
| State | `state/` | Order lifecycle management |
| MVC | `ui/` | Separation of concerns in UI |

## Running the Demos
```bash
# Compile
mvn -q -DskipTests compile

# Week 2-6 Demos
java -cp target/classes com.cafepos.demo.Week2Demo
java -cp target/classes com.cafepos.demo.Week3Demo
java -cp target/classes com.cafepos.demo.Week4Demo
java -cp target/classes com.cafepos.demo.Week5Demo
java -cp target/classes com.cafepos.demo.Week6Demo

# Week 8 Demos 
java -cp target/classes com.cafepos.demo.Week8DemoCommands
java -cp target/classes com.cafepos.demo.Week8DemoAdapter

# Week 8 Demos (interaction)
java -cp target/classes com.cafepos.demo.Week8Demo_Commands
java -cp target/classes com.cafepos.demo.Week8Demo_Adapter

# Week 9 Demos
java -cp target/classes com.cafepos.demo.Week9Demo_Menu
java -cp target/classes com.cafepos.demo.Week9Demo_State

# Week 10 Demos
java -cp target/classes com.cafepos.demo.Week10DemoMVC
java -cp target/classes com.cafepos.ui.EventWiringDemo
```

## Running Tests
```bash
mvn test
```

## Trade-offs: Layering vs Partitioning

### Why Layered Monolith?

1. **Simplicity**: Single process, easy debugging and deployment
2. **Performance**: In-process calls faster than network calls
3. **Consistency**: No distributed transaction complexity

### Future Partitioning Candidates

- **Payments**: Separate service for PCI compliance (REST API)
- **Notifications**: Async messaging via EventBus or message queue
- **Reporting**: CQRS with separate read model

### Connectors for Future Splitting

- REST APIs for synchronous communication
- Event Bus for asynchronous domain events
- Message Queues (Kafka/RabbitMQ) for reliable delivery

## ADRs

See [docs/adr/](docs/adr/) for Architecture Decision Records:
- [ADR-001: Layered Monolith Architecture](docs/adr/ADR-001-layered-architecture.md)
- [ADR-002: EventBus for Component Communication](docs/adr/ADR-002-eventbus-connector.md)

## SOLID Principles Applied

- **SRP**: Each class has one responsibility
- **OCP**: New discounts/payments added without modifying existing code
- **DIP**: Services depend on abstractions (interfaces)