package com.pos.inventoryfeature.dao.stock;

import java.math.BigDecimal;

public record ProductStockSummary(
        String productId,
        String productName,
        Long totalQuantity,
        BigDecimal sellingPrice
) {}
