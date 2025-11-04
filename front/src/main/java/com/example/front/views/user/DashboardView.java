package com.example.front.views.user;

import com.example.front.views.invoice.NewInvoiceView;
import com.example.front.views.reports.ReportsView;
import com.example.front.views.user.dto.SellerDto;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.PermitAll;


@Route("dashbord")
@PageTitle("Moje konto")
@PermitAll

public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H1 header = new H1("📑 System Faktur");

      //  SellerDto byUserEmail = sellerService.findByUserEmail();

        Div sellerInfo = new Div();
      //  sellerInfo.add(new Paragraph("Firma "+byUserEmail.name()),new Paragraph("NIP: " + byUserEmail.nip()), new Paragraph("REGON: " + byUserEmail.regon()), new Paragraph("Adres: " + byUserEmail.street() + " " + byUserEmail.houseNumber() + ", " + byUserEmail.zipCode() + " " + byUserEmail.city()));


        RouterLink newInvoice = new RouterLink("➕ Wystaw fakturę", NewInvoiceView.class);
        RouterLink invoicesList = new RouterLink("📂 Lista faktur", InvoiceListView.class);
        RouterLink clients = new RouterLink("👥 Klienci", ClientsView.class);
        RouterLink reports = new RouterLink("📊 Raporty", ReportsView.class);

        RouterLink account =new RouterLink("➕ Dodaj  nr konta", AccountNb.class);

        add(header, sellerInfo, newInvoice, invoicesList, clients, reports,account);
    }
}

