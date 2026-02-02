package com.pos.inventoryfeature.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.pos.inventoryfeature.dao.stock.StockEntry;

@Entity
@Table(name = "purchase")
@Getter
@Setter
public class Purchase {
    @Id
    private String id;

    private String supplierInvoiceNumber;

    @OneToMany(
            mappedBy = "purchase",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<StockEntry> stockEntries = new ArrayList<>();
    private BigDecimal totalInvoiceAmount;

    @CreatedDate
    private LocalDate arrivalDate;

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
