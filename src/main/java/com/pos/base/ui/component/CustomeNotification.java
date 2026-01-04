package com.pos.base.ui.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Setter;

@Setter
public class CustomeNotification extends Notification {

    private int notificationDuration = 5000;
    private String message = "";
    private String actionText = "";
    private Span messageSpan;
    private Notification.Position position;
    private ComponentEventListener<ClickEvent<Button>> clickListener;
    private Button actionBtn ;
    private VerticalLayout content = new VerticalLayout();

    public CustomeNotification() {
        this.setDuration(notificationDuration);
        messageSpan = new Span(message);
        actionBtn = new Button(actionText);

        actionBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setSpacing(true); // #
        content.add(messageSpan, actionBtn);

        add(content);
    }

    public void render(){
        this.setDuration(notificationDuration);

        messageSpan.setText(message);
        actionBtn.setText(actionText);

        if (clickListener != null) {
            actionBtn.addClickListener(clickListener);
        }
    }

    public void display(){
        render();
        this.open();
    }

}
