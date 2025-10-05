package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.user.UserService;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.lukasz.faktury.views.index.IndexView;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final UserService userService;


    private final PasswordField password;
    private final EmailField email;
    private final TextField nip;

    public RegisterView(UserService userService) {
        this.userService = userService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("📝 Rejestracja użytkownika");


        email = new EmailField("Email");
        email.setRequired(true);
        email.setClearButtonVisible(true);

        password = new PasswordField("Hasło");
        password.setRequired(true);

        nip = new TextField("Nip");
        nip.setRequired(true);
        nip.setClearButtonVisible(true);

        Button registerButton = new Button("Zarejestruj się", event -> register());
        RouterLink index = new RouterLink("Powrót do strony głównej", IndexView.class);

        add(header, email, password, nip, registerButton,index);
    }

    private void register() {
        UserRequest request = new UserRequest(email.getValue(), password.getValue(), nip.getValue());


        try {


            UserResponse response = userService.register(request);
            Notification.show("Potwierdz email w ciagu 24h", 5000, Notification.Position.MIDDLE);
            Notification.show("Zarejestrowano użytkownika: " + response.email(), 3000, Notification.Position.BOTTOM_CENTER);
            getUI().ifPresent(ui -> ui.getPage().executeJs("setTimeout(function() { window.location.href = $0; }, 5000);", "login"

            ));
        } catch (CustomValidationException | NipNotFoundException | NipAlreadyRegisteredException | UserException ex) {

            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }


    }


}


