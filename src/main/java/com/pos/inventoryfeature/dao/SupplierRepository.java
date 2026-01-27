package com.pos.inventoryfeature.dao;

import com.pos.inventoryfeature.dto.SupplierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    @Query(
            """
             SELECT s FROM Supplier s
             where (:searchTerm IS NULL
                     OR LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%') ) )
                     OR LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%') )
             """)
    Page<Supplier> findAll(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("""
            SELECT COUNT(s)
            FROM Supplier s
            WHERE (:filter IS NULL
                   OR LOWER(s.name) LIKE LOWER(CONCAT('%', :filter, '%'))
                   OR LOWER(s.email) LIKE LOWER(CONCAT('%', :filter, '%')))
            """)
    long count(@Param("filter") String filter);


}
