package com.pos.retailfeature.events;

import com.pos.core.service.CartState;
import com.vaadin.flow.component.ComponentEvent;

public class CartChangeEvent extends ComponentEvent<CartState> {
    public CartChangeEvent(CartState source) {
        super(source, false);
    }
}
