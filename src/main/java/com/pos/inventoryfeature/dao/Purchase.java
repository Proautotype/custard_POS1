package com.pos.inventoryfeature.dao;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pos.inventoryfeature.dao.stock.StockEntry;

@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    private String id;

    private String supplierInvoiceNumber;
    private LocalDate arrivalDate;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<StockEntry> stockEntries = new ArrayList<>();
    private BigDecimal totalInvoiceAmount;

    // @ManyToOne
    // private Supplier supplier;

    public void addStockEntry(StockEntry entry) {
        stockEntries.add(entry);
        entry.setPurchase(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<StockEntry> getStockEntries() {
        return stockEntries;
    }
}
