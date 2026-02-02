package com.pos.checkoutfeature.ui;

import com.pos.base.ui.HasDynamicHeader;
import com.pos.checkoutfeature.component.simplepayment.PaymentMode;
import com.pos.checkoutfeature.component.simplepayment.SimplePaymentComponent;
import com.pos.checkoutfeature.component.simplepayment.SimplePaymentListener;
import com.pos.checkoutfeature.component.simplepayment.SimplePaymentOrchestrator;
import com.pos.core.service.CartState;
import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Route("/checkout")
@PageTitle("Checkout")
@PermitAll
public class CheckoutVIew extends HorizontalLayout implements HasDynamicHeader {

    // private final AppConfigurationProperties appConfigurationProperties;
    private CartState cartState;
    private SimplePaymentOrchestrator orchestrator;
    private Utils utils;

    NumberField amtTenderedField;

    CheckoutVIew(CartState cartState, SimplePaymentOrchestrator orchestrator, Utils utils) {
        this.cartState = cartState;
        this.orchestrator = orchestrator; 
        this.utils = utils;

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
        Span checkoutTime = new Span(discountCalculator());
        checkoutMeta.add(checkoutHeader, checkoutTime);
        header.add(checkoutMeta);

        Div totalTray = new Div();
        totalTray.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.AlignItems.CENTER);
        totalTray.setWidth("100%");
        totalTray.getStyle().setPadding("15px");

        Span totalTextDesc = new Span("TOTAL AMOUNT DUE");
        H2 totalText = new H2(utils.cedisCurrencyFormat(cartState.getTotal()));
        totalText.getStyle().setFontSize("5em");

        totalTray.add(totalTextDesc, totalText);

        content.add(header, totalTray, checkoutMode());
        return content;
    }

    private Div rightArea() {
        Div content = new Div();
        content.addClassNames(
                LumoUtility.JustifyContent.BETWEEN, LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN);
        content.setWidth("35%");
        content.getStyle().setBackgroundColor("rgba(149, 157, 165, 0.1)");

        HorizontalLayout titleArea = new HorizontalLayout(
                new HorizontalLayout(VaadinIcon.RECORDS.create(), new H6("Receipt Preview")),
                new Button("Draft", event -> {
                }));
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
                LumoUtility.Width.FULL);
        Span companyAddress = new Span("Takoradi Open, 123 Avenue kenkey street");
        companyAddress.addClassNames(LumoUtility.TextAlignment.CENTER);
        hero.add(new H4("National You Company"),
                companyAddress,
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
                cashPayment(cartState.getTotal()));
        tabSheet.add(
                createTab("Mobile Money", VaadinIcon.MOBILE.create()),
                mobilePayment(cartState.getTotal()));
        return tabSheet;
    }

    @Override
    public Component[] getHeaderActions() {
        return new Component[0];
    }

    private Component createTab(String title, Icon icon) {
        Div tab = new Div(icon, new Span(title));
        tab.addClassNames(
            LumoUtility.Display.FLEX, 
            LumoUtility.JustifyContent.CENTER, 
            LumoUtility.AlignItems.CENTER,
            LumoUtility.FlexDirection.COLUMN
        );
        tab.getStyle().setPadding("5px");
        return tab;
    }

    private Component cashPayment(BigDecimal payable) {

        SimplePaymentComponent paymentComponent = new SimplePaymentComponent(PaymentMode.CASH, cartState.getTotal().doubleValue());
        paymentComponent.setListener(valuechangePaymentListener);
       return paymentComponent;
    }

    private Component mobilePayment(BigDecimal payable) {
        SimplePaymentComponent paymentComponent = new SimplePaymentComponent(PaymentMode.MOBILE, cartState.getTotal().doubleValue());
        paymentComponent.setListener(valuechangePaymentListener);
        return paymentComponent;
    }

    private final ComponentRenderer<Component, ReceiptItem> receiptRowRenderer = new ComponentRenderer<>(
            receiptItem -> {
                Div row = new Div();
                Div detail = new Div();
                detail.setWidthFull();
                detail.addClassNames(
                        LumoUtility.Display.FLEX,
                        LumoUtility.JustifyContent.BETWEEN,
                        LumoUtility.Padding.SMALL);

                Span itemNameTxt = new Span(receiptItem.getName());
                itemNameTxt.getStyle().setFontSize("12px").setFontWeight("800px");
                Span itemNetTotalTxt = new Span(utils.cedisCurrencyFormat(receiptItem.getTotalPrice()));
                itemNetTotalTxt.getStyle().setFontSize("12px").setFontWeight("400px");
                detail.add(itemNameTxt, itemNetTotalTxt);
                detail.setWidth("90%");
                detail.getStyle().setPadding("0px");

                Span itemSummationTxt = new Span(
                        String.format("%s * ¢%s", receiptItem.getQuantity(), receiptItem.getUnitPrice()));
                itemSummationTxt.addClassNames(LumoUtility.TextColor.SECONDARY);
                itemSummationTxt.getStyle().setFontSize("11px");

                row.add(detail, new HorizontalLayout(itemSummationTxt));
                row.getStyle().setBorderBottom("1px solid darkgray").setPadding("5px");
                row.setWidthFull();
                return row;
            });

    private String discountCalculator() {
        return String.format("Discount (%.0f%%) | Total (%.00f)",
                cartState.getDiscount(),
                cartState.getTotal());
    }

     private final SimplePaymentListener valuechangePaymentListener = new SimplePaymentListener() {

            @Override
            public void onAmountChanged(Double amount) {
                orchestrator.onAmountChanged(amount);
            }

            @Override
            public void onModeChanged(PaymentMode mode) {
               orchestrator.onModeChanged(mode);
            }

            @Override
            public void onConfirm(Double amount, PaymentMode mode) {
               orchestrator.process(amount, mode);
            }

            @Override
            public void onDescriptionChanged(String note) {
                orchestrator.onNoteChanged(note);
            }

            @Override
            public void onMobileNumberChanged(String mobileNumber) {
                orchestrator.onMobileNumberChanged(mobileNumber);
            }
        };

}
