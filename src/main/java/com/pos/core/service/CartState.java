package com.pos.core.service;

import com.pos.base.ui.component.CustomeNotification;
import com.pos.retailfeature.events.CartChangeEvent;
import com.pos.retailfeature.subcomponent.ReceiptItem;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@VaadinSessionScope
@Component
@Tag(value = "cartstate")
public class CartState extends com.vaadin.flow.component.Component {

    private final CustomeNotification customeNotification = new CustomeNotification();

    private List<ReceiptItem> cart = new ArrayList<>();
    private Customer customer;

    private final ListDataProvider<ReceiptItem> dataProvider;

    public CartState() {
        dataProvider = new ListDataProvider<>(cart);
        start();
    }

    public void start() {
        cart.clear();
        customer = new Customer();
    }

    public void clear(){
        cart.clear();
    }

    public void addToCart(ReceiptItem receiptItem) {
        // validate and udpate existing cart items
        cart.stream()
                .filter(item -> item.getName().equals(receiptItem.getName()))
                .findFirst().ifPresentOrElse(existingItem -> {
                    // update existing item quantity
                    int newQuantity = existingItem.getQuantity() + 1;
                    existingItem.setQuantity(newQuantity);
                    existingItem.setTotalPrice(
                            existingItem.getUnitPrice().multiply(
                                    BigDecimal.valueOf(newQuantity)
                            )
                    );
                    dataProvider.refreshItem(existingItem);
                    fireEvent(new CartChangeEvent(this));
                }, () -> {
                    cart.add(receiptItem);
                    customeNotification.setMessage(String.format("Added %s",receiptItem.getName()));
                    customeNotification.setActionText("Undo");
                    customeNotification.setClickListener(e -> this.removeFromCart(receiptItem.getName()));
                    customeNotification.display();
                    dataProvider.refreshAll();
                    fireEvent(new CartChangeEvent(this));
                });
    }

    public void removeFromCart(String itemName) {
        cart.removeIf(item -> item.getName().equals(itemName));
        fireEvent(new CartChangeEvent(this));
    }

    public void setCustomerName(String customerName) {
        if(customer == null){
            customer = new Customer();
        }
        customer.setName(customerName);
    }

    public void setCustomerContact(String customerContact) {
        if(customer == null){
            customer = new Customer();
        }
        customer.setContact(customerContact);
    }

    public BigDecimal getTotal(){
       return dataProvider.getItems().stream()
                .map(ReceiptItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String formatMoney(BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.CANADA).format(amount);
    }

}
