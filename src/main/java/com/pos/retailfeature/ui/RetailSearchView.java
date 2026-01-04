package com.pos.retailfeature.ui;

import com.pos.base.ui.component.ChipGroup;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.function.Consumer;

public class RetailSearchView extends VerticalLayout {
    RetailSearchView(Consumer<String> onSearch) {
        TextField searchField = inputTextField(onSearch);

        HorizontalLayout prereq = new HorizontalLayout();
        prereq.setWidthFull(); //www
        ChipGroup chipGroup =
                new ChipGroup(List.of("All", "Prescribed (Rx)", "First aid"));
        prereq.add(chipGroup);

        add(new H6("Search/Filter"), searchField, prereq);
        addClassNames(LumoUtility.BoxShadow.MEDIUM);
        getStyle().setBoxShadow("rgba(17, 12, 46, 0.15) 0px 48px 100px 0px");
    }

    TextField inputTextField(Consumer<String> valueListener) {
        TextField searchField = new TextField();
        searchField.setWidthFull();
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchField.addValueChangeListener(ooa -> {
            valueListener.accept(ooa.getValue());
        });
        return searchField;
    }
}
