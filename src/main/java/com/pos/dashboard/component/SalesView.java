package com.pos.dashboard.component;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;

import com.pos.retailfeature.dao.sale.Sale;
import com.pos.retailfeature.dao.sale.SaleFilter;
import com.pos.retailfeature.dao.sale.SaleRepository;
import com.pos.retailfeature.service.providers.SaleDataProvider;
import com.pos.shared.StaticUtils;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@VaadinSessionScope
@Slf4j
public class SalesView extends VerticalLayout {

    private final SaleRepository saleRepository;

    private final SaleFilter filter = new SaleFilter();
    private final SaleDataProvider dataProvider;
    private Span totalSpan = new Span("Total: ");

    private final Grid<Sale> grid = new Grid<>(Sale.class, false);

    private Utils utils = new Utils();

    @Value(value = "${app.currency}")
    private String currency;

    public SalesView(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
        this.dataProvider = new SaleDataProvider(saleRepository);

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        dataProvider.setFilter(filter);

        add(buildFilterBar(), buildGrid());

    }

    private Component buildFilterBar() {

        TextField idField = new TextField("Sale ID");
        idField.setClearButtonVisible(true);

        TextField mobileField = new TextField("Mobile Money No.");
        mobileField.setClearButtonVisible(true);

        TextField performedByField = new TextField("Performed By");
        performedByField.setClearButtonVisible(true);

        DatePicker fromDate = new DatePicker("From");
        DatePicker toDate = new DatePicker("To");

        idField.addValueChangeListener(e -> {
            filter.setId(e.getValue());
            refreshSummary();
        });

        mobileField.addValueChangeListener(e -> {
            filter.setMobileMoneyNumber(e.getValue());
            refreshSummary();
        });

        performedByField.addValueChangeListener(e -> {
            filter.setPerformedBy(e.getValue());
            refreshSummary();
        });

        fromDate.addValueChangeListener(e -> {
            filter.setFromDate(e.getValue());
            refreshSummary();
        });

        toDate.addValueChangeListener(e -> {
            filter.setToDate(e.getValue());
            refreshSummary();
        });

        HorizontalLayout filters = new HorizontalLayout(
                idField,
                mobileField,
                performedByField,
                fromDate,
                toDate);

        filters.setWidthFull();
        filters.setAlignItems(Alignment.END);
        filters.setFlexGrow(1, idField, mobileField, performedByField);

        return filters;
    }

    private Component buildGrid() {

        grid.setSizeFull();
        grid.setDataProvider(dataProvider);

        grid.addColumn(Sale::getId)
                .setHeader("Receipt No.")
                .setAutoWidth(true);

        grid.addColumn(Sale::getPaymentMethod)
                .setHeader("Payment Mode")
                .setAutoWidth(true);

        grid.addColumn(Sale::getMobileMoneyNumber)
                .setHeader("Mobile No.")
                .setAutoWidth(true);

        grid.addColumn(Sale::getPerformedBy)
                .setHeader("Performed By")
                .setAutoWidth(true);

        grid.addColumn(Sale::getTransactionDate)
                .setHeader("Transaction Date")
                .setAutoWidth(true);

        grid.addColumn(arg0 -> StaticUtils.cedisNumberFormatter(arg0.getAmountTendered()))
                .setHeader("Tendered")
                .setAutoWidth(true);

        Span totalHeaderSpan = new Span("Cost");
        totalHeaderSpan.getStyle().setFontWeight("bold").setFontSize("15px");
        Grid.Column<Sale> totalColumn = grid.addColumn(arg0 -> StaticUtils.cedisNumberFormatter(arg0.getTotalAmount()))
                .setHeader(totalHeaderSpan)
                .setKey("totalAmount")
                .setAutoWidth(true);

        grid.addColumn(Sale::getChangeGiven)
                .setHeader("Change")
                .setAutoWidth(true);

        grid.setColumnReorderingAllowed(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);


        GridContextMenu<Sale> menu =  grid.addContextMenu();
        menu.addItem(utils.visualComponent("View", VaadinIcon.EYE), event -> {
            Sale sale = event.getItem().get();
            log.info("Sale id {} -> cost {} ", sale.getId(), sale.getTotalAmount());

            new DialogSaleItemView(sale).open();

        });
        menu.addSeparator();

        // grid.getColumns().stream().forEach(col -> log.info(col.getKey()));

        FooterRow footerRow = grid.appendFooterRow();

        totalSpan.getStyle()
                .setFontWeight("bold")
                .setFontSize("var(--lumo-font-size-m)");

        footerRow.getCell(totalColumn).setComponent(totalSpan);
        refreshSummary();
        return grid;
    }

    @PostConstruct
    private void init() {
        refreshSummary();
    }

    private void refreshSummary() {
        log.info("refreshing to sell");
        BigDecimal total = dataProvider.getTotalAmount(filter);
        log.info("refreshing to sell total -> {} ", total);
        totalSpan.setText("Total: " + "¢" + " " + total);
    }

}
