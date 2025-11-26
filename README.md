 Week 6 Lab – Refactoring Code Smells

Removed smells: God Class, Long Method, Primitive Obsession, Duplicated Logic, Feature Envy, Global State.  
Refactorings applied: Extract Class (DiscountPolicy, TaxPolicy, ReceiptPrinter, PricingService, CheckoutService), Replace Conditional with Polymorphism, Introduce Strategy, Dependency Injection, Remove Global State.  
SOLID principles: SRP, each class has one responsibility; OCP, new discounts added without modifying core code; DIP, services depend on abstractions.  
Result: Code is modular, testable and extensible; all characterization tests pass and behavior remains identical.  
Extensibility:Add a new discount by implementing `DiscountPolicy` and injecting it into `PricingService`, no changes to existing classes.

Week 10 Lab — Layering vs Partitioning Trade-offs

Why Layered Monolith?

The reasons we chose a layered monolithic architecture for our coffee shop POS system are as follows:

1. Simplicity: All components run in a single process, making development, debugging, and deployment very simple.

2. Performance: Intra-process method calls between layers are faster than network calls between distributed services.

3. Consistency: Data consistency can be easily maintained without the need for distributed transactions.

Future Partitioning Candidates

1. Payments: Can be implemented as a standalone microservice to meet PCI compliance requirements. Synchronous processing will be handled using a REST API.

2. Notification: Asynchronous message passing can be performed using an event bus or message queue to retrieve email/SMS notifications.

3. Reports: Read-intensive analysis can use CQRS with an independent read model.

 Connectors for Future Splitting

1. REST APIs: For synchronous service-to-service communication
2. Event Bus: For asynchronous domain events (OrderCreated, OrderPaid)
3. Message Queues: For reliable async processing (Kafka/RabbitMQ)