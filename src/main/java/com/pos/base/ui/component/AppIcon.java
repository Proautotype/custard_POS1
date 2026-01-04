package com.pos.base.ui.component;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class AppIcon extends Div {
    public AppIcon(){
        setHeightFull();
        Icon icon = LumoIcon.PLAY.create();
        icon.setSize("20px");
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.START);
        add(icon, new Text("CustardPOS"));
    }
}
