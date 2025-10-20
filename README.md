 Week 6 Lab – Refactoring Code Smells

Removed smells: God Class, Long Method, Primitive Obsession, Duplicated Logic, Feature Envy, Global State.  
Refactorings applied: Extract Class (DiscountPolicy, TaxPolicy, ReceiptPrinter, PricingService, CheckoutService), Replace Conditional with Polymorphism, Introduce Strategy, Dependency Injection, Remove Global State.  
SOLID principles: SRP, each class has one responsibility; OCP, new discounts added without modifying core code; DIP, services depend on abstractions.  
Result: Code is modular, testable and extensible; all characterization tests pass and behavior remains identical.  
Extensibility:Add a new discount by implementing `DiscountPolicy` and injecting it into `PricingService`, no changes to existing classes.
