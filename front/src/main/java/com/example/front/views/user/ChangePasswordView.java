package com.example.front.views.user;

import com.example.front.exceptions.TokenException;
import com.example.front.views.user.dto.ConfirmPassword;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.client.RestClient;


import java.util.List;
import java.util.Optional;

@Route("change-password")
@AnonymousAllowed
public class ChangePasswordView extends VerticalLayout implements BeforeEnterObserver {
    Optional<String> token;
    private final RestClient restClient;
    private final TextField emailField = new TextField("email");
    private final PasswordField newPassword = new PasswordField("Nowe hasło");
    private final PasswordField confirmPassword = new PasswordField("Potwierdź hasło");

    public ChangePasswordView(RestClient restClient) {
        this.restClient = restClient;


    addClassName("change-password-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 reset = new H1("Ustaw nowe hasło");

        Button changeBtn = new Button("Zmień hasło");
        add(reset,emailField, newPassword, confirmPassword, changeBtn);

        changeBtn.addClickListener(e -> changePassword());

    }


    private void changePassword() {
        try {
            ConfirmPassword cP = new ConfirmPassword(token.get(), newPassword.getValue(), confirmPassword.getValue(),emailField.getValue());
            restClient.post().uri("/newPassword").body(cP).retrieve().body(Void.class);
         //   resetPasswordService.newPassword(token.get(), newPassword.getValue(), confirmPassword.getValue());
            Notification.show("Hasło zostało zmienione", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.getPage().executeJs("setTimeout(function() { window.location.href = $0; }, 3000);", "login"));

        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of()).stream().findFirst();


        }
    }



