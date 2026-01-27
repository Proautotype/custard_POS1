package com.pos.inventoryfeature;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.stream.Stream;

public class StocksDataProvider  extends AbstractBackEndDataProvider<StockEntryDto, String> {
    @Override
    protected Stream<StockEntryDto> fetchFromBackEnd(Query<StockEntryDto, String> query) {
        return Stream.empty();
    }

    @Override
    protected int sizeInBackEnd(Query<StockEntryDto, String> query) {
        return 0;
    }
}
