package com.pos.retailfeature.subcomponent;

import com.pos.core.service.CartState;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.NumberFormat;

import static com.pos.shared.Utils.calculateDiscount;

@Slf4j
@org.springframework.stereotype.Component
@VaadinSessionScope
public class ReceiptView extends VerticalLayout {
    private final H2 totalSpan = new H2("0");
    private final CartState cartState;
    private final Utils utils;

    Button checkoutButton;
    NumberField discountTxt = new NumberField();
    Span subTotalTxt = new Span();

    public ReceiptView(CartState cartState, Utils utils) {
        final Grid<ReceiptItem> receiptGrid = new Grid<>(ReceiptItem.class, false);
        this.cartState = cartState;
        this.utils = utils;
        updateTotal();

        discountTxt.setMin(0.0);
        discountTxt.setStepButtonsVisible(true);
        discountTxt.setValue(cartState.getDiscount());
        discountTxt.addValueChangeListener(numberChangeEvent -> {
            cartState.setDiscount(numberChangeEvent.getValue());
            updateTotal();
        });

        receiptGrid.setDataProvider(cartState.getDataProvider());
        cartState.getDataProvider().addDataProviderListener(event -> {
            updateTotal();
        });
        addColumns(receiptGrid, cartState.getDataProvider());

        VerticalLayout customerDetails = new VerticalLayout();
        customerDetails.setWidthFull();
        customerDetails.setPadding(false);
        HorizontalLayout top = new HorizontalLayout();
        top.add(new H4("Cart"));
        top.setSpacing(false);
        top.setWidthFull();
        HorizontalLayout content = new HorizontalLayout();
        content.add(
                new H5("Winston. A"),
                new Button("Edit")
        );
        content.addClassNames(
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.BETWEEN
        );
        content.setWidthFull();
        customerDetails.add(top, content);

        VerticalLayout totalsView = new VerticalLayout();
        HorizontalLayout subtotal = separatedView("Subtotal(0)", subTotalTxt);
        // HorizontalLayout tax = separatedSub("Tax (8%)", "");
        HorizontalLayout discount = separatedView("Discount (%)", discountTxt);

        totalsView.add(subtotal,  discount);
        totalsView.setWidthFull();
        totalsView.getStyle().setBackgroundColor("#dcdcdc");

        VerticalLayout actionBar = new VerticalLayout();
        actionBar.setPadding(false);
        actionBar.setWidthFull();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setPadding(false);
        buttons.addClassNames(LumoUtility.JustifyContent.BETWEEN);

        checkoutButton = new Button("Checkout");
        checkoutButton.setSuffixComponent(VaadinIcon.ARROW_RIGHT.create());
        checkoutButton.addClassNames(LumoUtility.Background.PRIMARY);
        checkoutButton.getStyle().setColor("#fff");
        checkoutButton.addClickListener(buttonClickEvent -> {
            checkoutAction();
        });

        Button discountButton = new Button("Discount");
        discountButton.setSuffixComponent(VaadinIcon.SCALE_UNBALANCE.create());

        buttons.add(checkoutButton);
        actionBar.add(buttons);

        // total
        totalSpan.getStyle().setFontWeight("600").setColor("#282833");
        HorizontalLayout netTotal = new HorizontalLayout(new H4("Total"), totalSpan);
        netTotal.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        netTotal.setAlignItems(FlexComponent.Alignment.CENTER);
        netTotal.setWidthFull();
        add(customerDetails, receiptGrid, totalsView, netTotal, actionBar);

        setHeightFull();
        setWidthFull();
        setPadding(false);
        setSpacing(false);
    }

    private HorizontalLayout separatedSub(String first, String second) {
        HorizontalLayout content = new HorizontalLayout();
        content.setPadding(false);
        content.setWidthFull();
        content.addClassNames(LumoUtility.JustifyContent.BETWEEN);
        Span subtotalHeaderTxt = new Span(first);
        subtotalHeaderTxt.getStyle().setFontSize("14px").setFontWeight(400);
        Span subtotalValueTxt = new Span(second);
        subtotalValueTxt.getStyle().setFontSize("15px").setFontWeight(500);

        content.add(subtotalHeaderTxt, subtotalValueTxt);
        return content;
    }

    private HorizontalLayout separatedView(String first, Component second) {
        HorizontalLayout content = new HorizontalLayout();
        content.setPadding(false);
        content.setWidthFull();
        content.addClassNames(LumoUtility.JustifyContent.BETWEEN);
        Span subtotalHeaderTxt = new Span(first);
        subtotalHeaderTxt.getStyle().setFontSize("14px").setFontWeight(400);
        second.getStyle().setFontSize("15px").setFontWeight(500);

        content.add(subtotalHeaderTxt, second);
        return content;
    }

    private void addColumns(Grid<ReceiptItem> grid, ListDataProvider<ReceiptItem> provider) {
        // name
        grid.addColumn(ReceiptItem::getName)
                .setHeader("Item")
                .setFlexGrow(3)
                .setTextAlign(ColumnTextAlign.START);
        // quantity
        grid.addComponentColumn(item -> {
            IntegerField qty = new IntegerField();
            qty.setWidth("112px");
            qty.setMin(1);
            qty.setMax(100);
            qty.setStepButtonsVisible(true);
            qty.setValue(item.getQuantity());

            qty.addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    item.setQuantity(e.getValue());
                    BigDecimal newTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(e.getValue()));
                    log.info("new total price inline -> {} ", newTotalPrice);
                    item.setTotalPrice(newTotalPrice);
                    provider.refreshItem(item);
                    updateTotal(provider);
                }
            });
            return qty;
        })
        .setHeader("Quantity")
        .setFlexGrow(2)
        .setTextAlign(ColumnTextAlign.CENTER);

        // price
        grid.addColumn(receiptItem -> receiptItem.getTotalPrice().toString())
                .setHeader("Price")
                .setTextAlign(ColumnTextAlign.CENTER)
                .setFlexGrow(2);
                

        // actions
        grid.addComponentColumn(receiptItem -> {
            Button remove = new Button("", e -> {
                provider.getItems().remove(receiptItem);
                provider.refreshAll();
                updateTotal(provider);
            });
            remove.setIcon(VaadinIcon.TRASH.create());
            remove.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
            return remove;
        }).setHeader("Remove")
        .setFlexGrow(0);
    }

    public void updateTotal(ListDataProvider<ReceiptItem> provider) {

        BigDecimal total = provider.getItems().stream()
                .map(ReceiptItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalSpan.setText(utils.cedisCurrencyFormat(total));
    }

    public void updateTotal() {
        BigDecimal total = this.cartState.getTotal();
        BigDecimal discountedTotal = calculateDiscount(cartState.getDiscount(), total);

        subTotalTxt.setText(utils.cedisCurrencyFormat(total));
        totalSpan.setText(utils.cedisCurrencyFormat(discountedTotal));
    }


    public void checkoutAction() {
        UI.getCurrent().getPage().setLocation("/checkout");
    }
}
