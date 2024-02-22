package com.pos.discountservice.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Basket {

    private static final BigDecimal TAX = BigDecimal.valueOf(.07);

    private List<LineItem> lineItems;

    private boolean voided = false;

    private BigDecimal subtotal = BigDecimal.valueOf(0);

    private BigDecimal total= BigDecimal.valueOf(0);

    private String registerId;

    private String cashierId;

    private String createdTimestamp;

    private BigDecimal discount;

}
