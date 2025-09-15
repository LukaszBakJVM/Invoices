package org.lukasz.faktury.views.invoice;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("newinvoice")
@PermitAll
public class NewInvoiceView extends VerticalLayout {
    public NewInvoiceView() {

    }
}
