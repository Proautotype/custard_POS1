package com.pos.inventoryfeature;

import com.pos.inventoryfeature.service.StockEntryService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;

@SpringComponent
@UIScope
@RequiredArgsConstructor
public class BlueView extends Dialog {
    private final StockEntryService s;


}
