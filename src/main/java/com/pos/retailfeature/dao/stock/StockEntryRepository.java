package com.pos.retailfeature.dao.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {
    // Finds stock entries for a specific product expiring before a certain date
    @Query("SELECT s FROM StockEntry s WHERE s.expiryDate <= :thresholdDate AND (s.quantityReceived - s.quantitySoldFromThisBatch) > 0")
    List<StockEntry> findExpiringStock(@Param("thresholdDate") LocalDate thresholdDate);

    // Finds all stock received on a specific date for accountability audits
    List<StockEntry> findByArrivalDate(LocalDate arrivalDate);
}
