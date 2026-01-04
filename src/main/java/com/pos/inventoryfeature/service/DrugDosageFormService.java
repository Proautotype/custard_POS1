package com.pos.inventoryfeature.service;

import com.pos.inventoryfeature.dao.DrugDosageForm;
import com.pos.inventoryfeature.dao.DrugDosageFormRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DrugDosageFormService {

    private final DrugDosageFormRepository repository;

    public DrugDosageFormService(DrugDosageFormRepository repository) {
        this.repository = repository;
    }

    public Page<DrugDosageForm> findAll(
            String filter,
            Pageable pageable
    ) {
        if (filter == null || filter.isBlank()) {
            return repository.findAll(pageable);
        }
        return repository.findByNameContainingIgnoreCase(filter, pageable);
    }

    public long count(String filter) {
        if (filter == null || filter.isBlank()) {
            return repository.count();
        }
        return repository.countByNameContainingIgnoreCase(filter);
    }
}