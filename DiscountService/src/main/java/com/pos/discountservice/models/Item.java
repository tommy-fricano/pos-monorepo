package com.pos.discountservice.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Item {

    private long code;
    private String name;
    private BigDecimal price;

    public Item(long code, String name, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }
}
