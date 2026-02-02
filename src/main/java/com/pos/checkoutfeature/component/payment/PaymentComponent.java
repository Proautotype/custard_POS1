package com.pos.checkoutfeature.component.payment;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;

public class PaymentComponent {
    private final List<String> predefinedInputValues = List.of("10", "15", "20", "30", "50", "Exact");
    private final PaymentSession session;
    private PaymentOrchestrator orchestrator;

    public PaymentComponent(PaymentSession session) {
        this.session = session;
    }

    public void setOrchestrator(PaymentOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    private void notify(boolean confirmed) {
        orchestrator.handle(new PaymentEvent(session, confirmed));
    }

    public Component build() {
        NumberField input = new NumberField("Pay with ".concat(session.option().name()));
        input.setWidthFull();
        input.addValueChangeListener(e -> {
            session.updateAmount(e.getValue());
            notify(false);
        });

        HorizontalLayout quickButtons = new HorizontalLayout();
        predefinedInputValues.forEach(v -> {
            Button b = new Button(v, e -> {
                Double value = v.equalsIgnoreCase("exact")
                        ? session.payable()
                        : Double.valueOf(v);
                input.setValue(value);
                session.updateAmount(value);
                notify(false);
            });
            quickButtons.add(b);
        });

        Button confirm = new Button("Confirm Payment", e -> {
            if (session.isValid()) {
                notify(true);
            }
        });
        confirm.setEnabled(false);

        input.addValueChangeListener(e -> confirm.setEnabled(session.isValid()));

        return new VerticalLayout(input, quickButtons, confirm);

    }

}
