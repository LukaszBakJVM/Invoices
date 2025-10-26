package org.lukasz.faktury.user;

import org.lukasz.faktury.seller.dto.SellerDto;
import org.lukasz.faktury.user.dto.RegisterDto;
import org.lukasz.faktury.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    ResponseEntity<UserResponse> registration(@RequestBody RegisterDto register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(register));
    }
    @GetMapping("/findByNip")
    ResponseEntity<List<SellerDto>> findByNip(@RequestParam String nip){
        return ResponseEntity.ok(service.findDataByNip(nip));
    }
}
