package com.pos.inventoryfeature.dao;

import com.pos.inventoryfeature.dao.stock.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
