package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.views.index.IndexView;

@Route("login")
@PageTitle("Logowanie")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        H1 header = new H1("📝 Strona Logowania");
        Button homeButton = new Button("← Wróć do strony głównej", e -> UI.getCurrent().navigate(IndexView.class));

        addClassName("login-view");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        // nie wysyłaj POST do /login, robimy ręczne logowanie
        login.setAction("login");

        login.addLoginListener(e -> {
            String email = e.getUsername();
            String password = e.getPassword();


            VaadinSession.getCurrent().setAttribute("email", email);
            VaadinSession.getCurrent().setAttribute("pass", password);

            UI.getCurrent().navigate(IndexView.class);

            login.setError(true);
            Notification.show("Niepoprawny login lub hasło", 3000, Notification.Position.MIDDLE);

        });

        add(header, login, homeButton);
    }

}
