package com.pos.retailfeature.subcomponent;

import com.pos.core.service.CartState;
import com.pos.retailfeature.dto.ProductDto;
import com.pos.retailfeature.events.CartChangeEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Slf4j
public class ReceiptView extends VerticalLayout {
    private final H2 totalSpan = new H2("0");
    private final CartState cartState;
    Button checkoutButton;

    public ReceiptView(CartState cartState) {
        final Grid<ReceiptItem> receiptGrid = new Grid<>(ReceiptItem.class, false);
        this.cartState = cartState;

        cartState.addAttachListener(attachEvent -> {
            log.info("Cart altered");
            Notification.show("Cart altered", 500, Notification.Position.MIDDLE);
        });

        receiptGrid.setDataProvider(cartState.getDataProvider());
        addColumns(receiptGrid, cartState.getDataProvider());


        VerticalLayout customerDetails = new VerticalLayout();
        customerDetails.setWidthFull();
        customerDetails.setPadding(false);
        HorizontalLayout top = new HorizontalLayout();
        top.add(new H4("Receipt"));
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
        HorizontalLayout subtotal = separatedSub("Subtotal(0)", "$50.00");
        HorizontalLayout tax = separatedSub("Tax (8%)", "$3.40");
        HorizontalLayout discount = separatedSub("Discount (Member)", "-$1.40");

        totalsView.add(subtotal, tax, discount);
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

        buttons.add(discountButton, checkoutButton);
        actionBar.add(buttons);

        // total
        totalSpan.getStyle().setFontWeight("600").setColor("#282833");
        HorizontalLayout  netTotal = new HorizontalLayout(new H4("Total"), totalSpan);
        netTotal.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        netTotal.setAlignItems(FlexComponent.Alignment.CENTER);
        netTotal.setWidthFull();
        add(customerDetails, receiptGrid, totalsView,netTotal  , actionBar);

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

    private void addColumns(Grid<ReceiptItem> grid, ListDataProvider<ReceiptItem> provider) {
        // name
        grid.addColumn(ReceiptItem::getName).setHeader("Item").setAutoWidth(true).setFlexGrow(2);
        // quantity
        grid.addComponentColumn(item -> {
            IntegerField qty = new IntegerField();
            qty.setMin(1);
            qty.setStepButtonsVisible(true);
            qty.setValue(item.getQuantity());
            qty.setWidth("100px");

            qty.addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    item.setQuantity(e.getValue());
                    item.setTotalPrice(
                            item.getUnitPrice().multiply(BigDecimal.valueOf(e.getValue()))
                    );
                    provider.refreshItem(item);
                    updateTotal(provider);
                }
            });
            return qty;
        }).setHeader("Qty");

        // price
        grid.addColumn(receiptItem -> receiptItem.getTotalPrice().toString())
                .setHeader("Price")
                .setAutoWidth(true);

        // actions
        grid.addComponentColumn(receiptItem -> {
            Button remove = new Button("", e -> {
                provider.getItems().remove(receiptItem);
                provider.refreshAll();
                updateTotal(provider);
            });
            remove.setIcon(VaadinIcon.TRASH.create());
            remove.addThemeVariants(ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            return remove;
        }).setHeader(" Delete");
    }

    public void updateTotal(ListDataProvider<ReceiptItem> provider) {

        BigDecimal total = provider.getItems().stream()
                .map(ReceiptItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalSpan.setText(formatMoney(total));
    }

    private String formatMoney(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }

    public void updateTotal(){
        BigDecimal total = this.cartState.getDataProvider().getItems().stream()
                .map(ReceiptItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalSpan.setText(formatMoney(total));
//        subtotalValue.setText(formatMoney(total));
    }


    public void checkoutAction(){
        UI.getCurrent().getPage().setLocation("/checkout");
    }
}
