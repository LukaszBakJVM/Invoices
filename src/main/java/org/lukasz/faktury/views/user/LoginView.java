package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.user.login.CustomUserDetailsService;
import org.lukasz.faktury.views.index.IndexView;

@Route("login")

@PageTitle("Logowanie | FakturyApp")
@AnonymousAllowed

public class LoginView extends VerticalLayout {

    private final CustomUserDetailsService service;
    private final PasswordField password;
    private final EmailField email;

    public LoginView(CustomUserDetailsService service) {
        this.service = service;


        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("📝 Strona  Logowania");


        email = new EmailField("Email");
        email.setRequired(true);
        email.setClearButtonVisible(true);

        password = new PasswordField("Hasło");
        password.setRequired(true);
        Button loginButton = new Button("Zaloguj się", event -> login());

        RouterLink index = new RouterLink("Strona  Główna", IndexView.class);

        add(header, email, password, loginButton,index);


    }

    private void login() {
        try {

            service.loadUserByUsername(email.getValue());

        } catch (UserException ex) {
            Notification.show(ex.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}
