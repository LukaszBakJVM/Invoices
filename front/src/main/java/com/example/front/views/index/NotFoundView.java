package com.example.front.views.index;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.http.HttpServletResponse;
@AnonymousAllowed

public class NotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {


    public NotFoundView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }


    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<NotFoundException> errorParameter) {

        Icon warningIcon = VaadinIcon.WARNING.create();
        warningIcon.setSize("80px");
        warningIcon.getStyle().set("color", "red");


        H1 title = new H1("404 - Strona nie istnieje");
        Paragraph message = new Paragraph("Ups! Wygląda na to, że podany adres jest nieprawidłowy.");


        Button homeButton = new Button("← Wróć do strony głównej", e -> UI.getCurrent().navigate(""));

        add(warningIcon, title, message, homeButton);

        return HttpServletResponse.SC_NOT_FOUND;
    }
}






