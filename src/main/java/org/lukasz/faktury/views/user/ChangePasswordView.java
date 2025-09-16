package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.exceptions.TokenException;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.ResetPasswordService;

import java.util.List;
import java.util.Optional;

@Route("change-password")
@AnonymousAllowed
public class ChangePasswordView extends VerticalLayout implements BeforeEnterObserver {
    Optional<String> token;
    private final ResetPasswordService resetPasswordService;
    private final PasswordField newPassword = new PasswordField("Nowe hasło");
    private final PasswordField confirmPassword = new PasswordField("Potwierdź hasło");

    public ChangePasswordView(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;


        addClassName("change-password-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 reset = new H1("Ustaw nowe hasło");

        Button changeBtn = new Button("Zmień hasło");
        add(reset, newPassword, confirmPassword, changeBtn);

        changeBtn.addClickListener(e -> changePassword());

    }


    private void changePassword() {
        try {
            resetPasswordService.newPassword(token.get(), newPassword.getValue(), confirmPassword.getValue());
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



