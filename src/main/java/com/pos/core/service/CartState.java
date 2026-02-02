package com.pos.core.service;

import com.pos.base.ui.component.CustomeNotification;
import com.pos.retailfeature.events.CartChangeEvent;
import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.pos.shared.StaticUtils;
import com.pos.shared.Utils;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@VaadinSessionScope
@Component
@Slf4j
@Tag(value = "cartstate")
public class CartState extends com.vaadin.flow.component.Component {

    private String currentSaleid = "";
    private final CustomeNotification customeNotification = new CustomeNotification();

    private final List<ReceiptItem> cart = new ArrayList<>();
    private Customer customer;
    private double discount = 0;

    private final ListDataProvider<ReceiptItem> dataProvider;

    public CartState() {
        dataProvider = new ListDataProvider<>(cart);
        start();
    }

    public void start() {
        clear();

    }

    public void clear() {
        cart.clear();
        dataProvider.refreshAll();
        fireEvent(new CartChangeEvent(this));
        customer = new Customer();
        discount = 0;
        currentSaleid = "SALE-".concat(StaticUtils.generateIdentifier(12, "-"));
    }

    public void addToCart(ReceiptItem receiptItem) {
        // validate and udpate existing cart items
        cart.stream()
                .filter(item -> item.getName().equals(receiptItem.getName()))
                .findFirst().ifPresentOrElse(existingItem -> {
                    // update existing item quantity
                    int newQuantity = existingItem.getQuantity() + 1;
                    existingItem.setQuantity(newQuantity);

                    BigDecimal newTotalPrice = existingItem.getUnitPrice().multiply(
                                    BigDecimal.valueOf(newQuantity)
                            );
                    log.info("new total price -> {} ", newTotalPrice);


                    existingItem.setTotalPrice(newTotalPrice);
                    dataProvider.refreshItem(existingItem);
                    fireEvent(new CartChangeEvent(this));
                }, () -> {
                    cart.add(receiptItem);
                    dataProvider.refreshAll();
                    fireEvent(new CartChangeEvent(this));
                });
    }

    public void removeFromCart(String itemName) {
        cart.removeIf(item -> item.getName().equals(itemName));
        dataProvider.refreshAll();
        fireEvent(new CartChangeEvent(this));
    }

    public void setCustomerName(String customerName) {
        if (customer == null) {
            customer = new Customer();
        }
        customer.setName(customerName);
    }

    public void setCustomerContact(String customerContact) {
        if (customer == null) {
            customer = new Customer();
        }
        customer.setContact(customerContact);
    }

    public BigDecimal getTotal() {
        return cart.stream()
                .map(ReceiptItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String formatMoney(BigDecimal amount) {

        return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount);
    }

    public ListDataProvider<ReceiptItem> getDataProvider() {
        return dataProvider;
    }

    public double getDiscount() {
        return discount != 0 ? discount : 0;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

}
