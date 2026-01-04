package com.pos.base.ui.contract;

import com.pos.core.service.PosService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class GenericPosPresenter implements PosViewContract.Presenter{

    private final PosViewContract.View view; // Reference to the UI implementation
    private final PosService posService;     // Reference to the shared business logic
    @Override
    public void addItemToCart(String barcode) {
        try {
           // Sale sale = posService.addItem(barcode); // Business logic runs here
           // view.displayCart(sale.toCartModel()); // View is updated
        } catch (Exception e) {
            view.showErrorMessage("Item not found or out of stock.");
        }
    }

    @Override
    public void processPayment(BigDecimal amount) {

    }

    @Override
    public void applyDiscount(String code) {

    }
}
