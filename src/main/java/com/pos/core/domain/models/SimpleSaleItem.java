package com.pos.core.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class SimpleSaleItem {
    private String id;
    private String name;
    private BigDecimal price;

    public SimpleSaleItem(String name, BigDecimal price){
        id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
    }
}
