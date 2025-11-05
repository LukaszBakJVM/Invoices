package com.example.front.views.user;

import com.example.front.exceptions.UserException;
import com.example.front.views.user.dto.EmailRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import org.springframework.web.client.RestClient;

@Route("reset-password")
@AnonymousAllowed

public class ResetPasswordView extends VerticalLayout {
    private final RestClient restClient;

    public ResetPasswordView(RestClient restClient) {
        this.restClient = restClient;



        TextField email = new TextField("Podaj swój email");
        Button resetBtn = new Button("Resetuj hasło");

        resetBtn.addClickListener(e -> resetPassword(email.getValue()));

        H1 resetPassword = new H1("Resetowanie hasła");

        add(resetPassword, email, resetBtn);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();


}


    private void resetPassword(String email) {
        try {
            EmailRequest request = new EmailRequest(email);
            restClient.post().uri("/token/resetPassword").body(request).retrieve().body(Void.class);
          //  resetPasswordService.createToken(email);


            Notification.show("Link do resetowania hasła został wysłany na Twój email",1000, Notification.Position.MIDDLE);

        } catch (UserException ex) {
            Notification.show(ex.getMessage(), 1000, Notification.Position.MIDDLE);
        }


    }

}
