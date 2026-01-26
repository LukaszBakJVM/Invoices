package org.lukasz.faktury.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InvoiceItemsServiceImplTest {
    @Mock
    InvoiceItemsRepo invoiceItemsRepo;
    @Mock
    InvoiceItemsMapper invoiceItemsMapper;
    @InjectMocks
    InvoiceItemsServiceImpl invoiceItemsService;


    @Test
    void shouldReturnListOfTax() {

        //when
        List<String> result = invoiceItemsService.tax();

        //then


        Assertions.assertEquals(List.of("VAT23", "VAT8", "VAT5", "VAT0"), result);


    }


    @Test
    void shouldReturnListOfUnit() {


        //when
        List<String> result = invoiceItemsService.unit();

        //then


        Assertions.assertEquals(List.of("szt", "godz", "dni", "m-c", "km", "kg", "inna"), result);


    }

    @Test
    void shouldCalculateBruttoPrice_fromNetto() {
        //given
        BigDecimal netto = BigDecimal.valueOf(111.34);
        String tax = "VAT23";


        //when
        BigDecimal result = invoiceItemsService.nettoToBrutto(netto, tax);

        //then
        Assertions.assertEquals(BigDecimal.valueOf(136.95), result);

    }

    @Test
    void shouldCalculateNettoPrice_fromNBrutto() {
        //given
        BigDecimal brutto = BigDecimal.valueOf(136.95);
        String tax = "VAT23";


        //when
        BigDecimal result = invoiceItemsService.bruttoToNetto(brutto, tax);

        //then
        Assertions.assertEquals(BigDecimal.valueOf(111.34), result);

    }

    @Test
    void shouldCalculateTotalValue() {
        //given
        BigDecimal brutto = BigDecimal.valueOf(123.41);
        int quantity = 3;

        //when
        BigDecimal result = invoiceItemsService.calculateTotalValue(brutto, quantity);

        //then

        Assertions.assertEquals(BigDecimal.valueOf(370.23), result);
    }

}
