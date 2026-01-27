package com.pos.retailfeature.dao.sale;

import com.pos.inventoryfeature.dao.stock.StockEntry;
import com.pos.retailfeature.dao.product.Product;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Sale sale;

    @ManyToOne
    private Product product;

    @ManyToOne
    private StockEntry stockEntry; // Tracks which specific batch was sold

    private Integer quantity;

    // Captured at time of sale (Audit Trail)
    private BigDecimal priceAtSale;
    private BigDecimal taxAmount;

    public BigDecimal getSubtotal() {
        return priceAtSale.multiply(BigDecimal.valueOf(quantity));
    }
}
