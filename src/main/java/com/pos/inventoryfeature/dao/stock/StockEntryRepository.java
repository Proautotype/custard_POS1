package com.pos.inventoryfeature.dao.stock;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockEntryRepository extends JpaRepository<StockEntry, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT s
                FROM StockEntry s
                WHERE s.product.id = :productId
                  AND (s.quantityReceived - s.quantitySoldFromThisBatch) > 0
                  AND s.expiryDate > :today
                ORDER BY s.expiryDate ASC, s.arrivalDate ASC
            """)
    List<StockEntry> findAvailableBatchesForUpdate(
            @Param("productId") String productId,
            @Param("today") LocalDate today
    );


    @Query(
            value = """  
                    SELECT new com.pos.inventoryfeature.dao.stock.ProductStockSummary(
                                p.id,
                                p.name,
                                SUM(s.quantityReceived - s.quantitySoldFromThisBatch),
                                p.currentSellingPrice
                            )
                    FROM StockEntry s
                    JOIN s.product p
                        WHERE (:searchTerm IS NULL
                           OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                           OR LOWER(p.id) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
                    
                    GROUP BY p.id, p.name
                    HAVING SUM(s.quantityReceived - s.quantitySoldFromThisBatch) > 0
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT p.id)
                    FROM StockEntry s
                    JOIN s.product p
                    WHERE (:searchTerm IS NULL
                                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                                OR LOWER(p.id) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                          )
                    """
    )
    Page<ProductStockSummary> findStockEntriesGroupedByProduct(
            @Param("searchTerm") String searchTerm, Pageable pageable
    );

    @Query(
            """
                SELECT COUNT(DISTINCT p.id)
                 FROM StockEntry s
                 JOIN s.product p
                 WHERE (:searchTerm IS NULL
                    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                    OR LOWER(p.id) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                 )
            """
    )
    int count(@Param("searchTerm") String searchTerm);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s 
        FROM StockEntry s
        WHERE s.product.id = :productId
            AND (s.quantityReceived - s.quantitySoldFromThisBatch) > 0 
            AND s.expiryDate > :today
        ORDER BY s.expiryDate ASC, s.arrivalDate ASC
         """)
    List<StockEntry> findAvailableBatchesForSale(
        @Param("productId") String productId,
        @Param("today") LocalDate today
    );

    @Query("""
        SELECT (SUM(s.quantityReceived - s.quantitySoldFromThisBatch))
        FROM StockEntry s
        WHERE s.product.id = :productId
            AND (s.quantityReceived - s.quantitySoldFromThisBatch) > 0
    """)
    Long calculateAvailableProductStockEntry(@Param("productId") String productId);

    @Query("""
            SELECT s FROM StockEntry s
            WHERE s.id = :batchId
        """)
    Optional<StockEntry> findBatchForAdjustment(@Param("batchId") Long batchId);

}
