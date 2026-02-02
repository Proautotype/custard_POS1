package com.pos.inventoryfeature.dao.stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pos.checkoutfeature.component.simplepayment.PaymentMode;
import com.pos.retailfeature.dao.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock_movement")
@Getter
@Setter
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne
    private StockEntry stockEntry; // which batch

    @Enumerated(EnumType.STRING)
    private StockMovementType type;

    @Enumerated(EnumType.STRING)
    private PaymentMode mode;

    private Integer quantity;

    private String performedBy; // username / userId

    private LocalDateTime performedAt; 

    private String reference; // receipt no / invoice id

    private String adjustmentReason;

    private BigDecimal unitPrice; // price at which stock was sold or adjusted

}
