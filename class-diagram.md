```mermaid
classDiagram

namespace com.cafepos.common {
    class Money {
        - BigDecimal amount
        + of(amount: double) : Money$
        + zero() : Money$
        + add(other: Money) : Money
        + multiply(qty: int) : Money
        + multiply(factor: double) : Money
        + compareTo(o: Money) : int
        + equals(o: Object) : boolean
        + toString() : String
    }
}

namespace com.cafepos.domain {
    class Product {
        <<interface>>
        + id() : String
        + name() : String
        + basePrice() : Money
    }

    class Priced {
        <<interface>>
        + price() : Money
    }

    class SimpleProduct {
        - String id
        - String name
        - Money basePrice
        + SimpleProduct(id: String, name: String, basePrice: Money)
        + id() : String
        + name() : String
        + basePrice() : Money
        + price() : Money
    }

    class LineItem {
        - Product product
        - int quantity
        + LineItem(product: Product, quantity: int)
        + product() : Product
        + quantity() : int
        + lineTotal() : Money
    }

    class Order {
        - long id
        - List~LineItem~ items
        - List~OrderObserver~ observers
        + Order(id: long)
        + id() : long
        + items() : List~LineItem~
        + register(o: OrderObserver) : void
        + unregister(o: OrderObserver) : void
        + addItem(item: LineItem) : void
        + subtotal() : Money
        + taxAtPercent(pct: int) : Money
        + totalWithTax(pct: int) : Money
        + pay(strategy: PaymentStrategy) : void
        + markReady() : void
    }

    class OrderIds {
        - static AtomicLong counter
        + next() : long$
    }
}

namespace com.cafepos.decorator {
    class ProductDecorator {
        <<abstract>>
        # Product base
        # ProductDecorator(base: Product)
        + id() : String
        + basePrice() : Money
    }

    class ExtraShot {
        - static Money SURCHARGE
        + ExtraShot(base: Product)
        + name() : String
        + price() : Money
    }

    class OatMilk {
        - static Money SURCHARGE
        + OatMilk(base: Product)
        + name() : String
        + price() : Money
    }

    class Syrup {
        - static Money SURCHARGE
        + Syrup(base: Product)
        + name() : String
        + price() : Money
    }

    class SizeLarge {
        - static Money SURCHARGE
        + SizeLarge(base: Product)
        + name() : String
        + price() : Money
    }
}

namespace com.cafepos.factory {
    class ProductFactory {
        + create(recipe: String) : Product
    }
}

namespace com.cafepos.catalog {
    class Catalog {
        <<interface>>
        + add(p: Product) : void
        + findById(id: String) : Optional~Product~
    }

    class InMemoryCatalog {
        - Map~String, Product~ byId
        + add(p: Product) : void
        + findById(id: String) : Optional~Product~
    }
}

namespace com.cafepos.payment {
    class PaymentStrategy {
        <<interface>>
        + pay(order: Order) : void
    }

    class CashPayment {
        + pay(order: Order) : void
    }

    class CardPayment {
        + pay(order: Order) : void
    }

    class WalletPayment {
        + pay(order: Order) : void
    }
}

namespace com.cafepos.observer {
    class OrderObserver {
        <<interface>>
        + updated(order: Order, event: String) : void
    }

    class KitchenDisplay {
        + updated(order: Order, event: String) : void
    }

    class DeliveryDesk {
        + updated(order: Order, event: String) : void
    }

    class CustomerNotifier {
        + updated(order: Order, event: String) : void
    }
}

namespace com.cafepos.demo {
    class Week2Demo {
        + main(args: String[]) : void$
    }
    class Week3Demo {
        + main(args: String[]) : void$
    }
    class Week4Demo {
        + main(args: String[]) : void$
        - addItemToOrder(scanner, catalog, order) : void$
        - payOrder(scanner, order) : void$
    }
    class Week5Demo {
        + main(args: String[]) : void$
        - addCustomDrink(scanner, factory, order) : void$
        - viewOrder(order) : void$
        - payOrder(scanner, order) : void$
        - printReceipt(order) : void$
    }
}

%% Core relationships
Product <|.. SimpleProduct : implements
Product <|.. ProductDecorator : implements
Priced <|.. SimpleProduct : implements
Priced <|.. ExtraShot : implements
Priced <|.. OatMilk : implements
Priced <|.. Syrup : implements
Priced <|.. SizeLarge : implements

%% Decorator pattern
ProductDecorator <|-- ExtraShot : extends
ProductDecorator <|-- OatMilk : extends
ProductDecorator <|-- Syrup : extends
ProductDecorator <|-- SizeLarge : extends
ProductDecorator o-- Product : wraps

%% Catalog
Catalog <|.. InMemoryCatalog : implements

%% Payment Strategy
PaymentStrategy <|.. CashPayment : implements
PaymentStrategy <|.. CardPayment : implements
PaymentStrategy <|.. WalletPayment : implements

%% Observer Pattern
OrderObserver <|.. KitchenDisplay : implements
OrderObserver <|.. DeliveryDesk : implements
OrderObserver <|.. CustomerNotifier : implements

%% Composition relationships
LineItem --> Product : uses
Order --> "*" LineItem : contains
Order --> "*" OrderObserver : notifies
InMemoryCatalog --> "*" Product : stores
Order --> PaymentStrategy : pays with

%% Factory Pattern
ProductFactory ..> Product : creates
ProductFactory ..> SimpleProduct : creates
ProductFactory ..> ExtraShot : creates
ProductFactory ..> OatMilk : creates
ProductFactory ..> Syrup : creates
ProductFactory ..> SizeLarge : creates

%% Demo dependencies
Week2Demo ..> Catalog : uses
Week2Demo ..> Order : uses
Week2Demo ..> LineItem : uses
Week3Demo ..> Order : uses
Week3Demo ..> PaymentStrategy : uses
Week4Demo ..> Catalog : uses
Week4Demo ..> Order : uses
Week4Demo ..> OrderObserver : uses
Week4Demo ..> PaymentStrategy : uses
Week5Demo ..> ProductFactory : uses
Week5Demo ..> Order : uses
Week5Demo ..> PaymentStrategy : uses


