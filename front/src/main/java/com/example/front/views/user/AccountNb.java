package com.example.front.views.user;

import com.example.front.config.RestClientFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@Route("addAcount")
@PermitAll
public class AccountNb extends VerticalLayout {
    private final TextField accountNb;
    private final RestClientFactory restClient;

    public AccountNb(RestClientFactory restClient) {
        this.restClient = restClient;

        accountNb=new TextField();
        accountNb.setPlaceholder("Nr konta");

        Button nb = new Button("Dodaj konto bankowe",e->account());

        Button homeButton = new Button("← Powrót na poprzednia stronę", e -> UI.getCurrent().navigate(DashboardView.class));
        add(accountNb,nb,homeButton);
    }

    private void account() {

        //todo
       // sellerService.addAccountNb(accountNb.getValue());
        getUI().ifPresent(ui -> ui.navigate(DashboardView.class));

    }
}
