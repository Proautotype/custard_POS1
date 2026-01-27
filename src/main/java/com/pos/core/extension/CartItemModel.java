package com.pos.core.extension;

import java.math.BigDecimal;

public record CartItemModel(String name, BigDecimal price, boolean isRx) {
    // Constructor, getters, and equals/hashCode omitted for brevity.

}
