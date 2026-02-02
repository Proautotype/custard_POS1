package com.pos.dashboard.ui;

import com.pos.base.ui.HasDynamicHeader;

import com.pos.retailfeature.dao.sale.SaleFilter;
import com.pos.retailfeature.dao.sale.SaleRepository;

import com.pos.base.ui.MainLayout;
import com.pos.dashboard.component.SalesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard")
@PermitAll
public class Dashboard extends VerticalLayout implements HasDynamicHeader {

    private final SaleRepository saleRepository;

    Dashboard(SaleRepository saleRepository) {

        setSizeFull();

        this.saleRepository = saleRepository;
        add(new H1("Welcome to Dashboard"));

        SalesView salesView = new SalesView(saleRepository);
        salesView.setHeightFull();

        add(salesView);

    }

    @Override
    public Component[] getHeaderActions() {
        return new Component[] {
                new Button("Create Order"),
                new Button("Export")
        };
    }
}
