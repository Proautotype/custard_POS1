package com.pos.retailfeature.dao.sale;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String>, JpaSpecificationExecutor<Sale> {
    @Query("""
            select coalesce(sum(s.totalAmount), 0)
            from Sale s
            where (s.mobileMoneyNumber like %:mobile%)
              and (s.performedBy like concat('%', :performedBy, '%'))
              and (s.transactionDate >= :fromDate)
              and (s.transactionDate <= :toDate)
            """)
    BigDecimal sumTotalAmount(
            @Param("mobile") String mobile,
            @Param("performedBy") String performedBy,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}
