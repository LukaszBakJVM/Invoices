package org.lukasz.faktury.invoices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.buyer.Buyer;
import org.lukasz.faktury.buyer.BuyerService;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.validation.Validation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoicesServiceImplTest {
    @Mock
    InvoicesRepo repo;
    @Mock
    InvoicesMapper mapper;
    @Mock
    SellerService sellerService;
    @Mock
    BuyerService buyerService;
    @Mock
    InvoiceItemsService invoiceItemsService;
    @Mock
    Validation validation;
    @InjectMocks
    InvoicesServiceImpl invoicesService;

    @Test
    void shouldCreateNewInvoices() {

        Seller seller = new Seller();
        seller.setNip("1234567");

        LocalDate dateOfIssue = LocalDate.of(2026, 2, 11);

        InvoicesDto invoicesDto = new InvoicesDto("FV/01/2025", dateOfIssue, "Lublin", dateOfIssue, 0, dateOfIssue, "Gotówka");
        BuyerDto buyerDto = new BuyerDto("name", "6351234577", "787273", "city", "00-000", "street", "8");

        InvoiceItemsDto dto = new InvoiceItemsDto("item1", 1, "szt", BigDecimal.valueOf(100.00), BigDecimal.valueOf(23), BigDecimal.valueOf(123), BigDecimal.valueOf(123));

        InvoiceItemsDto dto1 = new InvoiceItemsDto("item2", 2, "szt", BigDecimal.valueOf(10.00), BigDecimal.valueOf(23), BigDecimal.valueOf(12, 3), BigDecimal.valueOf(24.60));

        List<InvoiceItemsDto> itemsDtos = List.of(dto, dto1);

        //when

        when(sellerService.findByEmail()).thenReturn(seller);
        doNothing().when(validation).validation(invoicesDto);
        when(mapper.ToEntity(eq(invoicesDto), any(LocalDateTime.class))).thenReturn(mock(Invoices.class));
        when(buyerService.findBuyer(any(BuyerDto.class))).thenReturn(mock(Buyer.class));


        invoicesService.createInvoices(invoicesDto, buyerDto, itemsDtos);

        verify(sellerService, times(1)).findByEmail();
        verify(validation, times(1)).validation(invoicesDto);


    }
}
