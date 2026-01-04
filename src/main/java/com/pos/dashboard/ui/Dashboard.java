package com.pos.dashboard.ui;

import com.pos.base.ui.HasDynamicHeader;
import com.pos.base.ui.MainLayout;
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

    Dashboard() {
        add(new H1("Welcome to Dashboard"));
    }

    @Override
    public Component[] getHeaderActions() {
        return new Component[]{
                new Button("Create Order"),
                new Button("Export")
        };
    }
}
