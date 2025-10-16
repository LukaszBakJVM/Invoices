package org.lukasz.faktury.views.invoice;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.lukasz.faktury.buyer.BuyerService;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.exceptions.*;
import org.lukasz.faktury.invoices.InvoicesService;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.invoices.dto.InvoicesPdf;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.pdfenerator.PDFGenerator;
import org.lukasz.faktury.views.user.DashboardView;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Route("newinvoice")
@PermitAll

public class NewInvoiceView extends VerticalLayout {


    private final BuyerService buyerService;
    private final List<InvoiceItemsDto> items = new ArrayList<>();
    private final PDFGenerator pdfGenerator;

    private final InvoiceItemsService invoiceItemsService;
    private final InvoicesService invoicesService;
    private final AtomicBoolean updating;

    private final TextField nipField;
    private final ComboBox<String> tax;
    private final ComboBox<String> unit;
    // dane faktury
    TextField numberField = new TextField("Numer faktury");
    DatePicker dateOfIssueField = new DatePicker("Data wystawienia");
    TextField placeField = new TextField("Miejsce wystawienia");
    DatePicker dateOfSaleField = new DatePicker("Data sprzedaży");
    IntegerField postponementField = new IntegerField("Odroczenie (dni)");
    DatePicker paymentDateField = new DatePicker("Termin płatności");
    ComboBox<String> paymentTypeField = new ComboBox<>("Forma płatności");



    //items
    private final Grid<InvoiceItemsDto> invoiceItemsGrid;
    private final GridListDataView<InvoiceItemsDto> dataView;

    private final TextField descriptionField = new TextField("Nazwa");
    private final IntegerField quantityField = new IntegerField("Ilość");
    private final BigDecimalField nettoField = new BigDecimalField("Cena Netto");
    private final BigDecimalField bruttoField = new BigDecimalField("Cena Brutto");
    private final BigDecimalField totalValue = new BigDecimalField("Wartość");

    Button homeButton = new Button("← Powrót na poprzednia stronę", e -> UI.getCurrent().navigate(DashboardView.class));
    private final BigDecimalField sumNettoField = new BigDecimalField("Suma netto");
    private final BigDecimalField sumTaxField = new BigDecimalField("Podatek");
    private final BigDecimalField sumBruttoField = new BigDecimalField("Suma brutto");


    private final VerticalLayout buyerDataLayout;

    public NewInvoiceView(BuyerService buyerService, SellerService sellerService, PDFGenerator pdfGenerator, InvoiceItemsService invoiceItemsService, InvoicesService invoicesService, AtomicBoolean updating) {
        this.buyerService = buyerService;
        this.pdfGenerator = pdfGenerator;
        this.invoiceItemsService = invoiceItemsService;
        this.invoicesService = invoicesService;
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


        //nr faktury
        numberField.setValue(invoicesService.invoicesNumber());

        //data platnosci

        dateOfIssueField.addValueChangeListener(e -> calculatePaymentDate());
        postponementField.addValueChangeListener(e -> calculatePaymentDate());


        //  typ płatności pobierany z serwisu



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
        dataView = invoiceItemsGrid.setItems(items);


        invoiceItemsGrid.addColumn(item -> {
            int index = new ArrayList<>(dataView.getItems().toList()).indexOf(item) + 1;
            return String.valueOf(index);
        }).setHeader("Lp");





        invoiceItemsGrid.addColumn(InvoiceItemsDto::description).setHeader("Nazwa");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::quantity).setHeader("iość");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::unit).setHeader("Jednostka");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::priceNetto).setHeader("Cena Netto");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::tax).setHeader("Vat");
        invoiceItemsGrid.addColumn(InvoiceItemsDto::priceBrutto).setHeader("Cena Brutto");

        invoiceItemsGrid.addColumn(InvoiceItemsDto::totalValue).setHeader("Wartośc");

        invoiceItemsGrid.addComponentColumn(this::deleteButton);


        centerInvoice.setWidthFull();



        descriptionField.setWidth("150px");


        quantityField.setWidth("150px");
        quantityField.addValueChangeListener(event->calculateTotalValue());

        unit = new ComboBox<>("Jednostka");
        unit.setItems(invoiceItemsService.unit());
        unit.setWidth("150px");
        unit.setPlaceholder("Jednostka");


        nettoField.setWidth("150px");


        nettoField.addValueChangeListener(event -> nettoToBrutto());




        tax = new ComboBox<>("Vat");
        tax.setItems(invoiceItemsService.tax());
        tax.setWidth("105px");
        tax.setPlaceholder("Vat");
        tax.setValue("VAT23");


        bruttoField.setWidth("105px");
        bruttoField.addValueChangeListener(event -> bruttoToNetto());
        bruttoField.addValueChangeListener(event->calculateTotalValue());


        totalValue.setWidth("105px");


        Button addItemButton = new Button("Dodaj pozycję",p->addItem());


        HorizontalLayout fields = new HorizontalLayout();
        fields.add(descriptionField, quantityField, unit, nettoField, tax, bruttoField,totalValue);

        centerInvoice.add(invoiceItemsGrid, fields, addItemButton);
//pobierz
        Button saveAndDownloads = new Button("Zapisz i pobierz", new Icon(VaadinIcon.DOWNLOAD));
        saveAndDownloads.addClickListener(event -> saveAndDownloads());


        HorizontalLayout downLayout = new HorizontalLayout(saveAndDownloads, sumNettoField, sumTaxField, sumBruttoField);
        Div spacer = new Div();
        downLayout.add(spacer, sumNettoField, sumTaxField, sumBruttoField);
        downLayout.expand(spacer);
        downLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        centerInvoice.add(invoiceItemsGrid, downLayout, homeButton);


        rightLayout.add(buyerTitle, nipField, loadButton, buyerDataLayout);



        centerLayout.add(leftLayout, centerInvoice, rightLayout);
        add(centerLayout);
    }

    private void calculatePaymentDate() {
        if (dateOfIssueField.isEmpty() || postponementField.isEmpty()) {
            return;
        }
        LocalDate dateOfIssue = dateOfIssueField.getValue();
        Integer postponement = postponementField.getValue();
        LocalDate paymentDate = invoicesService.calculatePaymentDate(dateOfIssue, postponement);
        paymentDateField.setValue(paymentDate);


    }

    private void saveAndDownloads() {
        try {
            invoicesCheck();


            InvoicesDto invoicesDto = new InvoicesDto(numberField.getValue(), dateOfIssueField.getValue(), placeField.getValue(), dateOfSaleField.getValue()
                    , postponementField.getValue(), paymentDateField.getValue(), paymentTypeField.getValue());


            BuyerDto buyer = buyerService.findByNipAndSave(nipField.getValue());

            invoicesService.createInvoices(invoicesDto, buyer, items);
            TotalValues totalValues = new TotalValues(sumNettoField.getValue(),sumTaxField.getValue(),sumBruttoField.getValue());

            byte[] pdfBytes = pdfGenerator.generatePDF(new InvoicesPdf(invoicesDto, buyer, items,totalValues));

            String number = String.format("%s.pdf", invoicesDto.number());

            getUI().ifPresent(ui -> ui.getPage().executeJs("var link = document.createElement('a');" + "link.href = 'data:application/pdf;base64,' + $0;" + "link.download = $1;" + "link.click();", Base64.getEncoder().encodeToString(pdfBytes), number));


            items.clear();
            dataView.refreshAll();
            clearInvoicesFields();

        } catch (IOException | CustomValidationException | AccountNumberException | NipConflictException |
                 NipNotFoundException ex) {
            Notification.show(ex.getMessage(),4000, Notification.Position.MIDDLE);
        }

    }


    private void clearInvoicesFields() {
        numberField.setValue(invoicesService.invoicesNumber());
        dateOfIssueField.clear();
        placeField.clear();
        dateOfSaleField.clear();
        postponementField.clear();
        paymentDateField.clear();
        paymentTypeField.clear();

    }


// znajdz po nipie

    private void findByNip() {
        buyerDataLayout.removeAll();
        try {
            BuyerDto buyer = buyerService.findByNipAndSave(nipField.getValue());
            Span buyerName = new Span("Firma: " + buyer.name());
            Span buyerNip = new Span("NIP: " + buyer.nip());
            Span buyerRegon = new Span("REGON: " + buyer.regon());
            Span buyerAddress = new Span("Adres: " + buyer.street() + " " + buyer.houseNumber() + ", " + buyer.zipCode() + " " + buyer.city());

            buyerDataLayout.add(buyerName, buyerNip, buyerRegon, buyerAddress);

        } catch (NipNotFoundException | CustomValidationException | RestClientResponseException ex) {
            Notification.show("Nieprawidłowy Nip ", 5000, Notification.Position.MIDDLE);

        }
    }

       private void addItem(){


           try {


               InvoiceItemsDto invoiceItemsDto = getInvoiceItemsDto();
               if (items.contains(invoiceItemsDto)) {
                   throw new ItemExistException(("Pozycja  już jest na fakturze"));
               }


               items.add(invoiceItemsDto);
               invoiceItemsGrid.setItems(items);


               valueUpdate();
            clearFields();


           } catch (CustomValidationException | ItemExistException ex) {
            Notification.show(ex.getMessage(),3000, Notification.Position.MIDDLE);
        }


       }
    private Button deleteButton(InvoiceItemsDto item) {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));


            deleteButton.addClickListener(e->{
                items.remove(item);
                reduceTotalValues(item.quantity(),item.priceNetto(),item.tax(),item.totalValue());
                dataView.refreshAll();
            });




        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON);
        return deleteButton;
    }

    private void reduceTotalValues(int quantity, BigDecimal priceNetto,BigDecimal tax,BigDecimal totalValue){
        BigDecimal netto = sumNettoField.getValue().subtract(invoiceItemsService.reduceTotalValues(priceNetto, quantity));
        sumNettoField.setValue(netto);

        BigDecimal vat = sumTaxField.getValue().subtract(invoiceItemsService.reduceTotalValues(tax, quantity));
        sumTaxField.setValue(vat);

        BigDecimal brutto = sumBruttoField.getValue().subtract(totalValue);
        sumBruttoField.setValue(brutto);


    }

    private InvoiceItemsDto getInvoiceItemsDto() {

        if (descriptionField.getValue() == null || quantityField.getValue() == null || unit.getValue() == null || nettoField.getValue() == null || tax == null || bruttoField.getValue() == null) {
            throw new CustomValidationException("Uzupełnij wszystkie pola");

        }

        BigDecimal tax = bruttoField.getValue().subtract(nettoField.getValue());


        return new InvoiceItemsDto(descriptionField.getValue(), quantityField.getValue(), unit.getValue(), nettoField.getValue(), tax, bruttoField.getValue(), totalValue.getValue());

    }
//wylicz netto ->brutto

    private void nettoToBrutto() {
        if ( updating.get()){
            return;
        }
        updating.set(true);

        BigDecimal brutto = invoiceItemsService.nettoToBrutto(nettoField.getValue(), tax.getValue());

        bruttoField.setValue(brutto);
        updating.set(false);


    }
//wylicz brutto -> netto
    private void bruttoToNetto() {
        if (updating.get()){
            return;
        }
        updating.set(true);

        BigDecimal netto = invoiceItemsService.bruttoToNetto(bruttoField.getValue(), tax.getValue());

        nettoField.setValue(netto);
        updating.set(false);


    }
//wyczysc pola
    private void clearFields() {
        updating.set(true);
        descriptionField.clear();
        quantityField.clear();
        nettoField.clear();
        bruttoField.clear();
        totalValue.clear();
        updating.set(false);
    }
//calkowita wartosc
    private void valueUpdate() {

        List<BigDecimal> prices = totalPrice(nettoField.getValue(), bruttoField.getValue(), quantityField.getValue());

        calculateTotalTax();

        sumNettoField.setValue(prices.get(0));
        sumBruttoField.setValue(prices.get(1));



    }


    private BigDecimal checkEmpty(BigDecimalField value) {
        if (value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return value.getValue();
    }
    private void calculateTotalValue() {

            if (bruttoField.getValue() == null || quantityField.getValue() == null) {
                return;
            }
        BigDecimal total = invoiceItemsService.calculateTotalValue(bruttoField.getValue(), quantityField.getValue());
        totalValue.setValue(total);

    }
    private void calculateTotalTax(){
        sumTaxField.setValue(checkEmpty(sumTaxField));
        BigDecimal value = sumTaxField.getValue();
        BigDecimal calculateTax = bruttoField.getValue().subtract(nettoField.getValue());
        BigDecimal totalTax = calculateTax.multiply(BigDecimal.valueOf(quantityField.getValue())).add(value);
        sumTaxField.setValue(totalTax);

    }
    private List<BigDecimal> totalPrice(BigDecimal priceNetto ,BigDecimal priceBrutto,int quantity){
        sumNettoField.setValue(checkEmpty(sumNettoField));
        sumBruttoField.setValue(checkEmpty(sumBruttoField));

        BigDecimal calculateNetto= invoiceItemsService.calculateTotalValue(priceNetto, quantity);
        BigDecimal totalNetto = sumNettoField.getValue().add(calculateNetto);

        BigDecimal calculateBrutto = invoiceItemsService.calculateTotalValue(priceBrutto, quantity);
        BigDecimal totalBrutto = sumBruttoField.getValue().add(calculateBrutto);

        return List.of(totalNetto,totalBrutto);

    }

    private void invoicesCheck() {
        if (numberField.isEmpty() || dateOfIssueField.isEmpty() || placeField.isEmpty() || dateOfSaleField.isEmpty() || postponementField.isEmpty() || paymentDateField.isEmpty() || paymentTypeField.isEmpty()) {
            throw new CustomValidationException("Uzupełnij wszystkie pola");
        }
    }


}



