package com.pos.base.ui.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class RoundedDiv extends Div {
    public RoundedDiv(String title){
        var bulb = new Div();
        bulb.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER,LumoUtility.JustifyContent.CENTER);
        bulb.getStyle().setBackgroundColor("#7fac38").setBorderRadius("50px").setPadding("4px");

        H6 label = new H6(title);
        label.getStyle().setColor("#ffffff");

        getStyle().setBorder("1px solid white");

        this.add(bulb, label);
    }
}
