package com.pos.inventoryfeature.service.provider;

import com.pos.inventoryfeature.dao.DrugDosageForm;
import com.pos.inventoryfeature.service.DrugDosageFormService;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DrugDosageFormDataProvider
        extends AbstractBackEndDataProvider<DrugDosageForm, String> {

    private final DrugDosageFormService service;

    @Override
    protected Stream<DrugDosageForm> fetchFromBackEnd(
            Query<DrugDosageForm, String> query
    ) {
        PageRequest pageRequest = PageRequest.of(
                query.getOffset() / query.getLimit(),
                query.getLimit()
        );

        return service
                .findAll(query.getFilter().orElse(null), pageRequest)
                .stream();
    }

    @Override
    protected int sizeInBackEnd(
            Query<DrugDosageForm, String> query
    ) {
        return (int) service.count(query.getFilter().orElse(null));
    }
}