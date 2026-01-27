package org.lukasz.faktury.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.invoices.Invoices;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceItemsServiceImplTest {
    @Mock
    InvoiceItemsRepo invoiceItemsRepo;
    @Mock
    InvoiceItemsMapper invoiceItemsMapper;
    @InjectMocks
    InvoiceItemsServiceImpl invoiceItemsService;


    @Test
    void shouldSaveItemOnInvoices() {

        //given
        Invoices invoices = new Invoices();
        invoices.setNumber("FV/01/01/2025");
        InvoiceItemsDto dto1 = mock(InvoiceItemsDto.class);
        InvoiceItemsDto dto2 = mock(InvoiceItemsDto.class);
        List<InvoiceItemsDto> itemsDto = List.of(dto1, dto2);
        List<InvoiceItems> items = List.of(new InvoiceItems());

        when(invoiceItemsMapper.saveItems(itemsDto, invoices)).thenReturn(items);
        when(invoiceItemsRepo.saveAll(items)).thenReturn(items);

        //when
        invoiceItemsService.saveItems(itemsDto, invoices);

        //then

        verify(invoiceItemsMapper).saveItems(any(), any());
        verify(invoiceItemsRepo).saveAll(any());


    }


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
    void shouldReturnNettoPrice_whenTax_is0() {
        //given
        BigDecimal netto = BigDecimal.valueOf(111.34);
        String tax = "VAT0";


        //when
        BigDecimal result = invoiceItemsService.nettoToBrutto(netto, tax);

        //then
        Assertions.assertEquals(BigDecimal.valueOf(111.34), result);

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
    void shouldReturnBruttoPrice_whenTax_Is0() {
        //given
        BigDecimal brutto = BigDecimal.valueOf(136.95);
        String tax = "VAT0";


        //when
        BigDecimal result = invoiceItemsService.bruttoToNetto(brutto, tax);

        //then
        Assertions.assertEquals(BigDecimal.valueOf(136.95), result);

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
