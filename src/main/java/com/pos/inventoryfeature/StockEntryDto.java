package com.pos.inventoryfeature;

import com.pos.retailfeature.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class StockEntryDto {
    private Long id;
    private ProductDto product;

    private Integer quantityReceived;
    private Integer quantitySoldFromThisBatch = 0;

    private LocalDate arrivalDate;
    private LocalDate expiryDate;

    private String batchNumber; // e.g., "LOT-2024-X"

    private BigDecimal unitCostPrice;       // What you paid the supplier for THIS batch
    private BigDecimal totalBatchCost;      // unitCostPrice * quantityReceived
}
