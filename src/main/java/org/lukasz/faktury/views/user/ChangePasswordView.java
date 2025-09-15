package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
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
    private final ResetPasswordService resetPasswordService;
    private PasswordField newPassword = new PasswordField("Nowe hasło");
    private PasswordField confirmPassword = new PasswordField("Potwierdź hasło");
    private Button changeBtn = new Button("Zmień hasło");

    public ChangePasswordView(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;


        addClassName("change-password-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        H1 reset = new H1("Ustaw nowe hasło");

        add(reset, newPassword, confirmPassword, changeBtn);

        changeBtn.addClickListener(e -> changePassword());
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> token = event.getLocation().getQueryParameters().getParameters().getOrDefault("token", List.of()).stream().findFirst();

        try {
            if (token.isEmpty()) {
                throw new TokenException("Token nie istnieje");
            }
            resetPasswordService.findToken(token.get());
            add(new H3("✅ Potwierdzono poprawnie!"));


            getUI().ifPresent(ui -> ui.navigate("login"));

        } catch (TokenException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);

        }

    }

    //todo

    private void changePassword() {
        if (newPassword.getValue().isEmpty() || confirmPassword.getValue().isEmpty()) {
            Notification.show("Wypełnij wszystkie pola");

        }

        if (!newPassword.getValue().equals(confirmPassword.getValue())) {
            Notification.show("Hasła nie są zgodne");

        }
    }
}
