package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final ActivationTokenService activationToken;

    public TokenController(ActivationTokenService activationToken) {
        this.activationToken = activationToken;
    }
    @GetMapping("/{token}")
    ResponseEntity<Void>findToken(@PathVariable String token){
        activationToken.findToken(token);
        return ResponseEntity.ok().build();

    }
}



