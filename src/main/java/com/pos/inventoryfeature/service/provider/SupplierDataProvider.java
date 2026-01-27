package com.pos.inventoryfeature.service.provider;

import com.pos.inventoryfeature.dao.SupplierRepository;
import com.pos.inventoryfeature.dto.SupplierDto;
import com.pos.inventoryfeature.mapper.SupplierMapper;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SupplierDataProvider extends AbstractBackEndDataProvider<SupplierDto, String> {

    private final SupplierRepository supplierRepository;
    public String filter = "";

    @Override
    protected Stream<SupplierDto> fetchFromBackEnd(Query<SupplierDto, String> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();
        int pageNumber = offset / limit;
        Pageable pageable = PageRequest.of(
                pageNumber, limit
        );
        return supplierRepository
                .findAll(filter, pageable)
                .stream().map(SupplierMapper::toDto);
    }

    @Override
    protected int sizeInBackEnd(Query<SupplierDto, String> query) {
        return Math.toIntExact(supplierRepository.count(filter));
    }

}
