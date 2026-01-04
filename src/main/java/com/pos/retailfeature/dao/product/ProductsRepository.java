package com.pos.retailfeature.dao.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, String> {
    @Query("""
        SELECT COALESCE(SUM(s.quantityReceived - s.quantitySoldFromThisBatch), 0)
        FROM StockEntry s
        JOIN s.product p
        WHERE p.genericProduct.id = :genericId
    """)
    Integer sumStockByGenericId(@Param("generic_id") String genericId);

    @Query("""
        SELECT p FROM Product p 
        LEFT JOIN p.genericProduct g 
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
        OR LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    Page<Product> searchProducts(@Param("searchTerm") String searchTerm, Pageable pageable);

    /*
         @Query("""
        SELECT COUNT(s) FROM SaleItemEntity s
        WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    * */
    @Query("""
        SELECT COUNT(p) FROM Product p 
        LEFT JOIN p.genericProduct g 
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
        OR LOWER(g.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    long countSearch(@Param("searchTerm") String searchTerm);

    // Optional: A more specific search that filters by Category as well
    @Query("""
        SELECT p FROM Product p 
        JOIN p.genericProduct g 
        WHERE g.category = :category 
        AND LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    """)
    Page<Product> searchByCategory(@Param("searchTerm") String searchTerm,
                                   @Param("category") String category,
                                   Pageable pageable);

}
