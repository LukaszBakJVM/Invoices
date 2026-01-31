package org.lukasz.faktury.utils.pdfenerator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.invoices.dto.InvoicesPdf;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.views.invoice.TotalValues;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PDFGeneratorImplTest {
    @Mock
    SellerService sellerService;
    @InjectMocks
    PDFGeneratorImpl pdfGenerator;


    @Test
    void shouldGeneratePdfSuccessfully() throws IOException {
        // given
        Seller seller = new Seller();
        seller.setName("Test Seller");


        when(sellerService.findByEmail()).thenReturn(seller);

        InvoicesPdf invoice = createTestInvoice();


        // when
        byte[] result = pdfGenerator.generatePDF(invoice);

        // then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.length > 0);

        verify(sellerService).findByEmail();


    }

    @Test
    void shouldReturnValidPdfHeader() throws IOException {
        // given
        Mockito.when(sellerService.findByEmail()).thenReturn(new Seller());
        InvoicesPdf invoice = createTestInvoice();

        // when
        byte[] pdf = pdfGenerator.generatePDF(invoice);

        // then
        String header = new String(pdf, 0, 4);
        Assertions.assertEquals("%PDF", header);
    }


    private InvoicesPdf createTestInvoice() {
        InvoicesDto invoicesDto = new InvoicesDto("FV/1/2025", LocalDate.now(), "place", LocalDate.now(), 0, LocalDate.now(), "Gotówka");


        BuyerDto buyerDto = new BuyerDto("Test Buyer", "123445", "678", "city", "zipcode", "street,", "houseNumber");


        InvoiceItemsDto item = new InvoiceItemsDto("description", 2, "szt", BigDecimal.valueOf(100), BigDecimal.valueOf(23), BigDecimal.valueOf(123), BigDecimal.valueOf(246));


        TotalValues totalValues = new TotalValues(BigDecimal.valueOf(100), BigDecimal.valueOf(23), BigDecimal.valueOf(123));

        return new InvoicesPdf(invoicesDto, buyerDto, List.of(item), totalValues);
    }


}


