package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.ResetPasswordService;

import java.util.List;
import java.util.Optional;

@Route("resetToken")
@AnonymousAllowed
public class TokenConfirmation extends VerticalLayout implements BeforeEnterObserver {
    private final ResetPasswordService resetPasswordService;

    public TokenConfirmation(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        Optional<String> token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of()).stream().findFirst();

        try {


            resetPasswordService.findToken(token.get());
            add(new H3("✅ Potwierdzono poprawnie!"));
            event.forwardTo(ChangePasswordView.class);


        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }

    }
}

