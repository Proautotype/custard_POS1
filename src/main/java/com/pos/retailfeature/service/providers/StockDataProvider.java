package com.pos.retailfeature.service.providers;

import com.pos.inventoryfeature.dao.stock.ProductStockSummary;
import com.pos.inventoryfeature.dao.stock.StockEntryRepository;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockDataProvider extends AbstractBackEndDataProvider<ProductStockSummary, String> {

    private final StockEntryRepository stockEntryRepository;
    private String searchTerm = "";

    @Override
    protected Stream<ProductStockSummary> fetchFromBackEnd(Query<ProductStockSummary, String> query) {
        try {
            PageRequest pageable = PageRequest.of(
                query.getOffset() / query.getLimit(),
                query.getLimit()
        );
        return stockEntryRepository.findStockEntriesGroupedByProduct(searchTerm, pageable).stream();
        } catch (Exception e) {
            log.error("StockProvder Error  {}", e.getMessage());
            // log.error(e.getLocalizedMessage(), e);
            return Stream.empty();
        }
    }

    @Override
    protected int sizeInBackEnd(Query<ProductStockSummary, String> query) {
        return stockEntryRepository.count(searchTerm);
    }

    public void setSearch(String search){
        this.searchTerm = search;
    }
}
