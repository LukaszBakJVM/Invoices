package org.lukasz.faktury.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.lukasz.faktury.user.UserServiceImpl;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;

@Route("register")
public class RegisterView extends VerticalLayout {
    private final UserServiceImpl service;

    private TextField username;
    private PasswordField password;
    private EmailField email;
    private TextField nip;

    public RegisterView(UserServiceImpl service) {
        this.service = service;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("📝 Rejestracja użytkownika");

        username = new TextField("Nazwa użytkownika");
        username.setRequired(true);
        username.setClearButtonVisible(true);

        email = new EmailField("Email");
        email.setRequired(true);
        email.setClearButtonVisible(true);

        password = new PasswordField("Hasło");
        password.setRequired(true);

        nip = new TextField("Nip");
        nip.setRequired(true);
        nip.setClearButtonVisible(true);

        Button registerButton = new Button("Zarejestruj się", event -> register());

        add(header, username, email, password, nip, registerButton);
    }

    private void register() {
        UserRequest request = new UserRequest(username.getValue(), password.getValue(), email.getValue(), nip.getValue());


        // if (user.isEmpty() || mail.isEmpty() || pass.isEmpty()||Nip.isEmpty()) {
        //   Notification.show("Wszystkie pola są wymagane!", 3000, Notification.Position.MIDDLE);

        //  }
        try {


            UserResponse response = service.register(request);
            Notification.show("Potwierdz email w ciagu 24h", 5000, Notification.Position.MIDDLE);
            Notification.show("Zarejestrowano użytkownika: " + response.username() + " | " + response.email(), 3000, Notification.Position.BOTTOM_CENTER);
        } catch (CustomValidationException ex) {

            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }


        // np. po rejestracji przekierowanie do logowania
        //  getUI().ifPresent(ui -> ui.navigate("login"));
    }


}


