package com.pos.retailfeature.service.providers;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.pos.retailfeature.dao.sale.Sale;
import com.pos.retailfeature.dao.sale.SaleFilter;
import com.pos.retailfeature.dao.sale.SaleRepository;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;

import jakarta.persistence.criteria.Predicate;

@Component
public class SaleDataProvider extends AbstractBackEndDataProvider<Sale, SaleFilter> {

    private SaleFilter filter;
    private final SaleRepository saleRepository;

    public SaleDataProvider(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    protected Stream<Sale> fetchFromBackEnd(Query<Sale, SaleFilter> query) {
        SaleFilter filter = query.getFilter().orElse(new SaleFilter());

        // Paging
        int offset = query.getOffset();
        int limit = query.getLimit();
        PageRequest pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "transactionDate"));

        return saleRepository
                .findAll((root, cq, cb) -> {

                    List<Predicate> predicates = new ArrayList<>();

                    if (hasText(filter.getId())) {
                        predicates.add(cb.equal(root.get("id"), filter.getId()));
                    }

                    if (hasText(filter.getMobileMoneyNumber())) {
                        predicates.add(cb.like(
                                root.get("mobileMoneyNumber"),
                                "%" + filter.getMobileMoneyNumber() + "%"));
                    }

                    if (hasText(filter.getPerformedBy())) {
                        predicates.add(cb.like(
                                cb.lower(root.get("performedBy")),
                                "%" + filter.getPerformedBy().toLowerCase() + "%"));
                    }

                    if (filter.getFromDate() != null) {
                        predicates.add(cb.greaterThanOrEqualTo(
                                root.get("transactionDate"),
                                filter.getFromDate().atStartOfDay()));
                    }

                    if (filter.getToDate() != null) {
                        predicates.add(cb.lessThanOrEqualTo(
                                root.get("transactionDate"),
                                filter.getToDate().atTime(LocalTime.MAX)));
                    }

                    return cb.and(predicates.toArray(new Predicate[0]));
                }, pageable)
                .stream();

    }

    @Override
    protected int sizeInBackEnd(Query<Sale, SaleFilter> query) {
        SaleFilter filter = query.getFilter().orElse(new SaleFilter());

        return (int) saleRepository.count((root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (hasText(filter.getId())) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (hasText(filter.getMobileMoneyNumber())) {
                predicates.add(cb.like(
                        root.get("mobileMoneyNumber"),
                        "%" + filter.getMobileMoneyNumber() + "%"));
            }

            if (hasText(filter.getPerformedBy())) {
                predicates.add(cb.like(
                        cb.lower(root.get("performedBy")),
                        "%" + filter.getPerformedBy().toLowerCase() + "%"));
            }

            if (filter.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("transactionDate"),
                        filter.getFromDate().atStartOfDay()));
            }

            if (filter.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("transactionDate"),
                        filter.getToDate().atTime(LocalTime.MAX)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public void setFilter(SaleFilter filter) {
        this.filter = filter;
    }

    public BigDecimal getTotalAmount(SaleFilter filter) {

        return saleRepository.sumTotalAmount(
                emptyToNull(filter.getMobileMoneyNumber()),
                emptyToNull(filter.getPerformedBy()),
                filter.getFromDate() != null ? filter.getFromDate().atStartOfDay() : null,
                filter.getToDate() != null ? filter.getToDate().atTime(LocalTime.MAX) : null);
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

}
