package com.pos.retailfeature.dao.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenericProductRepository extends JpaRepository<GenericProduct, String> {
    List<GenericProduct> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select count(g) from GenericProduct g where g.name = :name")
    long countByName(@Param("name") String name);

    Optional<GenericProduct> findByName(String name);
}
