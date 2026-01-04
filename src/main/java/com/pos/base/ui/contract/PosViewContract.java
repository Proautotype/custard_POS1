package com.pos.base.ui.contract;

import com.pos.core.extension.CartItemModel;

import java.math.BigDecimal;
import java.util.List;

public interface PosViewContract {
    interface Presenter {
        void addItemToCart(String barcode);
        void processPayment(BigDecimal amount);
        void applyDiscount(String code);
    }

    interface View {
        void displayCart(List<CartItemModel> items);
        void updateGrandTotal(BigDecimal total);
        void showPaymentSuccess();
        void showErrorMessage(String message);
    }
}
