package com.example.front.views.user;


import com.example.front.exceptions.TokenException;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Route("confirm")
@AnonymousAllowed
public class ConfirmView extends VerticalLayout implements BeforeEnterObserver {

    private final RestClient restClient;

    public ConfirmView(RestClient restClient) {
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

            restClient.get().uri(uriBuilder -> uriBuilder.path("registration/{token}").build(token.get())).retrieve().body(Void.class);

            // activationTokenService.findToken(token.get());
            add(new H3("✅ Potwierdzono poprawnie!"));


            event.forwardTo(LoginView.class);

        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }

    }
}
