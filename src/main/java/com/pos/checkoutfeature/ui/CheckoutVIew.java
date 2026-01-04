package com.pos.checkoutfeature.ui;

import com.pos.base.ui.HasDynamicHeader;
import com.pos.core.service.CartState;
import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Route("/checkout")
@PageTitle("Checkout")
@PermitAll
public class CheckoutVIew extends HorizontalLayout implements HasDynamicHeader {

    private CartState cartState;
    private ArrayList<String> predefinedInputValues = new ArrayList<>();
    private TextField balanceField;
    private Button completePaymentBtn;

    CheckoutVIew(CartState cartState) {
        this.cartState = cartState;
        predefinedInputValues.addAll(List.of("10", "15", "20", "30", "50", "Exact"));


        setWidthFull();
        setHeightFull();

        add(leftArea(), rightArea());
    }

    private VerticalLayout leftArea() {
        VerticalLayout content = new VerticalLayout();
        content.setWidth("60%");
        content.setHeightFull();
        HorizontalLayout header = new HorizontalLayout();
        Div checkoutMeta = new Div();
        checkoutMeta.addClassNames(LumoUtility.FlexDirection.COLUMN, LumoUtility.Padding.XSMALL);
        H3 checkoutHeader = new H3("Checkout");
        Span checkoutTime = new Span("time");
        checkoutMeta.add(checkoutHeader, checkoutTime);
        header.add(checkoutMeta);

        Div totalTray = new Div();
        totalTray.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER
        );
        totalTray.setWidth("100%");
        totalTray.getStyle().setPadding("15px");

        Span totalTextDesc = new Span("TOTAL AMOUNT DUE");
        H2 totalText = new H2(cartState.formatMoney(cartState.getTotal()));
        totalText.getStyle().setFontSize("5em");

        totalTray.add(totalTextDesc, totalText);

        TextArea paymentNote = new TextArea("TRANSACTION NOTES (OPTIONAL)", "Add note about prescription, patient request or any special details to this transaction");
        paymentNote.setWidthFull();
        paymentNote.setHeight("120px");

        HorizontalLayout paymentAction = new HorizontalLayout();
        completePaymentBtn = new Button("Complete & Print");
        completePaymentBtn.setEnabled(false);
        completePaymentBtn.setWidthFull();
        completePaymentBtn.addClassNames(
                LumoUtility.Background.PRIMARY
        );
        completePaymentBtn.getStyle().setColor("#fff");
        paymentAction.setWidthFull();
        paymentAction.add(completePaymentBtn);

        VerticalLayout actions = new VerticalLayout(paymentNote, paymentAction);
        actions.setWidthFull();

        content.add(header, totalTray, checkoutMode(), actions);
        return content;
    }

    private Div rightArea() {
        Div content = new Div();
        content.addClassNames(
                LumoUtility.JustifyContent.BETWEEN, LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN);
        content.setWidth("35%");
        content.getStyle().setBackgroundColor("rgba(149, 157, 165, 0.1)");

        HorizontalLayout titleArea = new HorizontalLayout(
                new HorizontalLayout(VaadinIcon.RECORDS.create(), new H6("Receipt Preview")),
                new Button("Draft", event -> {
                })
        );
        titleArea.setJustifyContentMode(JustifyContentMode.BETWEEN);
        titleArea.setAlignItems(Alignment.CENTER);
        titleArea.setPadding(true);
        titleArea.getStyle().setBackgroundColor("#ddd");
        titleArea.setWidthFull();


        VerticalLayout hero = new VerticalLayout();
        hero.addClassNames("receipt-area",
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.Width.FULL
        );
        Span companyAddress = new Span("Takoradi Open, 123 Avenue kenkey street");
        companyAddress.addClassNames(LumoUtility.TextAlignment.CENTER);
        hero.add(new H4("National You Company"),
                companyAddress
                ,
                new Span("*------------------------------*"));

        VirtualList<ReceiptItem> receiptList = new VirtualList<>();
        receiptList.setDataProvider(cartState.getDataProvider());
        receiptList.setRenderer(receiptRowRenderer);
        receiptList.setWidth("90%");
        receiptList.getStyle().setPaddingLeft("10px");

        Div receiptArea = new Div();
        receiptArea.getStyle().setBoxShadow("rgba(149, 157, 165, 0.3) 0px 8px 24px");
        receiptArea.addClassNames("receipt-area");
        receiptArea.setWidth("80%");
        receiptArea.setHeight("80%");

        receiptArea.add(hero, receiptList);
        content.add(titleArea, receiptArea, new HorizontalLayout(new Span("Receipt options")));
        return content;
    }

    private TabSheet checkoutMode() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setWidthFull();
        tabSheet.add(
                createTab("Cash", VaadinIcon.CASH.create()),
                cashPayment(cartState.getTotal(), false)
        );

        tabSheet.add(
                createTab("Credit Card", VaadinIcon.CREDIT_CARD.create()),
                new Div(new Text("Selected card payment"))
        );
        tabSheet.add(
                createTab("Mobile Money", VaadinIcon.MOBILE.create()),
                cashPayment(cartState.getTotal(), true)
        );
        return tabSheet;
    }

    @Override
    public Component[] getHeaderActions() {
        return new Component[0];
    }

    private Component createTab(String title, Icon icon) {
        Div tab = new Div(icon, new Span(title));
        tab.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.CENTER, LumoUtility.AlignItems.CENTER, LumoUtility.FlexDirection.COLUMN);
        tab.getStyle().setPadding("5px");
        return tab;
    }

    private Component cashPayment(BigDecimal payable, boolean isMobileMoney) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setWidthFull();

        HorizontalLayout top = new HorizontalLayout();
        VerticalLayout left = new VerticalLayout();

        HorizontalLayout mobileMoneyContainer = new HorizontalLayout();
        TextField mobileMoneyTxt = new TextField();
        mobileMoneyTxt.setMaxLength(10);
        mobileMoneyTxt.setMinLength(10);
        mobileMoneyTxt.setPattern("xxx-xxxx-xxx");
        mobileMoneyTxt.setLabel("Enter mobile money number");
        mobileMoneyTxt.setPlaceholder("0200000000");
        mobileMoneyContainer.add(mobileMoneyTxt);

        NumberField amtTenderedField = getNumberField();
        HorizontalLayout predefinedValuesDisplay = new HorizontalLayout();
        predefinedValuesDisplay.setPadding(false);
        predefinedInputValues.forEach(inputValueStr -> {
            Button valueBtn = new Button("¢".concat(inputValueStr));
            valueBtn.addClickListener(buttonClickEvent -> {
                if (!inputValueStr.equalsIgnoreCase("exact")) {
                    amtTenderedField.setValue(Double.valueOf(inputValueStr));
                } else {
                    amtTenderedField.setValue(payable.doubleValue());
                }
            });
            predefinedValuesDisplay.add(valueBtn);
        });

        left.add(amtTenderedField, predefinedValuesDisplay);

        if (isMobileMoney) {
            left.add(mobileMoneyContainer);
        }

        VerticalLayout right = new VerticalLayout();
        balanceField = new TextField("Change Due");
        balanceField.setPrefixComponent(new Span("¢"));
        balanceField.setWidth("100%");
        balanceField.setHeight("80px");

        right.add(balanceField);

        top.add(left, right);
        layout.add(top);
        return layout;
    }

    @NotNull
    private NumberField getNumberField() {
        NumberField amtTenderedField = new NumberField("Amount tendered");
        amtTenderedField.setValue(cartState.getTotal().doubleValue());
        amtTenderedField.setMin(0);
        amtTenderedField.setStepButtonsVisible(true);
        amtTenderedField.setPrefixComponent(new Span("¢"));
        amtTenderedField.setWidth("100%");

        NumberField.NumberFieldI18n amtTenderedFieldI8n = new NumberField.NumberFieldI18n();
        amtTenderedFieldI8n.setMinErrorMessage("Value can't be lesser than zero (0)");
        amtTenderedField.setI18n(amtTenderedFieldI8n);

        amtTenderedField.addValueChangeListener(amtTenderedChangeEvent());
        return amtTenderedField;
    }

    private HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<NumberField, Double>> amtTenderedChangeEvent() {
        return inputEvent -> {
            double change = BigDecimal.valueOf(inputEvent.getValue()).subtract(cartState.getTotal()).doubleValue();
            balanceField.setValue(String.valueOf(change));
            completePaymentBtn.setEnabled(change >= 0);

            log.info("input value changed {} ", change);
        };
    }

    private ComponentRenderer<Component, ReceiptItem> receiptRowRenderer = new ComponentRenderer<>(receiptItem -> {
        Div row = new Div();
        Div detail = new Div();
        detail.setWidthFull();
        detail.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.JustifyContent.BETWEEN,
                LumoUtility.Padding.SMALL
        );

        Span itemNameTxt = new Span(receiptItem.getName());
        itemNameTxt.getStyle().setFontSize("12px").setFontWeight("800px");
        Span itemNetTotalTxt = new Span(cartState.formatMoney(receiptItem.getTotalPrice()));
        itemNetTotalTxt.getStyle().setFontSize("12px").setFontWeight("400px");
        detail.add(itemNameTxt, itemNetTotalTxt);
        detail.setWidth("90%");
        detail.getStyle().setPadding("0px");

        Span itemSummationTxt = new Span(String.format("%s * ¢%s", receiptItem.getQuantity(), receiptItem.getUnitPrice()));
        itemSummationTxt.addClassNames(LumoUtility.TextColor.SECONDARY);
        itemSummationTxt.getStyle().setFontSize("11px");

        row.add(detail, new HorizontalLayout(itemSummationTxt));
        row.getStyle().setBorderBottom("1px solid darkgray").setPadding("5px");
        row.setWidthFull();
        return row;
    });

}
