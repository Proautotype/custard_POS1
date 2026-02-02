package com.pos.checkoutfeature.component.payment;

public interface PaymentOrchestrator {
    void handle(PaymentEvent paymentEvent);
} 