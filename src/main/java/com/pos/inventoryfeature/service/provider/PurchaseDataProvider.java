package com.pos.inventoryfeature.service.provider;

import com.pos.inventoryfeature.dao.Purchase;
import com.pos.inventoryfeature.dao.PurchaseRepository;
import com.pos.inventoryfeature.dto.PurchaseFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class PurchaseDataProvider extends AbstractBackEndDataProvider<Purchase, PurchaseFilter> {

    private final PurchaseRepository repository;

    PurchaseFilter filter;

    @Override
    protected Stream<Purchase> fetchFromBackEnd(Query<Purchase, PurchaseFilter> query) {

        if (filter == null) {
            filter = new PurchaseFilter();
        }

        PurchaseFilter filter = new PurchaseFilter();
        // Paging
        int offset = query.getOffset();
        int limit = query.getLimit();
        PageRequest pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "arrivalDate"));

        return repository
                .findAll((root, cq, cb) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (filter.getStartDate() != null && filter.getEndDate() != null) {
                        predicates.add(
                                cb.between(
                                        root.get("arrivalDate"),
                                        filter.getStartDate().atStartOfDay(),
                                        filter.getEndDate().atTime(LocalTime.MAX))
                                    );
                    }

                    if (filter.getMinimumAmount() != null) {
                        cb.greaterThan(
                                root.get("total_invoice_amount"),
                                filter.getMinimumAmount()
                            );
                    }

                    if (filter.getMaximumAmount() != null) {
                        cb.lessThan(
                                root.get("total_invoice_amount"),
                                filter.getMaximumAmount());
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                }, pageable)
                .stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Purchase, PurchaseFilter> query) {

        if (filter == null) {
            filter = new PurchaseFilter();
        }

        return (int) repository.count((root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getStartDate() != null && filter.getEndDate() != null) {
                predicates.add(
                        cb.between(
                                root.get("arrivalDate"),
                                filter.getStartDate().atStartOfDay(),
                                filter.getEndDate().atTime(LocalTime.MAX)));
            }

            if (filter.getMinimumAmount() != null) {
                cb.greaterThan(
                        root.get("total_invoice_amount"),
                        filter.getMinimumAmount());
            }

            if (filter.getMaximumAmount() != null) {
                cb.lessThan(
                        root.get("total_invoice_amount"),
                        filter.getMaximumAmount());
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    @Override
    public Object getId(Purchase item) {
        return super.getId(item);
    }

}
