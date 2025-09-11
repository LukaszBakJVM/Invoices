package org.lukasz.faktury.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")

public class IndexView extends VerticalLayout {

    public IndexView() {


        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        addClassName("dashboard-bg");


        H1 header = new H1("📑 System do zarządzania fakturami");
        header.addClassName("dashboard-header");

        H2 subtitle = new H2("Witaj! Wybierz co chcesz zrobić:");
        subtitle.addClassName("dashboard-subtitle");

        RouterLink login = new RouterLink("Zaloguj się", LoginView.class);
        RouterLink registration = new RouterLink("Zarejstruj się", RegisterView.class);
        add(header, login, registration);


    }


}


