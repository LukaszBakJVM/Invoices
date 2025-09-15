package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.UI;
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
import org.lukasz.faktury.views.index.IndexView;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")

@PageTitle("Logowanie")
@AnonymousAllowed

public class LoginView extends VerticalLayout {

    private final AuthenticationManager authenticationManager;

    private final PasswordField password;
    private final EmailField email;

    public LoginView(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;


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

        add(header, email, password, loginButton, index);


    }

    private void login() {
        try {
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email.getValue(), password.getValue());

            Authentication authentication = authenticationManager.authenticate(authRequest);


            SecurityContextHolder.getContext().setAuthentication(authentication);

            Notification.show("Zalogowano!", 3000, Notification.Position.MIDDLE);


            UI.getCurrent().navigate("dashbord");

        } catch (AuthenticationException ex) {
            Notification.show("Niepoprawny login lub hasło", 5000, Notification.Position.MIDDLE);
        }
    }
}
