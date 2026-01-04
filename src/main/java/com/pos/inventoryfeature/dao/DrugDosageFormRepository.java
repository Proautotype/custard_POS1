package com.pos.inventoryfeature.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DrugDosageFormRepository
        extends JpaRepository<DrugDosageForm, UUID> {

    Page<DrugDosageForm> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    long countByNameContainingIgnoreCase(String name);
}