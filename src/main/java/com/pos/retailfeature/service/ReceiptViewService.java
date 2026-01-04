package com.pos.retailfeature.service;

import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;

public interface ReceiptViewService {
    void updateTotalOnReceipt(ListDataProvider<ReceiptItem> provider);
    VerticalLayout renderView();
}
