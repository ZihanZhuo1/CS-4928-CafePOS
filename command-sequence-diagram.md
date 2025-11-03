# Command Pattern - Sequence Diagram

## Cashier Adds an Item to Order

```mermaid
sequenceDiagram
    actor Cashier
    participant PosRemote as PosRemote<br/>(Invoker)
    participant AddItemCommand as AddItemCommand<br/>(Command)
    participant OrderService as OrderService<br/>(Receiver)
    participant ProductFactory
    participant Order

    Note over Cashier,Order: Button Press Flow: Invoker → Command → Receiver

    Cashier->>PosRemote: press(0)
    activate PosRemote
    Note right of PosRemote: Looks up command<br/>in slot 0

    PosRemote->>AddItemCommand: execute()
    activate AddItemCommand
    Note right of AddItemCommand: Command forwards<br/>request to receiver

    AddItemCommand->>OrderService: addItem("ESP", 1)
    activate OrderService
    Note right of OrderService: Receiver does<br/>the actual work

    OrderService->>ProductFactory: create("ESP")
    activate ProductFactory
    ProductFactory-->>OrderService: Product
    deactivate ProductFactory

    OrderService->>Order: addItem(LineItem)
    activate Order
    Order-->>OrderService: void
    deactivate Order

    OrderService-->>AddItemCommand: void
    deactivate OrderService

    AddItemCommand-->>PosRemote: void
    deactivate AddItemCommand

    PosRemote->>PosRemote: history.push(command)
    Note right of PosRemote: Stores command<br/>for undo

    PosRemote-->>Cashier: ✓ Command executed
    deactivate PosRemote
```
## Roles

**Cashier (Actor):**

- Initiates the action by pressing a button

**PosRemote (Invoker):**

- Holds command slots (buttons)
- Knows which command is in each slot
- Executes commands without knowing what they do
- Maintains command history for undo

**AddItemCommand (Command):**

- Encapsulates the request as an object
- Knows which receiver to call
- Stores parameters (recipe, quantity)
- Acts as a "translator" between invoker and receiver

**OrderService (Receiver):**

- Knows HOW to perform the actual work
- Coordinates with domain objects (ProductFactory, Order)
- Keeps business logic separate from UI concerns

**Domain Objects (ProductFactory, Order):**

- The actual business objects that do the work
- Commands never directly access these - always through the receiver

## Undo Flow

```mermaid
sequenceDiagram
    actor Cashier
    participant PosRemote as PosRemote<br/>(Invoker)
    participant AddItemCommand as AddItemCommand<br/>(Command)
    participant OrderService as OrderService<br/>(Receiver)
    participant Order

    Note over Cashier,Order: Undo Flow: Invoker → Command → Receiver

    Cashier->>PosRemote: undo()
    activate PosRemote

    PosRemote->>PosRemote: history.pop()
    Note right of PosRemote: Gets last command<br/>from history

    PosRemote->>AddItemCommand: undo()
    activate AddItemCommand
    Note right of AddItemCommand: Command reverses<br/>its own action

    AddItemCommand->>OrderService: removeLastItem()
    activate OrderService

    OrderService->>Order: removeLastItem()
    activate Order
    Order-->>OrderService: void
    deactivate Order

    OrderService-->>AddItemCommand: void
    deactivate OrderService

    AddItemCommand-->>PosRemote: void
    deactivate AddItemCommand

    PosRemote-->>Cashier: ✓ Undo completed
    deactivate PosRemote
```

## MacroCommand Flow

```mermaid
sequenceDiagram
    actor Cashier
    participant PosRemote as PosRemote<br/>(Invoker)
    participant MacroCommand as MacroCommand<br/>(Composite Command)
    participant AddEspresso as AddItemCommand<br/>(Espresso)
    participant AddLatte as AddItemCommand<br/>(Latte)
    participant OrderService as OrderService<br/>(Receiver)

    Note over Cashier,OrderService: MacroCommand executes multiple commands

    Cashier->>PosRemote: press(3)
    activate PosRemote

    PosRemote->>MacroCommand: execute()
    activate MacroCommand
    Note right of MacroCommand: Loops through<br/>all commands

    MacroCommand->>AddEspresso: execute()
    activate AddEspresso
    AddEspresso->>OrderService: addItem("ESP", 1)
    activate OrderService
    OrderService-->>AddEspresso: void
    deactivate OrderService
    AddEspresso-->>MacroCommand: void
    deactivate AddEspresso

    MacroCommand->>AddLatte: execute()
    activate AddLatte
    AddLatte->>OrderService: addItem("LAT", 1)
    activate OrderService
    OrderService-->>AddLatte: void
    deactivate OrderService
    AddLatte-->>MacroCommand: void
    deactivate AddLatte

    MacroCommand-->>PosRemote: void
    deactivate MacroCommand

    PosRemote->>PosRemote: history.push(macroCommand)

    PosRemote-->>Cashier: ✓ Combo added
    deactivate PosRemote

    Note over Cashier,OrderService: Both items added with one button press!
```
