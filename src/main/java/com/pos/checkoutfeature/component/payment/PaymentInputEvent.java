package com.pos.checkoutfeature.component.payment;

import com.pos.checkoutfeature.dto.PaymentOptions;

public record PaymentInputEvent(
        PaymentOptions option,
        double amount,
        boolean isExact,
        boolean isFinal
) {}
