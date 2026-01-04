package com.pos.core.extension;

import java.math.BigDecimal;

public class CartItemModel {
    private final String name;
    private final BigDecimal price;
    private final boolean isRx;
    // Constructor, getters, and equals/hashCode omitted for brevity.

    public CartItemModel(String name, BigDecimal price, boolean isRx) {
        this.name = name;
        this.price = price;
        this.isRx = isRx;
    }

    // Getters
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public boolean isRx() { return isRx; }
}
