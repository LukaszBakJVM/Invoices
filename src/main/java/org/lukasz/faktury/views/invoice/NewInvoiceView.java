package org.lukasz.faktury.views.invoice;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.lukasz.faktury.Buyer.BuyerService;
import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.invoices.InvoicesService;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.springframework.web.client.RestClientResponseException;

@Route("newinvoice")
@PermitAll

public class NewInvoiceView extends VerticalLayout {

    private final TextField nipField;
    private final BuyerService buyerService;

    private final VerticalLayout buyerDataLayout;

    public NewInvoiceView(BuyerService buyerService, SellerService sellerService, InvoicesService invoicesService) {
        this.buyerService = buyerService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 header = new H1("📝 Wystaw fakturę");
        header.getStyle().set("text-align", "center");
        add(header);
        setHorizontalComponentAlignment(Alignment.CENTER, header);

        HorizontalLayout centerLayout = new HorizontalLayout();
        centerLayout.setWidthFull();
        centerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);


        FormLayout invoiceHeaderLayout = new FormLayout();
        invoiceHeaderLayout.setWidth("650px");
        invoiceHeaderLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 7));
        invoiceHeaderLayout.setWidthFull();

        TextField numberField = new TextField("Numer faktury");

        numberField.setValue(invoicesService.invoicesNumber());


        DatePicker dateOfIssueField = new DatePicker("Data wystawienia");

        TextField placeField = new TextField("Miejsce wystawienia");

        DatePicker dateOfSaleField = new DatePicker("Data sprzedaży");

        NumberField postponementField = new NumberField("Odroczenie (dni)");

        DatePicker paymentDateField = new DatePicker("Termin płatności");

        //  płatności pobierany z serwisu
        ComboBox<String> paymentTypeField = new ComboBox<>("Forma płatności");
        paymentTypeField.setItems(invoicesService.paymentsMethod());

        invoiceHeaderLayout.add(numberField, dateOfIssueField, placeField, dateOfSaleField, postponementField, paymentDateField, paymentTypeField);

        add(invoiceHeaderLayout);


        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("300px");
        leftLayout.setPadding(true);
//sprzedawca lewa strona
        Span sellerTitle = new Span("Sprzedawca");
        sellerTitle.getStyle().set("font-weight", "bold");

        SellerDto seller = sellerService.findByUserEmail();
        Span name = new Span("Firma: " + seller.name());
        Span nip = new Span("NIP: " + seller.nip());
        Span regon = new Span("REGON: " + seller.regon());
        Span address = new Span("Adres: " + seller.street() + " " + seller.houseNumber() + ", " + seller.zipCode() + " " + seller.city());

        leftLayout.add(sellerTitle, name, nip, regon, address);

        // Nabynca prawa strona
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("300px");
        rightLayout.setPadding(true);

        Span buyerTitle = new Span("Nabywca");
        buyerTitle.getStyle().set("font-weight", "bold");

        nipField = new TextField("NIP kontrahenta");
        Button loadButton = new Button("Wczytaj", event -> findByNip());

        buyerDataLayout = new VerticalLayout(); // dane nabywcy
        buyerDataLayout.setSpacing(false);
        buyerDataLayout.setPadding(false);

        rightLayout.add(buyerTitle, nipField, loadButton, buyerDataLayout);


        centerLayout.add(leftLayout, rightLayout);
        add(centerLayout);
    }

    private void findByNip() {
        buyerDataLayout.removeAll();
        try {
            BuyerDto buyer = buyerService.findByNip(nipField.getValue());
            Span buyerName = new Span("Firma: " + buyer.name());
            Span buyerNip = new Span("NIP: " + buyer.nip());
            Span buyerRegon = new Span("REGON: " + buyer.regon());
            Span buyerAddress = new Span("Adres: " + buyer.street() + " " + buyer.houseNumber() + ", " + buyer.zipCode() + " " + buyer.city());

            buyerDataLayout.add(buyerName, buyerNip, buyerRegon, buyerAddress);

        } catch (NipNotFoundException | CustomValidationException | RestClientResponseException ex) {
            Notification.show("Nieprawidłowy Nip ", 5000, Notification.Position.MIDDLE);

        }

    }
}
