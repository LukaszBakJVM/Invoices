package org.lukasz.faktury.invoices;

import jakarta.transaction.Transactional;
import org.lukasz.faktury.buyer.BuyerService;
import org.lukasz.faktury.enums.Payment;
import org.lukasz.faktury.exceptions.AccountNumberException;
import org.lukasz.faktury.exceptions.NipConflictException;
import org.lukasz.faktury.invoices.dto.CalculatePaymentDate;
import org.lukasz.faktury.invoices.dto.InvoicesDtoRequest;
import org.lukasz.faktury.invoices.dto.InvoicesNumber;
import org.lukasz.faktury.items.InvoiceItemsService;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.validation.Validation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class InvoicesServiceImpl implements InvoicesService{
    private final InvoicesRepo repo;
    private final InvoicesMapper mapper;
    private final BuyerService buyerService;
    private final SellerService sellerService;
    private final InvoiceItemsService invoiceItemsService;

    private final Validation validation;
    private final StringBuilder str;
    private final LocalDate today = LocalDate.now();

    LocalDate start = today.withDayOfMonth(1);
    LocalDate end = today.withDayOfMonth(today.lengthOfMonth());


    public InvoicesServiceImpl(InvoicesRepo repo, InvoicesMapper mapper, Validation validation, StringBuilder str, SellerService sellerService, BuyerService buyerService, InvoiceItemsService invoiceItemsService) {
        this.repo = repo;
        this.mapper = mapper;
        this.validation = validation;
        this.str = str;

        this.buyerService = buyerService;
        this.sellerService = sellerService;
        this.invoiceItemsService = invoiceItemsService;
    }

    @Override
    @Transactional
    public void createInvoices(InvoicesDtoRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Seller seller = sellerService.findByEmail();
        if (seller.getNip().equals(request.buyerDto().nip())) {
            throw new NipConflictException("Podano ten sam NIP dla sprzedawcy i nabywcy");
        }


        validation.validation(request);

        Invoices invoices = mapper.ToEntity(request.invoicesDto(), now);

        if (seller.getAccountNb() == null && !request.invoicesDto().typOfPayment().equals("Gotówka")) {
            throw new AccountNumberException("Uzupełnij nr konta");
        }


        invoices.setBuyer(buyerService.findBuyer(request.buyerDto()));
        invoices.setSeller(seller);


        Invoices save = repo.save(invoices);

        invoiceItemsService.saveItems(request.invoiceItemsDtos(), save);


    }

    @Override
    public List<String> paymentsMethod() {
        return Arrays.stream(Payment.values()).map(Payment::getPAYMENT_TYPE).toList();
    }
    @Override
    public InvoicesNumber invoicesNumber(String email) {



        List<Invoices> listNumberList = repo.findAllBySeller_User_EmailAndGeneratedDateOfIssueBetween(email, start.atStartOfDay(), end.atTime(LocalTime.MAX));



        return calculateNumberOfInvoices(listNumberList);

    }

    @Override
    public CalculatePaymentDate calculatePaymentDate(LocalDate dateOfIssue, int postponement) {
        return new CalculatePaymentDate(dateOfIssue.plusDays(postponement));
    }


    private InvoicesNumber calculateNumberOfInvoices(List<Invoices> invoicesNb) {
        str.setLength(0);
        String date = today.toString();


        List<String> list = Arrays.stream(date.split("-")).toList();

        String initNumber = "FV/";
        if (invoicesNb.isEmpty()) {
            return new InvoicesNumber(str.append(initNumber).append("1").append("/").append(list.get(1)).append("/").append(list.get(0)).toString());
        }

        Optional<Invoices> first = invoicesNb.stream().sorted(Comparator.comparing(Invoices::getGeneratedDateOfIssue).reversed()).findFirst();
        Invoices invoices = first.get();

        List<String> incrementNumber = Arrays.stream(invoices.getNumber().split("/")).toList();
        String actualNb = incrementNumber.get(1);
        int increment = increment(actualNb);

        return new InvoicesNumber(str.append(initNumber).append(increment).append("/").append(list.get(1)).append("/").append(list.get(0)).toString());
    }

    private int increment(String number) {
        int nb = Integer.parseInt(number);
        nb++;
        return nb;
    }


}
