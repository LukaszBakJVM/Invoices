package org.lukasz.faktury.buyer;

import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.buyer.dto.Nip;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyer")
public class BuyerController {
    private final BuyerService buyerService;

    public BuyerController(BuyerServiceImpl buyerService) {
        this.buyerService = buyerService;
    }
    @PostMapping
    ResponseEntity<BuyerDto> createNewBuyer(@RequestBody Nip nip){
        return ResponseEntity.status(HttpStatus.CREATED).body(buyerService.findByNipAndSave(nip.nip()));
    }
    @PostMapping("/nip")
    ResponseEntity<Void>findByNipAndNameAndSave(@RequestBody BuyerDto request){
        buyerService.findByNipAndNameAndSave(request);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    ResponseEntity<BuyerDto>findByNipAndName(@RequestParam String nip,@RequestParam String companyName ){
        return ResponseEntity.ok(buyerService.findByNipAndName(nip,companyName));
    }
}
