package pos.controllers;

import pos.listeners.RegisterEvent;
import pos.listeners.RegisterEventEnums;
import pos.listeners.RegisterEventListener;
import pos.listeners.ScannedEventListener;
import pos.models.Basket;
import pos.models.Item;
import pos.models.LineItem;
import pos.services.HttpClientService;
import pos.services.PriceBookService;
import pos.services.TenderedBasketService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class Register implements ScannedEventListener, RegisterEventListener{

    private final List<RegisterEventListener> listeners;

    private final PriceBookService priceBookService;

    private final HttpClientService httpClientService;

    private final TenderedBasketService tenderedBasketService;

    @Getter
    private final BarcodeScanner barcodeScanner;


    private Basket basket;


    @Autowired
    public Register(PriceBookService priceBookService, HttpClientService httpClientService, TenderedBasketService tenderedBasketService, BarcodeScanner barcodeScanner) {
        this.priceBookService = priceBookService;
        this.httpClientService = httpClientService;
        this.tenderedBasketService = tenderedBasketService;
        this.barcodeScanner = barcodeScanner;
        this.listeners = new ArrayList<>();
    }

    @PostConstruct
    private void begin() {
        barcodeScanner.addScannedEventListener(this);
    }

    @Override
    public void onScanned(String scannedData) {
        this.itemAdded(getItemFromScan(scannedData));
    }

    public void addRegisterEventListener(RegisterEventListener listener) {
        listeners.add(listener);
    }


    @Override
    public void updateListeners(RegisterEvent event) {
        for (RegisterEventListener registerEventListener : listeners) {
            registerEventListener.updateListeners(event);
        }
    }

    public Basket startBasket() {
        basket = new Basket();
        basket.setLineItems(new ArrayList<>());
        basket.setRegisterId("404404");
        basket.setCashierId("201201");
        basket.setCreatedTimestamp(String.valueOf(ZonedDateTime.now(ZoneId.systemDefault())));
        updateListeners(
                RegisterEvent.builder()
                .action(RegisterEventEnums.STARTBASKET)
                .basket(this.basket)
                .build()
        );
        return basket;
    }


    public void itemAdded(LineItem lineItem) {
        this.basket = this.getOrCreateBasket();

        this.basket.appendLineItem(lineItem);

        this.updateListeners(
                RegisterEvent.builder()
                        .action(RegisterEventEnums.ADDITEM)
                        .basket(this.basket)
                        .build()
        );
    }

    public void itemVoided() {
        this.basket.voidLineItem();

        this.updateListeners(
                RegisterEvent.builder()
                .action(RegisterEventEnums.VOIDITEM)
                .basket(this.basket)
                .build());
    }


    public void getDiscountForCheckout() {
        httpClientService.sendRequestToDiscountService(this);
    }

    public void checkout(RegisterEventEnums eventEnums) {
        this.basket.setAction(eventEnums.toString());
        tenderedBasketService.saveTenderedBasket(this.basket);
        this.updateListeners(
                RegisterEvent.builder()
                .action(eventEnums)
                .basket(this.basket)
                .build()
        );
    }

    public void basketChanged(Basket basket, RegisterEventEnums eventEnums) {
        this.basket = basket;
        RegisterEvent event;
        if(eventEnums == RegisterEventEnums.DISCOUNTFAILED) {
            event = RegisterEvent.builder()
                    .action(eventEnums)
                    .basket(this.basket)
                    .build();
        }
        else {
            basket.applyDiscount();
            event = RegisterEvent.builder()
                    .action(eventEnums)
                    .basket(this.basket)
                    .build();
        }
        this.updateListeners(event);
    }

    public void voidBasket() {
        this.updateListeners(
                RegisterEvent.builder()
                        .action(RegisterEventEnums.VOIDBASKET)
                        .basket(this.basket)
                        .build()
        );
        this.endBasket();
    }

    public void endBasket() {
        this.updateListeners(RegisterEvent.builder()
                .action(RegisterEventEnums.ENDBASKET)
                        .build()
        );
        this.basket = null;
    }

    public Basket getOrCreateBasket() {
        if(this.basket == null) {
            return this.startBasket();
        }else {
            return this.basket;
        }
    }

    public List<Item> sendPriceBook() {
        return priceBookService.getPriceBook();
    }

    public LineItem getItemFromScan(String scannedData) {
        Item item = priceBookService.getItem(Long.parseLong(scannedData));
        return LineItem.builder()
                .item(item)
                .quantity(1)
                .price(item.getPrice())
                .voided(false)
                .build();
    }

    public List<Basket> getTenderedBaskets(){
        return tenderedBasketService.getTenderedBaskets();
    }

}
