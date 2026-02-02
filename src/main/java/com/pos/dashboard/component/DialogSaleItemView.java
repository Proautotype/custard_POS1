package com.pos.dashboard.component;

import org.springframework.beans.factory.annotation.Value;

import com.pos.retailfeature.dao.product.Product;
import com.pos.retailfeature.dao.sale.Sale;
import com.pos.retailfeature.dao.sale.SaleItem;
import com.pos.shared.StaticUtils;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.ListDataProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DialogSaleItemView extends Dialog {

    @Value  ("${app.currency}")
    private String currency;

    private Sale sale;
    private ListDataProvider<SaleItem> saleItemDataProvider;

    public DialogSaleItemView(Sale sale){
        this.sale = sale;
        saleItemDataProvider = new ListDataProvider<>(this.sale.getItems());
        Grid<SaleItem> grid = new Grid<>(SaleItem.class, false);
        

        grid.setDataProvider(saleItemDataProvider); 

        grid.addColumn(arg0 -> {
            return arg0.getProduct().getName();
        }).setHeader("Product");

        grid.addColumn(arg0 -> {
            return arg0.getQuantity();
        }).setHeader("Quantity");

        grid.addColumn(arg0 -> {
            return StaticUtils.cedisNumberFormatter(arg0.getPriceAtSale());
        }).setHeader("PriceAtSale");

        Column<SaleItem> cost = grid.addColumn(arg0 -> {
            return StaticUtils.cedisNumberFormatter(arg0.getSubtotal());
        })
        .setHeader("Cost");

        FooterRow footerRow = grid.appendFooterRow();
        footerRow.getCell(cost).setComponent(new Span("Total " + StaticUtils.cedisNumberFormatter(sale.getTotalAmount())));


        add(new H3(String.format("Sale (%s) ", sale.getId())),grid);
        setWidth("40%");
    }

}
