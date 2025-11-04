package com.example.front.views.user;

import com.example.fakturyfront.config.RestClientFactory;
import com.example.fakturyfront.exceptions.TokenException;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.List;
import java.util.Optional;

@Route("confirm")
@AnonymousAllowed
public class ConfirmView extends VerticalLayout implements BeforeEnterObserver {

    private final RestClientFactory restClient;

    public ConfirmView(RestClientFactory restClient) {
        this.restClient = restClient;


        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();


    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        Optional<String> token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of()).stream().findFirst();

        try {
            if (token.isEmpty()) {
                throw new TokenException("Token nie istnieje");
            }
            activationTokenService.findToken(token.get());
            add(new H3("✅ Potwierdzono poprawnie!"));


            event.forwardTo(LoginView.class);

        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }

    }
}
