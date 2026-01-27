package com.pos.inventoryfeature.dao.stock;

import com.pos.inventoryfeature.dao.Purchase;
import com.pos.inventoryfeature.dao.Supplier;
import com.pos.retailfeature.dao.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "stock_entry",
        indexes = {
                @Index(name = "idx_stock_product", columnList = "product_id"),
                @Index(name = "idx_stock_expiry", columnList = "expiryDate")
        }
)
@Getter
@Setter
public class StockEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantityReceived;
    private Integer quantitySoldFromThisBatch = 0;

    private LocalDate arrivalDate;
    private LocalDate expiryDate; // Critical for Pharmacy (Rx/OTC)

    private String batchNumber; // e.g., "LOT-2024-X"

    private BigDecimal unitCostPrice;       // What you paid the supplier for THIS batch
    private BigDecimal totalBatchCost;      // unitCostPrice * quantityReceived

    @ManyToOne(optional = false)
    private Purchase purchase;

    @ManyToOne
    private Supplier supplier;

    public Integer getRemainingInBatch() {
        return quantityReceived - quantitySoldFromThisBatch;
    }
}
