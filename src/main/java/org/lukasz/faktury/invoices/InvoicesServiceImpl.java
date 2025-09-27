package org.lukasz.faktury.invoices;

import org.lukasz.faktury.enums.Payment;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
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
    private final Validation validation;
    private final StringBuilder str;
    private LocalDate today = LocalDate.now();
    LocalDate start = today.withDayOfMonth(1);
    LocalDate end = today.withDayOfMonth(today.lengthOfMonth());


    public InvoicesServiceImpl(InvoicesRepo repo, InvoicesMapper mapper, Validation validation, StringBuilder str, SellerService sellerService) {
        this.repo = repo;
        this.mapper = mapper;
        this.validation = validation;
        this.str = str;
    }

    @Override
    public void createInvoices(InvoicesDto request) {
        validation.validation(request);

        Invoices invoices = mapper.ToEntity(request);
        repo.save(invoices);


    }

    @Override
    public List<String> paymentsMethod() {
        return Arrays.stream(Payment.values()).map(Payment::getPAYMENT_TYPE).toList();
    }
    @Override
   public String invoicesNumber(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Invoices> listNumberList = repo.findAllBySellers_User_EmailAndNumberOfIssueBetween(email, start, end);


        return calculateNumberOfInvoices(listNumberList);

    }


    private String calculateNumberOfInvoices(List<Invoices> invoicesNb) {
        str.setLength(0);
        String date = today.toString();

        List<String> list = Arrays.stream(date.split("-")).toList();
        String initNumber = "FV/";
        if (invoicesNb.isEmpty()) {
            return str.append(initNumber).append("1").append("/").append(list.get(1)).append("/").append(list.get(2)).toString();
        }

        Optional<Invoices> first = invoicesNb.stream().max(Comparator.comparing(Invoices::getNumberOfIssue));
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
