package com.pos.core.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class SaleItem {
    private final UUID id = UUID.randomUUID();
    private final String barcode;
    private final String name;
    private int quantity;
    private BigDecimal unitPrice;

    public SaleItem(String barcode, String name, BigDecimal unitPrice, int quantity) {
        this.barcode = barcode;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice(){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isPrescription() {
        return name.toLowerCase().contains("rx");
    }
}
