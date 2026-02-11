package com.pos.checkoutfeature.component.simplepayment;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class SimplePaymentComponent extends VerticalLayout {

    private final ArrayList<String> predefinedInputValues = new ArrayList<>();

    private SimplePaymentListener listener;

    public void setListener(SimplePaymentListener listener) {
        this.listener = listener;
    }

    public SimplePaymentComponent(PaymentMode mode, Double payable) {
        predefinedInputValues.addAll(List.of("10", "15", "20", "30", "50", "Exact"));

        NumberField amountField = new NumberField("Enter amount");
        amountField.setPlaceholder("0.0");
        amountField.setStepButtonsVisible(true);

        NumberField changNumberField = new NumberField("Change");
        changNumberField.setReadOnly(false);

        TextField cardField = new TextField("Enter ".concat(mode.name()).concat(" number."));

        HorizontalLayout accountsLayout = new HorizontalLayout();
        accountsLayout.add(amountField, changNumberField);

        HorizontalLayout predefinedValuesDisplay = new HorizontalLayout();

        predefinedValuesDisplay.setPadding(false);
        predefinedInputValues.forEach(inputValueStr -> {
            Button valueBtn = new Button("".concat(inputValueStr));
            valueBtn.addClickListener(buttonClickEvent -> {
                if (!inputValueStr.equalsIgnoreCase("exact")) {
                    amountField.setValue(Double.valueOf(inputValueStr));
                } else {
                    amountField.setValue(payable);
                }
            });
            predefinedValuesDisplay.add(valueBtn);
        });

        TextArea paymentNoteTxt = new TextArea("TRANSACTION NOTES (OPTIONAL)",
                "Add note about prescription, patient request or any special details to this transaction");
        paymentNoteTxt.setWidthFull();
        paymentNoteTxt.setHeight("120px");

        Button confirm = new Button("Confirm");

        amountField.addValueChangeListener(e -> {
            if (listener != null) {
                listener.onAmountChanged(e.getValue());
                changNumberField.setValue(e.getValue() - payable);
            }
        });

        changNumberField.addValueChangeListener(e -> {
            if(e.getValue() < 0) {
                changNumberField.setInvalid(true);
                changNumberField.setErrorMessage("Amount is less than payable");
            } else {
                changNumberField.setInvalid(false);
            }
        });

        cardField.addValueChangeListener(e -> {
            if (listener != null) {
                listener.onMobileNumberChanged(e.getValue());
            }
        });
        confirm.addClickListener(e -> {



            if(amountField.getValue() == null || amountField.getValue() <= 0 || changNumberField.isInvalid() || amountField.getValue() < payable) {
                // Notification.show("Please enter valida amount").open();
                Dialog errorDialog = new Dialog();
                errorDialog.setHeaderTitle("Invalid amount");
                errorDialog.add("Please enter a valid amount that is greater than or equal to the payable amount.");
                Button closeBtn = new Button("Close", event -> errorDialog.close());
                closeBtn.getThemeNames().add("error");
                errorDialog.add(closeBtn);
                errorDialog.open();
                flash(amountField);
                return;
            }


            if (listener != null) {
                Dialog  confirmDialog = new Dialog();
                confirmDialog.setHeaderTitle("Confirm purchase");
                confirmDialog.add("Are you sure you want to proceed?");
                Button yesBtn = new Button("Yes", event -> {
                    listener.onConfirm(amountField.getValue(), mode);
                    confirmDialog.close();
                });
                Button noBtn = new Button("No", event -> confirmDialog.close());
                noBtn.getThemeNames().add("error");

                VerticalLayout dialogLayout = new VerticalLayout();
                dialogLayout.setSizeFull();
                dialogLayout.add(new HorizontalLayout(yesBtn,noBtn ));

                confirmDialog.add(dialogLayout);
                confirmDialog.open();
            }
        });
        
        paymentNoteTxt.addValueChangeListener(e -> {
            if (listener != null) {
                listener.onDescriptionChanged(e.getValue());
            }
        });


        add(accountsLayout);

        if (mode.equals(PaymentMode.MOBILE)) {
            add(cardField);
        }

        add(predefinedValuesDisplay, paymentNoteTxt, confirm);

    }

    public void flash(Component component) {
    component.addClassName("flash");

    // Remove class after animation so it can be reused
    UI.getCurrent().access(() ->
        component.getElement()
                 .executeJs(
                     "setTimeout(() => $0.classList.remove('flash'), 2500)",
                     component.getElement()
                 )
    );
}


}
