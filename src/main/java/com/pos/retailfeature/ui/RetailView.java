package com.pos.retailfeature.ui;

import com.pos.base.ui.HasDynamicHeader;
import com.pos.core.service.CartState;
import com.pos.retailfeature.service.providers.ProductDataProvider;
import com.pos.retailfeature.subcomponent.DisplayView;
import com.pos.retailfeature.subcomponent.ReceiptView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Route("/sell")
@PageTitle("Shop")
@PermitAll
public class RetailView extends VerticalLayout implements HasDynamicHeader {

    private final ProductDataProvider dataProvider;
    private int currentColumns = 2;
    private final DisplayView displayView;
    private final ReceiptView receiptView;

    RetailView(ProductDataProvider dataProvider, CartState cartState) {
        this.dataProvider = dataProvider;

        setWidthFull();
        setHeightFull();

        displayView = new DisplayView(dataProvider, cartState);

        VerticalLayout main = new VerticalLayout();
        HorizontalLayout content = new HorizontalLayout();
        VerticalLayout leftSection = leftSection();
        leftSection.setMaxWidth("72%");

        receiptView = new ReceiptView(cartState);
        receiptView.setMaxWidth("28%");

        VerticalLayout footer = new VerticalLayout();

        content.add(leftSection, receiptView);
        content.setSpacing(false);
        main.add(content, footer);
        main.setWidthFull();
        main.setHeightFull();

        content.setWidthFull();
        content.setHeightFull();
        add(main);
    }

    @Override
    public Component[] getHeaderActions() {
        LocalDateTime date = LocalDateTime.now();
        Div moment = new Div();
        H6 timeText = new H6(date.format(DateTimeFormatter.ofPattern("mm:ss a")));
        timeText.getStyle().setFontSize("11px");
        H6 dateTxt = new H6(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
        dateTxt.getStyle().setFontSize("10px");

        moment.add(timeText, dateTxt);
        moment.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.END);

        Div userProfile = new Div();
        H6 username = new H6("John Doe");
        username.getStyle().setFontSize("11px");
        H6 jobDesc = new H6("Pharmacist");
        jobDesc.getStyle().setFontSize("10px");

        userProfile.add(username, jobDesc);
        userProfile.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.END);

        Div seperator = new Div();
        seperator.setHeightFull();
        seperator.getStyle().setPadding("0.5px").setBackgroundColor("gray");

        Avatar avatarBasic = new Avatar("WA");

        var layout = new HorizontalLayout(
                moment,
                createIcon(VaadinIcon.BELL_O, "0"),
                seperator,
                userProfile,
                avatarBasic
        );
        layout.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.END,
                LumoUtility.Padding.Right.MEDIUM
        );
        layout.setWidthFull();
        layout.setHeightFull();
        return new Component[]{layout};
    }

    private VerticalLayout leftSection() {

        RetailSearchView retailSearchView = new RetailSearchView(this::onSearch);
        VerticalLayout section = new VerticalLayout();

        section.add(retailSearchView, displayView);
        section.setWidthFull();
        section.setHeightFull();

        initResponsiveBehavior();
        applyFilter("");

        return section;
    }

    private void initResponsiveBehavior() {
        UI ui = UI.getCurrent();

        ui.getPage().retrieveExtendedClientDetails(details ->
                updateColumns(details.getBodyClientWidth())
        );

        ui.getPage().addBrowserWindowResizeListener(e ->
                updateColumns(e.getWidth())
        );
    }

    private void updateColumns(int widthPx) {
        int cols = calculateColumns(widthPx);
        if (cols != currentColumns) {
            currentColumns = cols;
//            displayView.setDataProvider(dataProvider, currentColumns);
        }
    }

    private void onSearch(String query) {
        log.info("query search {} ", query);
        applyFilter(query);
    }

    private void applyFilter(String query) {
        dataProvider.setFilter(query);
        displayView.refresh();
    }

    private Icon createIcon(VaadinIcon vaadinIcon, String label) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        return icon;
    }

    private int calculateColumns(int widthPx) {
        if (widthPx < 600) return 2;
        if (widthPx < 900) return 3;
        return 4;
    }
}
