package com.pos.core.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Sale {
    private final UUID id = UUID.randomUUID();
    private final List<SaleItem> items = new ArrayList<>();
    private final BigDecimal discount = BigDecimal.ZERO;
    private final BigDecimal taxRate = new BigDecimal("0.05"); // 5% generic tax

    public List<SaleItem> getItems() {
        return items;
    }

    public void addItem(SaleItem item) {
        this.items.add(item);
    }

    public BigDecimal calculateSubtotal(){
        return items.stream()
                .map(SaleItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotal() {
        BigDecimal subtotal = calculateSubtotal();
        BigDecimal taxAmount = subtotal.multiply(taxRate);
        return subtotal.add(taxAmount).subtract(discount);
    }
}
