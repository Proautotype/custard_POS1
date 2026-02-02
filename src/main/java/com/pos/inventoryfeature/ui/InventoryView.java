package com.pos.inventoryfeature.ui;

import com.pos.base.ui.HasDynamicHeader;
import com.pos.inventoryfeature.StockEntryView;
import com.pos.inventoryfeature.dao.Purchase;
import com.pos.inventoryfeature.service.StockEntryService;
import com.pos.inventoryfeature.service.provider.PurchaseDataProvider;
import com.pos.shared.StaticUtils;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.ObjectProvider;

@Slf4j
@UIScope
@PageTitle("inventory")
@Route("/inventory")
@RolesAllowed("ADMIN")
@RequiredArgsConstructor
public class InventoryView extends VerticalLayout implements HasDynamicHeader {
    private final StockEntryView stockEntryView;
    private final PurchaseDataProvider purchaseDataProvider;
    private final Utils utils;

    NumberField productSellingPriceField = new NumberField("Selling Price");

    @PostConstruct
    private void init() {
        setupStock();
        add(titleArea(), searchArea(), purchaseView(), stockEntryView);
    }

    private void setupStock() {

        productSellingPriceField.setStepButtonsVisible(true);
        productSellingPriceField.setValue(0.0);
    }

    private Component titleArea() {
        VerticalLayout content = new VerticalLayout();
        H1 header = new H1("Inventory Management");
        HorizontalLayout statBoard = new HorizontalLayout();
        statBoard.getStyle().set("gap", "5px").setWidth("100%");

        Span totalItems = new Span();
        totalItems.add(VaadinIcon.ARCHIVES.create(), new Span("Total items: (12,000)"));
        totalItems.getElement().getThemeList().add("badge primary");

        Span stock = new Span();
        totalItems.add(VaadinIcon.TRENDING_UP.create(), new Span("Stocks: (20)"));
        totalItems.getElement().getThemeList().add("badge secondary");

        statBoard.add(totalItems, stock);
        content.setJustifyContentMode(JustifyContentMode.BETWEEN);
        content.add(header, statBoard);
        return content;
    }

    private Component searchArea() {
        HorizontalLayout content = new HorizontalLayout();
        TextField searchField = new TextField();
        searchField.setClearButtonVisible(true);
        searchField.setPlaceholder("Search by Product Name, SKU, or supplier");
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.setSuffixComponent(VaadinIcon.OPTIONS.create());
        searchField.setWidth("60%");

        HorizontalLayout actions = new HorizontalLayout();
        Button createBtn = new Button("Create Stock");
        createBtn.setPrefixComponent(VaadinIcon.PLUS.create());
        createBtn.addClickListener(event -> {
            stockEntryView.open();
        });

        Button createCategory = new Button("Category");
        createBtn.setPrefixComponent(VaadinIcon.PLUG.create());

        actions.add(createCategory, createBtn);
        content.add(searchField, actions);
        content.getStyle().setBoxShadow("rgba(0, 0, 0, 0.16) 0px 1px 4px").setWidth("100%").setPadding("10px");
        return content;
    }

    private Component purchaseView() {
        VerticalLayout contentLayout = new VerticalLayout();

        Grid<Purchase> purchaseGrid = new Grid<>();
        purchaseGrid.setMultiSort(true);
        purchaseGrid.setHeightFull();
        purchaseGrid.setDataProvider(purchaseDataProvider);

        purchaseGrid.addColumn(Purchase::getId).setHeader("ID");
        purchaseGrid.addColumn(Purchase::getSupplierInvoiceNumber).setHeader("Invoice Number");
        purchaseGrid
        .addColumn(args -> {
            
            return args.getArrivalDate() == null ? "Not Recorded" : args.getArrivalDate().format(DateTimeFormatter.BASIC_ISO_DATE);

        })
        .setHeader("Arrival Date");
        purchaseGrid.addColumn(args-> args.getStockEntries().size()).setHeader("Number of Items");
        purchaseGrid.addColumn(args ->  {
            return StaticUtils.cedisNumberFormatter(
                args.getTotalInvoiceAmount() != null ? args.getTotalInvoiceAmount() : BigDecimal.valueOf(0) 
            );
        }).setHeader("Cost Amount");

        contentLayout.add(purchaseGrid);
        purchaseGrid.setSizeFull();
        contentLayout.setWidthFull();
        contentLayout.setHeight("400px");
        return contentLayout;
    }

    @Override
    public Component[] getHeaderActions() {
        return new Component[0];
    }
}
