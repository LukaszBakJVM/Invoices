package org.lukasz.faktury.invoices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.buyer.Buyer;
import org.lukasz.faktury.buyer.BuyerService;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.exceptions.NipConflictException;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.validation.Validation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        //given

        Seller seller = new Seller();
        seller.setNip("1234567");

        LocalDate dateOfIssue = LocalDate.of(2026, 2, 11);

        InvoicesDto invoicesDto = new InvoicesDto("FV/01/2025", dateOfIssue, "Lublin", dateOfIssue, 0, dateOfIssue, "Gotówka");
        BuyerDto buyerDto = new BuyerDto("name", "6351234577", "787273", "city", "00-000", "street", "8");

        InvoiceItemsDto dto = new InvoiceItemsDto("item1", 1, "szt", BigDecimal.valueOf(100.00), BigDecimal.valueOf(23), BigDecimal.valueOf(123), BigDecimal.valueOf(123));

        InvoiceItemsDto dto1 = new InvoiceItemsDto("item2", 2, "szt", BigDecimal.valueOf(10.00), BigDecimal.valueOf(23), BigDecimal.valueOf(12, 3), BigDecimal.valueOf(24.60));

        List<InvoiceItemsDto> itemsDtos = List.of(dto, dto1);



        when(sellerService.findByEmail()).thenReturn(seller);
        doNothing().when(validation).validation(invoicesDto);
        when(mapper.ToEntity(eq(invoicesDto), any(LocalDateTime.class))).thenReturn(mock(Invoices.class));
        when(buyerService.findBuyer(any(BuyerDto.class))).thenReturn(mock(Buyer.class));

        //when
        invoicesService.createInvoices(invoicesDto, buyerDto, itemsDtos);

        //then
        verify(sellerService, times(1)).findByEmail();
        verify(validation, times(1)).validation(invoicesDto);


    }

    @Test
    void shouldThrowException_whenNipSellerAndBuyerIsSame() {
        //given
        Seller seller = new Seller();
        seller.setNip("6351234577");

        BuyerDto buyerDto = new BuyerDto("name", "6351234577", "787273", "city", "00-000", "street", "8");

        List<InvoiceItemsDto> items = List.of(mock(InvoiceItemsDto.class));
        InvoicesDto invoicesDto = mock(InvoicesDto.class);

        when(sellerService.findByEmail()).thenReturn(seller);
        // //when then
        NipConflictException nipConflictException = assertThrows(NipConflictException.class, () -> invoicesService.createInvoices(invoicesDto, buyerDto, items));
        assertEquals("Podano ten sam NIP dla sprzedawcy i nabywcy", nipConflictException.getMessage());
    }

    @Test
    void shouldCalculatePaymentDate() {
        //given
        LocalDate dateOfIssue = LocalDate.of(2026, 2, 11);
        int postponement = 7;

        //when
        LocalDate result = invoicesService.calculatePaymentDate(dateOfIssue, postponement);

        LocalDate expected = LocalDate.of(2026, 2, 18);
        // then
        assertEquals(expected, result);

    }


    @Test
    void shouldCalculateInvoicesNumberForLoggedUser() {
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);

        when(auth.getName()).thenReturn("test@email.com");
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);


        // given
        Invoices invoices = new Invoices();
        invoices.setGeneratedDateOfIssue(LocalDateTime.of(2025, 11, 18, 18, 25, 56));
        invoices.setNumber("FV/1/11/2025");
        Invoices invoices1 = new Invoices();
        invoices1.setGeneratedDateOfIssue(LocalDateTime.of(2026, 1, 3, 12, 25, 56));
        invoices1.setNumber("FV/1/1/2026");

        List<Invoices> invoicesList = List.of(invoices1, invoices);

        when(repo.findAllBySeller_User_EmailAndGeneratedDateOfIssueBetween(eq("test@email.com"), any(), any())).thenReturn(invoicesList);

        // when
        String result = invoicesService.invoicesNumber();


        // then
        Assertions.assertNotNull(result);
        verify(repo).findAllBySeller_User_EmailAndGeneratedDateOfIssueBetween(eq("test@email.com"), any(), any());


        SecurityContextHolder.clearContext();

    }

    @Test
    void shouldShowPaymentsMethods() {
        //when
        List<String> result = invoicesService.paymentsMethod();


        List<String> methods = List.of("Przelew", "Gotówka");

        //then

        assertEquals(methods, result);
    }


}

