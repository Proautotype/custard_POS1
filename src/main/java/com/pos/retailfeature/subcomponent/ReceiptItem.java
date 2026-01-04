package com.pos.retailfeature.subcomponent;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ReceiptItem {
    private String id;
    private String name = "";
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private int quantity = 0;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public ReceiptItem(){
        id = UUID.randomUUID().toString();
    }

}
