package com.pos.checkoutfeature.component.payment;

public record PaymentEvent(
    PaymentSession session,
    boolean confirmed
) {}
