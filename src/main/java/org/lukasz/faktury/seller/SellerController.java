package org.lukasz.faktury.seller;

import org.lukasz.faktury.seller.dto.AccountNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    private final SellerService service;

    public SellerController(SellerService service) {
        this.service = service;
    }

    @PostMapping("/account")
    ResponseEntity<String> addAccountNb(@AuthenticationPrincipal UserDetails user, @RequestBody AccountNumber account) {
        service.addAccountNb(account, user.getUsername());
        return ResponseEntity.ok("Dodano Konto");


    }
}
