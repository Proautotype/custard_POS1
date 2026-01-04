package com.pos.retailfeature.ui;

import com.pos.base.ui.component.AppIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class RetailHeader extends HorizontalLayout {
    RetailHeader() {
        setWidth("80%");
        setHeightFull();
        addClassNames(LumoUtility.Background.CONTRAST_90);

        add(new AppIcon());

    }
}
