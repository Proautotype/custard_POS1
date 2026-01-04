package com.pos.retailfeature.dao.product;

import com.pos.retailfeature.dao.stock.StockEntry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description = "";

    @ManyToOne
    @JoinColumn(name = "generic_id")
    private GenericProduct genericProduct;

    // pharmaceutical
    @Column(name = "dosage_form")
    private String dosageForm;
    private String strength;

    @Column(nullable = false)
    private BigDecimal currentSellingPrice = BigDecimal.ZERO; // Used by the cashier at the checkout.

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<StockEntry> stockEntries;

    // Helper method to get total stock across all dates
    // Add this inside Product class for high-performance sorting/filtering
    @org.hibernate.annotations.Formula("(SELECT COALESCE(SUM(s.quantity_received - s.quantity_sold_from_this_batch), 0) FROM stock_entries s WHERE s.product_id = id)")
    private Integer totalStockLeft;
}
