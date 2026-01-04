package com.pos.core.domain.repositories;

import com.pos.core.domain.models.Sale;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// In a real application, this would extend JpaRepository
public interface SaleRepository {
    Sale save(Sale sale);
    Optional<Sale> findById(UUID id);
    List<Sale> findAll();
}