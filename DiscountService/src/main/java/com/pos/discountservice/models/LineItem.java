package com.pos.discountservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class LineItem {

    private Item item;
    private BigDecimal price;
    private int quantity;
    private boolean voided;

}
