package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.utils.confirmationtoken.ConfirmationTokenService;

import java.util.List;
import java.util.Optional;

@Route("confirm")
public class ConfirmView extends VerticalLayout implements BeforeEnterObserver {
    private final ConfirmationTokenService confirmationTokenService;

    public ConfirmView(ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;

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
            confirmationTokenService.findToken(token.get());
            add(new H3("✅ Potwierdzono poprawnie!"));


            getUI().ifPresent(ui -> ui.navigate("login"));

        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }

    }
}
