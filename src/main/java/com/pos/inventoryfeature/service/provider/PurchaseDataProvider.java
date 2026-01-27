package com.pos.inventoryfeature.service.provider;

import com.pos.inventoryfeature.dao.Purchase;
import com.pos.inventoryfeature.dao.PurchaseRepository;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class PurchaseDataProvider extends AbstractBackEndDataProvider<Purchase, String> {

    private final PurchaseRepository repository;

    @Override
    protected Stream<Purchase> fetchFromBackEnd(Query<Purchase, String> query) {
        return repository.findAll().stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Purchase, String> query) {
        return 0;
    }

    @Override
    public Object getId(Purchase item) {
        return super.getId(item);
    }
}
