package com.pos.inventoryfeature.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, String>, JpaSpecificationExecutor<Purchase> {

    List<Purchase> findPurchaseByArrivalDate(LocalDate arrivalDate);
    List<Purchase> findPurchaseByArrivalDateBefore(LocalDate arrivalDateBefore);
    List<Purchase> findPurchaseByArrivalDateAfter(LocalDate arrivalDateAfter);

    List<Purchase> findPurchaseByArrivalDateBetween(LocalDate arrivalDateAfter, LocalDate arrivalDateBefore);


    Optional<Purchase> findPurchaseById(String id);
}
