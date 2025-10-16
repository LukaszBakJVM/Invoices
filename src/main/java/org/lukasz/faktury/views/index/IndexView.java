package org.lukasz.faktury.views.index;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.lukasz.faktury.views.user.LoginView;
import org.lukasz.faktury.views.user.RegisterView;

@Route("")
@PageTitle("Faktury XL")
@AnonymousAllowed


public class IndexView extends VerticalLayout {

    public IndexView() {


        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        addClassName("dashboard-bg");


        H1 header = new H1("📑 System do zarządzania fakturami");
        header.addClassName("dashboard-header");


        Button login = new Button("Zaloguj się", e -> UI.getCurrent().navigate(LoginView.class));

        Button registration = new Button("Zarejstruj się", e -> UI.getCurrent().navigate(RegisterView.class));
        add(header, login, registration);


    }


}


