package org.lukasz.faktury.views.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.views.index.IndexView;

import java.util.List;
import java.util.Map;

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
        Map<String, List<String>> parameters = beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        if (parameters.containsKey("error")) {
            login.setError(true);

            Notification.show(parameters.get("error").getFirst(), 5000, Notification.Position.MIDDLE);
        }
    }


}
