package org.lukasz.faktury.invoices;

import org.lukasz.faktury.enums.Payment;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.utils.validation.Validation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InvoicesServiceImpl implements InvoicesService{
    private final InvoicesRepo repo;
    private final InvoicesMapper mapper;
    private final Validation validation;
    private final SellerService sellerService;


    public InvoicesServiceImpl(InvoicesRepo repo, InvoicesMapper mapper, Validation validation, SellerService sellerService) {
        this.repo = repo;
        this.mapper = mapper;
        this.validation = validation;
        this.sellerService = sellerService;
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
    //todo
   public String invoicesNumber(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Invoices> bySellersNip = repo.findAllBySellers_User_Email(email);
        System.out.println();

        return "aaaaa";


   }


}
