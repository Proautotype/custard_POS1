package com.pos.core.extension.pharmacy.ui;

import com.pos.base.ui.contract.GenericPosPresenter;
import com.pos.base.ui.contract.PosViewContract;
import com.pos.core.extension.CartItemModel;
import com.pos.core.service.PosService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.List;

@Route("/pharmacy")
@PageTitle("Pharmacy")
@Profile("pharmacy-domain")
@RequiredArgsConstructor
public class PharmacyPosView extends Main implements PosViewContract.View {

    // UI Elements
    private final VerticalLayout cartList = new VerticalLayout();
    private final TextField barcodeInput = new TextField("Scan Barcode");
    private final Div totalDisplay = new Div();
    private final Div alertArea = new Div();

    // Adapter Reference
    private final PosViewContract.Presenter presenter;

    public PharmacyPosView(PosService posService){
        this.presenter = new GenericPosPresenter(this, posService);

        Button scanButton = new Button("Add Item");
        scanButton.addClickListener(e -> presenter.addItemToCart(barcodeInput.getValue()));

        add(scanButton);
    }

    @Override
    public void displayCart(List<CartItemModel> items) {

    }

    @Override
    public void updateGrandTotal(BigDecimal total) {

    }

    @Override
    public void showPaymentSuccess() {

    }

    @Override
    public void showErrorMessage(String message) {

    }
}
