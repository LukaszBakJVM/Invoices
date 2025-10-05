package org.lukasz.faktury.invoices;

import org.lukasz.faktury.Buyer.BuyerService;
import org.lukasz.faktury.Buyer.dto.BuyerDto;
import org.lukasz.faktury.enums.Payment;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.validation.Validation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class InvoicesServiceImpl implements InvoicesService{
    private final InvoicesRepo repo;
    private final InvoicesMapper mapper;
    private final BuyerService buyerService;

    private final Validation validation;
    private final StringBuilder str;
    private final LocalDate today = LocalDate.now();
    LocalDate start = today.withDayOfMonth(1);
    LocalDate end = today.withDayOfMonth(today.lengthOfMonth());


    public InvoicesServiceImpl(InvoicesRepo repo, InvoicesMapper mapper, Validation validation, StringBuilder str, SellerService sellerService, BuyerService buyerService) {
        this.repo = repo;
        this.mapper = mapper;
        this.validation = validation;
        this.str = str;

        this.buyerService = buyerService;
    }

    @Override
    public void createInvoices(InvoicesDto request, BuyerDto buyerDto, List<InvoiceItemsDto> invoiceItemsDtos) {
        //todo
        // validation.validation(request);

        Invoices invoices = mapper.ToEntity(request,today);

        invoices.setBuyer(buyerService.findBuyer(buyerDto.nip()));



        repo.save(invoices);


    }

    @Override
    public List<String> paymentsMethod() {
        return Arrays.stream(Payment.values()).map(Payment::getPAYMENT_TYPE).toList();
    }
    @Override
   public String invoicesNumber(){

        //todo poprawic zly  numer
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Invoices> listNumberList = repo.findAllBySeller_User_EmailAndGeneratedDateOfIssueBetween(email, start, end);


        return calculateNumberOfInvoices(listNumberList);

    }

    @Override
    public LocalDate calculatePaymentDate(LocalDate dateOfIssue, int postponement) {
        return dateOfIssue.plusDays(postponement);
    }


    private String calculateNumberOfInvoices(List<Invoices> invoicesNb) {
        str.setLength(0);
        String date = today.toString();


        List<String> list = Arrays.stream(date.split("-")).toList();

        String initNumber = "FV/";
        if (invoicesNb.isEmpty()) {
            return str.append(initNumber).append("1").append("/").append(list.get(1)).append("/").append(list.get(0)).toString();
        }

        Optional<Invoices> first = invoicesNb.stream().max(Comparator.comparing(Invoices::getGeneratedDateOfIssue));
        Invoices invoices = first.get();

        List<String> incrementNumber = Arrays.stream(invoices.getNumber().split("/")).toList();
        String actualNb = incrementNumber.get(1);
        int increment = increment(actualNb);

        return str.append(initNumber).append("/").append(increment).append("/").append(list.get(2)).append("/").append(list.get(1)).toString();
    }

    private int increment(String number) {
        int nb = Integer.parseInt(number);
        return nb + 1;
    }


}
