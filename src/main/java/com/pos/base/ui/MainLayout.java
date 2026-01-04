package com.pos.base.ui;

import com.pos.base.ui.component.AppIcon;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

@Layout
@PermitAll
public final class MainLayout extends AppLayout implements AfterNavigationObserver {

    private final H1 title = new H1();
    private final Div actions = new Div();

    MainLayout() {
        var appNavs = new HorizontalLayout(new DrawerToggle(), title, new AppIcon(), actions);
        appNavs.setWidthFull();
        appNavs.setHeightFull();
        appNavs.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);
        addToNavbar(appNavs);
        setPrimarySection(Section.DRAWER);

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);
        Button logout = new Button("Logout");
        logout.setPrefixComponent(VaadinIcon.SIGN_OUT.create());
        logout.addClickListener(event -> {
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setCloseOnEsc(true);
            confirmDialog.setText("Are you sure you want logout, " +
                    "no further usage of the system until next login");

            confirmDialog.addConfirmListener(confirmEvent -> {
                logout();
                confirmDialog.close();
            });
            confirmDialog.addRejectListener(confirmEvent -> {
                confirmDialog.close();
            });
            confirmDialog.open();
        });

        addToDrawer(scroller, logout);
        setPrimarySection(Section.DRAWER);
        actions.setWidthFull();
    }

    public static void logout() {
// Invalidate the current session
        VaadinSession.getCurrent().getSession().invalidate();

// Redirect to the login page or a custom logout page
        UI.getCurrent().getPage().setLocation("/login");
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(
                new SideNavItem("Dashboard", "/",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Shop", "/sell",
                        VaadinIcon.SHOP.create()),
                new SideNavItem("Checkout", "/checkout",
                        VaadinIcon.CHECK_CIRCLE_O.create()),
                new SideNavItem("Inventory", "/inventory",
                        VaadinIcon.INVOICE.create())
        );
        return nav;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        event.getActiveChain().stream()
                .filter(HasDynamicHeader.class::isInstance)
                .map(HasDynamicHeader.class::cast)
                .findFirst()
                .ifPresent(view -> {
//                    title.setText(view.getHeaderTitle());
                    actions.removeAll();
                    actions.add(view.getHeaderActions());
                });
    }
}
