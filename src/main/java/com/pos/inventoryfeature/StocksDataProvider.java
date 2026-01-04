package com.pos.inventoryfeature;

import com.pos.retailfeature.dto.ProductDto;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import java.util.stream.Stream;

public class StocksDataProvider  extends AbstractBackEndDataProvider<ProductDto, String> {
    @Override
    protected Stream<ProductDto> fetchFromBackEnd(Query<ProductDto, String> query) {
        return Stream.empty();
    }

    @Override
    protected int sizeInBackEnd(Query<ProductDto, String> query) {
        return 0;
    }
}
