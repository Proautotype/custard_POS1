package com.pos.checkoutfeature.component.payment;

import com.pos.checkoutfeature.dto.PaymentOptions;
import com.pos.checkoutfeature.dto.PaymentStatus;

public class PaymentSession {
     
    private final Double payable;
    private Double enteredAmount;
    private PaymentOptions option;
    private PaymentStatus status;

    public PaymentSession(Double payable, PaymentOptions option){
        this.payable = payable;
        this.option = option;
        this.status = PaymentStatus.INITIATED;
    }

    public void updateAmount(Double amount){
        this.enteredAmount = amount;
    }

    public boolean isExact(){
        return enteredAmount != null && enteredAmount.equals(payable);
    }

    public boolean isValid(){
        return enteredAmount != null && enteredAmount > 0 && enteredAmount <= payable;
    }

    public Double balance(){
        return payable - (enteredAmount == null ? 0 : enteredAmount);
    }

    public PaymentOptions option(){
        return option;
    }

    public Double payable(){
        return this.payable;
    }

    public void markPending() {
       status = PaymentStatus.PENDING;
    }

}
