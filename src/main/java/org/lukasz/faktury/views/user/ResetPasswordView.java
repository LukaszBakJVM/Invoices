package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.ResetPasswordService;

@Route("reset-password")
@AnonymousAllowed

public class ResetPasswordView extends VerticalLayout {
    private final ResetPasswordService resetPasswordService;

    public ResetPasswordView(ResetPasswordService resetPasswordService) {
        this.resetPasswordService = resetPasswordService;


        TextField email = new TextField("Podaj swój email");
        Button resetBtn = new Button("Resetuj hasło");

        resetBtn.addClickListener(e -> resetPassword(email.getValue()));

        H1 resetPassword = new H1("Resetowanie hasła");

        add(resetPassword, email, resetBtn);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }

    //todo
    private void resetPassword(String email) {
        try {
            resetPasswordService.createToken(email);
            Notification.show("Link do resetowania hasła został wysłany na Twój email",1000, Notification.Position.MIDDLE);

        } catch (UserException ex) {
            Notification.show(ex.getMessage(), 1000, Notification.Position.MIDDLE);
        }


    }

}
