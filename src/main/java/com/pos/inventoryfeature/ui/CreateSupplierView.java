package com.pos.inventoryfeature.ui;

import com.pos.inventoryfeature.service.SupplierService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@UIScope
@RequiredArgsConstructor
@Component
@Slf4j
public class CreateSupplierView extends Dialog {

    TextField nameTxt = new TextField("Name");
    TextField phoneTxT = new TextField("Phone", "0200000000");
    TextField emailTxT = new TextField("Email", "user@example.com");
    Button createBtn = new Button("Create Supplier");
    Button exitBtn = new Button("Exit");
    HorizontalLayout actions = new HorizontalLayout();

    private final SupplierService supplierService;

    @PostConstruct
    void init(){
        setHeaderTitle("Create new supplier");
        setCloseOnOutsideClick(false);
        nameTxt.setPattern("(###)-###-####");
        phoneTxT.setPattern("#@#.#");

        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        exitBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        actions.add(createBtn, exitBtn);
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        VerticalLayout verticalLayout = new VerticalLayout(nameTxt, phoneTxT, emailTxT, actions);
        verticalLayout.setWidth("250px");
        add(verticalLayout);

        setActions();
    }

    void setActions(){
        createBtn.addClickListener(event -> {
            supplierService.create(
                    nameTxt.getValue(),
                    phoneTxT.getValue(),
                    emailTxT.getValue()
            );

            Notification.show(
                    String.format("Supplier (%s) is created", nameTxt.getValue())
            ).addOpenedChangeListener(openedChangeEvent -> {
                if(!openedChangeEvent.isOpened()){
                    this.close();
                }
            });
        });
        exitBtn.addClickListener(event -> {
            nameTxt.clear();
            phoneTxT.clear();
            emailTxT.clear();
            this.close();
        });
    }

    void createSupplier(){
//        eventPublisher.publishEvent(new SupplierCreatedEvent());
    }

    public void setName(String name){
//        log.info("Set Name ---> {} ", name);
        nameTxt.setValue(name);
    }

}
