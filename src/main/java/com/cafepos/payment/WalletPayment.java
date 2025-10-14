package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class WalletPayment implements PaymentStrategy {
    private final String walletId;

    public WalletPayment(String walletId) {
        this.walletId = walletId;
    }

    public WalletPayment() {
        this.walletId = "WALLET-DEFAULT-001";
    }

    @Override
    public void pay(Order order) {
        System.out.println("[Wallet] Customer paid " + order.totalWithTax(10) + " EUR via wallet " + walletId);
    }
}