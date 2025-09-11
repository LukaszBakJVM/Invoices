package org.lukasz.faktury.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
@Route("dashbord")
public class DashboardView extends VerticalLayout {
    public DashboardView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H1 header = new H1("📑 System Faktur");
        Paragraph intro = new Paragraph("Witaj w systemie do zarządzania fakturami. Wybierz jedną z opcji:");


        RouterLink newInvoice = new RouterLink("➕ Wystaw fakturę", NewInvoiceView.class);
        RouterLink invoicesList = new RouterLink("📂 Lista faktur", InvoiceListView.class);
        RouterLink clients = new RouterLink("👥 Klienci", ClientsView.class);
        RouterLink reports = new RouterLink("📊 Raporty", ReportsView.class);

        add(header, intro, newInvoice, invoicesList, clients, reports);
    }
}

