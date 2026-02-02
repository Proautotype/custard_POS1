package com.pos.checkoutfeature.component.simplepayment;

public interface SimplePaymentListener {
    public void onAmountChanged(Double amount);
    public void onModeChanged(PaymentMode mode);
    public void onConfirm(Double amount, PaymentMode mode );
    public void onDescriptionChanged(String description);
    public void onMobileNumberChanged(String mobileNumber);
}
