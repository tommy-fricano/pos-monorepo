package com.pos.discountservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pos.models.Basket;
import pos.models.LineItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
@RequestMapping("/discount")
public class DiscountController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public String getDiscount(@RequestBody Basket basket) throws JsonProcessingException {
        Basket newBasket = basket;
        Random random = new Random();
        BigDecimal discount = BigDecimal.ZERO;
        for(LineItem lineItem: basket.getLineItems()){
            if(lineItem.isVoided()){
                continue;
            }
            discount = discount.add(lineItem.getPrice().multiply(BigDecimal.valueOf(random.nextDouble(.2,.8))));
        }
        newBasket.setDiscount(discount.setScale(2, RoundingMode.HALF_UP));
        return objectMapper.writeValueAsString(newBasket);
    }
}

