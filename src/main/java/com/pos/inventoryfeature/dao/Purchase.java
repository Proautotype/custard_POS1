package com.pos.inventoryfeature.dao;

import com.pos.retailfeature.dao.stock.StockEntry;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String supplierInvoiceNumber;
    private LocalDate arrivalDate;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StockEntry> stockEntries = new ArrayList<>();
    private BigDecimal totalInvoiceAmount;

    @ManyToOne
    private Supplier supplier;

    public void addStockEntry(StockEntry entry) {
        stockEntries.add(entry);
        entry.setPurchase(this);
    }
}
