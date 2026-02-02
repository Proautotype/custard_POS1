package com.pos.retailfeature.dao.sale;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleFilter {
    private String id;
    private String mobileMoneyNumber;
    private String performedBy;
    private LocalDate fromDate;
    private LocalDate toDate;
}
