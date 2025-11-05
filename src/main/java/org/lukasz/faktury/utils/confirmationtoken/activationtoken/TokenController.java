package org.lukasz.faktury.utils.confirmationtoken.activationtoken;

import org.lukasz.faktury.utils.confirmationtoken.activationtoken.dto.EmailRequest;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.ResetPasswordService;
import org.lukasz.faktury.utils.confirmationtoken.resetpasswordtoken.dto.ConfirmPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class TokenController {
    private final ActivationTokenService activationToken;
    private final ResetPasswordService resetPasswordService;

    public TokenController(ActivationTokenService activationToken, ResetPasswordService resetPasswordService) {
        this.activationToken = activationToken;
        this.resetPasswordService = resetPasswordService;
    }
    @GetMapping("registration/{token}")
    ResponseEntity<Void>findToken(@PathVariable String token){
        activationToken.findToken(token);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/resetPassword")
    ResponseEntity<Void> createToken(@RequestBody EmailRequest request) {
        resetPasswordService.createToken(request.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @PostMapping("/newPassword")
    ResponseEntity<Void> newPassword(@RequestBody ConfirmPassword confirmPassword) {
        resetPasswordService.newPassword(confirmPassword);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/{token}")
    ResponseEntity<Void>findResetToken(@PathVariable String token){
        resetPasswordService.findToken(token);
        return ResponseEntity.ok().build();
    }


}



