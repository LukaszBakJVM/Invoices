package org.lukasz.faktury.views.invoice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.buyer.BuyerService;
import org.lukasz.faktury.invoices.InvoicesService;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.pdfenerator.PDFGenerator;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewInvoiceViewTest {

    @Mock
    BuyerService buyerService;

    @Mock
    SellerService sellerService;

    @Mock
    PDFGenerator pdfGenerator;

    @Mock
    InvoiceItemsService invoiceItemsService;

    @Mock
    InvoicesService invoicesService;

    AtomicBoolean updating = new AtomicBoolean(false);

    @Test
    void shouldCreateViewSuccessfully() {
        // given
        SellerDto seller = new SellerDto("Test Firma", "1234567890", "123456789", "Testowa", "1", "00-000", "Warszawa");

        when(sellerService.findByUserEmail()).thenReturn(seller);
        when(invoicesService.invoicesNumber()).thenReturn("FV/1/2026");
        when(invoicesService.paymentsMethod()).thenReturn(List.of("PRZELEW"));

        // when + then
        assertDoesNotThrow(() -> new NewInvoiceView(buyerService, sellerService, pdfGenerator, invoiceItemsService, invoicesService, updating));
    }
}

