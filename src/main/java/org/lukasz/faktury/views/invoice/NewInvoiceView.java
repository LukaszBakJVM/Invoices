package org.lukasz.faktury.views.invoice;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.lukasz.faktury.Buyer.BuyerService;
import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.exceptions.CustomValidationException;
import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.invoices.InvoicesService;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Route("newinvoice")
@PermitAll

public class NewInvoiceView extends VerticalLayout {


    private final BuyerService buyerService;
    private final Grid<InvoiceItemsDto> invoiceItemsGrid;
    private final InvoiceItemsService invoiceItemsService;
    private final AtomicBoolean updating;

    private final TextField nipField;
    private final ComboBox<String> tax;
    private final ComboBox<String> unit;
    private final List<InvoiceItemsDto> items = new ArrayList<>();
    TextField descriptionField = new TextField("Nazwa");
    IntegerField quantityField = new IntegerField ("Ilość");
    BigDecimalField nettoField = new BigDecimalField("Cena Netto");
    BigDecimalField bruttoField = new BigDecimalField("Cena Brutto");
    BigDecimalField totalValue = new BigDecimalField("Wartość");


    BigDecimalField sumNettoField = new BigDecimalField("Suma netto");
    BigDecimalField sumTaxField = new BigDecimalField("Podatek");
    BigDecimalField sumBruttoField = new BigDecimalField("Suma brutto");




    private final VerticalLayout buyerDataLayout;

    public NewInvoiceView(BuyerService buyerService, SellerService sellerService, InvoicesService invoicesService, InvoiceItemsService invoiceItemsService, AtomicBoolean updating) {
        this.buyerService = buyerService;
        this.invoiceItemsService = invoiceItemsService;
        this.updating = updating;


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

        VerticalLayout centerInvoice = new VerticalLayout();

// faktura
        invoiceItemsGrid = new Grid<>(InvoiceItemsDto.class, false);


        invoiceItemsGrid.addColumn(InvoiceItemsDto::description).setHeader("Nazwa");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::quantity).setHeader("iość");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::unit).setHeader("Jednostka");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::priceNetto).setHeader("Cena Netto");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::tax).setHeader("Vat");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::priceBrutto).setHeader("Cena Brutto");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::totalValue).setHeader("Wartośc");

        centerInvoice.setWidthFull();



        descriptionField.setWidth("150px");


        quantityField.setWidth("150px");

        unit = new ComboBox<>("Jednostka");
        unit.setItems(invoiceItemsService.unit());
        unit.setWidth("150px");
        unit.setPlaceholder("Jednostka");

//todo  poprawic brutto
        nettoField.setWidth("150px");


        nettoField.addValueChangeListener(event -> nettoToBrutto());




        tax = new ComboBox<>("Vat");
        tax.setItems(invoiceItemsService.tax());
        tax.setWidth("105px");
        tax.setPlaceholder("Vat");
        tax.setValue("VAT23");

//todo bruttoto netto
        bruttoField.setWidth("105px");
        bruttoField.addValueChangeListener(event -> bruttoToNetto());
        //todo aktualizacja wartosci
        totalValue.setWidth("105px");



        Button addItemButton = new Button("Dodaj pozycję",p->addItem());

        HorizontalLayout fields = new HorizontalLayout();
        fields.add(descriptionField, quantityField, unit, nettoField, tax, bruttoField,totalValue);

        centerInvoice.add(invoiceItemsGrid, fields, addItemButton);


        HorizontalLayout downLayout = new HorizontalLayout(sumNettoField, sumTaxField, sumBruttoField);
        Div spacer = new Div();
        downLayout.add(spacer, sumNettoField, sumTaxField, sumBruttoField);
        downLayout.expand(spacer);
        downLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        centerInvoice.add(invoiceItemsGrid, downLayout);

        rightLayout.add(buyerTitle, nipField, loadButton, buyerDataLayout);



        centerLayout.add(leftLayout, centerInvoice, rightLayout);
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
    //todo validacja i  wartosc
       private void addItem(){
        try {

            if (descriptionField.getValue() == null || quantityField.getValue() == null || unit.getValue() == null || nettoField.getValue() == null || tax == null || bruttoField.getValue() == null) {
                throw new CustomValidationException("Uzupełnij wszystkie pola");

            }
            BigDecimal tax = bruttoField.getValue().subtract(nettoField.getValue());


            InvoiceItemsDto invoiceItemsDto = new InvoiceItemsDto(descriptionField.getValue(), quantityField.getValue(), unit.getValue(), nettoField.getValue(), tax, bruttoField.getValue(), totalValue.getValue());
            items.add(invoiceItemsDto);
            invoiceItemsGrid.setItems(items);
            invoiceItemsGrid.getDataProvider();
            valueUpdate();
            clearFields();


        }catch (CustomValidationException ex){
            Notification.show(ex.getMessage(),3000, Notification.Position.MIDDLE);
        }


       }


    private void nettoToBrutto() {
        if ( updating.get()){
            return;
        }
        updating.set(true);

        BigDecimal brutto = invoiceItemsService.nettoToBrutto(nettoField.getValue(), tax.getValue());

        bruttoField.setValue(brutto);
        updating.set(false);


    }

    private void bruttoToNetto() {
        if (updating.get()){
            return;
        }
        updating.set(true);

        BigDecimal netto = invoiceItemsService.bruttoToNetto(bruttoField.getValue(), tax.getValue());

        nettoField.setValue(netto);
        updating.set(false);


    }

    private void clearFields() {
        updating.set(true);
        descriptionField.clear();
        quantityField.clear();
        nettoField.clear();
        bruttoField.clear();
        updating.set(false);
    }

    private void valueUpdate() {

        sumNettoField.setValue(checkEmpty(sumNettoField));
        BigDecimal netto = sumNettoField.getValue().add(nettoField.getValue());
        sumNettoField.setValue(netto);



        sumTaxField.setValue(checkEmpty(sumTaxField));
        BigDecimal value = sumTaxField.getValue();
        BigDecimal tax = bruttoField.getValue().subtract(nettoField.getValue());

        sumTaxField.setValue(value.add(tax));



        sumBruttoField.setValue(checkEmpty(sumBruttoField));
        BigDecimal brutto = sumBruttoField.getValue().add(bruttoField.getValue());
        sumBruttoField.setValue(brutto);



    }

    private BigDecimal checkEmpty(BigDecimalField value) {
        if (value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return value.getValue();
    }


}
