package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardPayment() {
        this.cardNumber = "1234567890123456"; // 默认卡号
    }

    @Override
    public void pay(Order order) {
        String masked = "****" + cardNumber.substring(cardNumber.length() - 4);
        System.out.println("[Card] Customer paid " + order.totalWithTax(10) + " EUR with card " + masked);
    }
}