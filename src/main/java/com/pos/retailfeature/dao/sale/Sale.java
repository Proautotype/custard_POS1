package com.pos.retailfeature.dao.sale;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.pos.checkoutfeature.component.simplepayment.PaymentMode;

@Entity
@Table(name = "sales")
@Getter
@Setter
public class Sale {
    @Id
    private String id;

    @Column(unique = true)
    private String receiptNumber; // e.g., INV-2025-0001

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMethod = PaymentMode.CASH; // CASH, CARD, INSURANCE

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SaleItem> items;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    private BigDecimal amountTendered;
    private BigDecimal changeGiven;

    @Column(length = 500)
    private String paymentNote;

    private String mobileMoneyNumber;

    @Column(columnDefinition = "text")
    private String performedBy;

}