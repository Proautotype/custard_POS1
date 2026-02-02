package com.pos.inventoryfeature.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseFilter {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minimumAmount;
    private BigDecimal maximumAmount;
}

