package com.pos.loginfeature.ui;

import com.pos.base.ui.AuthLayout;
import com.pos.base.ui.component.RoundedDiv;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "/login", layout = AuthLayout.class)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends HorizontalLayout implements BeforeEnterObserver {
    private final LoginForm loginForm = new LoginForm();
    LoginView() {
        setWidthFull();
        Div loginWrapper = new Div();
        loginWrapper.addClassNames(LumoUtility.Width.FULL, LumoUtility.Height.FULL);
        loginWrapper.addClassNames(LumoUtility.Width.FULL, LumoUtility.Height.FULL);
        loginWrapper.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("CUSTARD ELECTRONICA");
        i18nForm.setUsername("Staff ID/Email");
        i18nForm.setPassword("Password");
        i18nForm.setSubmit("Sign in");

        loginForm.setI18n(i18n);
        loginForm.setAction("login");
        loginForm.setForgotPasswordButtonVisible(false);


        Div heroWrapper = new Div();
        heroWrapper.addClassNames(LumoUtility.Width.FULL, LumoUtility.Height.FULL);
        heroWrapper.addClassNames(LumoUtility.Width.FULL, LumoUtility.Height.FULL);
        heroWrapper.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.END, LumoUtility.JustifyContent.START, LumoUtility.Padding.Bottom.XLARGE);
        heroWrapper.getStyle().setBackgroundColor("#07033f");


        loginWrapper.add(loginForm);
        heroWrapper.add(heroSegment());


        add(loginWrapper);
        add(heroWrapper);
        addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.CENTER,
                LumoUtility.Height.FULL
        );
    }

    private FormLayout loginSegment() {
        FormLayout form = new FormLayout();
        form.getElement().setAttribute("action","login");
        form.getElement().setAttribute("method","post");

        H2 title = new H2("Pharmacy Management");
        title.addClassNames(LumoUtility.TextColor.HEADER);
        Input col = new Input();
        col.setType("color");

        H6 description = new H6("Please sign in to access your dashboard");
        description.addClassNames(
                LumoUtility.TextColor.DISABLED,
                LumoUtility.FontSize.XXSMALL,
                LumoUtility.Padding.Top.SMALL,
                LumoUtility.Padding.Bottom.MEDIUM
        );
        description.getStyle().setFontSize("10px");

        TextField usernameField = new TextField("Staff ID/Username", "Enter Staff ID");
        usernameField.addClassNames(LumoUtility.Width.FULL);
        usernameField.getElement().setAttribute("name", "username");
        PasswordField passwordField = new PasswordField("Password", "Enter Password");
        passwordField.addClassNames(LumoUtility.Width.FULL);
        passwordField.getElement().setAttribute("name","password");

        Div forgotPasswordArea = new Div();
        forgotPasswordArea.addClassNames(LumoUtility.TextColor.SUCCESS, LumoUtility.FontSize.XXSMALL);
        H6 forgotPwdLink = new H6("Forgot password?");
        forgotPwdLink.addClassNames(LumoUtility.TextColor.PRIMARY);
        forgotPwdLink.getStyle().setFontSize("8px");
        forgotPasswordArea.add(forgotPwdLink);
        forgotPasswordArea.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.END, LumoUtility.Width.FULL, LumoUtility.Padding.Top.SMALL, LumoUtility.Padding.Bottom.SMALL);

        Button signin = new Button("Sign in");
        signin.getElement().callJsFunction("setAttribute","type","submit");
        signin.addClassNames(
                LumoUtility.Background.PRIMARY,
                LumoUtility.Width.FULL,
                LumoUtility.Margin.Top.XLARGE,
                LumoUtility.TextColor.SECONDARY
        );
        signin.getStyle().setColor("#fff");

        form.add( title, description, usernameField, passwordField, forgotPasswordArea, signin);
        form.addClassNames(
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.START,
                LumoUtility.JustifyContent.CENTER
        );
        form.getStyle().setWidth("42%");
        return form;
    }

    private Div heroSegment() {
        Div content = new Div();

        RoundedDiv roundedDiv = new RoundedDiv("Secure environment");
        H1 title = new H1();
        title.setText("Reliable. Secure. Efficient.");
        title.getStyle().setColor("#ffffff");
        H6 description = new H6();
        description.setText("Empower pharmacists with next-generation tools for better patient care");
        description.addClassNames(LumoUtility.Padding.Top.MEDIUM);
        description.getStyle().setColor("#ffffff").setFontSize("10px");

        content.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.TextColor.PRIMARY_CONTRAST, LumoUtility.TextColor.SUCCESS_CONTRAST);
        content.getStyle()
                .setWidth("50%")
                .setHeight("auto")
                .setColor("#fff")
                .setPaddingBottom("10%")
                .setPaddingLeft("10%")
        ;
        content.add(title, description);
        return content;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
