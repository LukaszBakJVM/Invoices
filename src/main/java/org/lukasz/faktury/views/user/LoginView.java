package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.views.index.IndexView;

@Route("login")

@PageTitle("Logowanie")
@AnonymousAllowed

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    Button homeButton = new Button("← Wróć do strony głównej", e -> UI.getCurrent().navigate(IndexView.class));

    public LoginView() {
        H1 header = new H1("📝 Strona  Logowania");


        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        login.setForgotPasswordButtonVisible(true);
        login.addForgotPasswordListener(e -> getUI().ifPresent(ui -> ui.navigate(ResetPasswordView.class)));


        add(header, login,homeButton);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);


        }
    }
}
