package org.lukasz.faktury.views.invoice;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.lukasz.faktury.Buyer.BuyerService;

@Route("newinvoice")
@PermitAll

public class NewInvoiceView extends VerticalLayout {
    private final BuyerService buyerService;
    public NewInvoiceView(BuyerService buyerService) {
        this.buyerService = buyerService;


        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 header = new H1("📝 Strona  Logowania");
        add(header);
    }
}
