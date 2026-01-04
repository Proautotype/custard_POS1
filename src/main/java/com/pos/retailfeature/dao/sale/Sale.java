package com.pos.retailfeature.dao.sale;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime transactionDate;

    @Column(unique = true)
    private String receiptNumber; // e.g., INV-2025-0001

    private BigDecimal totalAmount;
    private String paymentMethod; // CASH, CARD, INSURANCE

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> items;
}