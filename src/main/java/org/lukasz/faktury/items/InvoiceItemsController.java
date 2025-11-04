package org.lukasz.faktury.items;

import org.lukasz.faktury.items.dto.BruttoToNetto;
import org.lukasz.faktury.items.dto.CalculateTotalValue;
import org.lukasz.faktury.items.dto.NettoToBrutto;
import org.lukasz.faktury.items.dto.ReduceTotalValues;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/invoiceItems")       // /invoiceItems/reduceTotalValues
public class InvoiceItemsController {
    private final InvoiceItemsService service;

    public InvoiceItemsController(InvoiceItemsService service) {
        this.service = service;
    }
    @GetMapping("/tax")
    ResponseEntity<List<String>>tax(){
        return ResponseEntity.ok(service.tax());
    }
    @GetMapping("/unit")
    ResponseEntity<List<String>>unit(){
        return ResponseEntity.ok(service.unit());
    }
    @GetMapping("/nettoToBrutto")
    ResponseEntity<NettoToBrutto>nettoToBrutto(@RequestParam BigDecimal priceNetto , @RequestParam String tax){
        return ResponseEntity.ok(service.nettoToBrutto(priceNetto,tax));
    }
    @GetMapping("/bruttoToNetto")
    ResponseEntity<BruttoToNetto>bruttoToNetto(@RequestParam BigDecimal priceBrutto , @RequestParam String tax){
        return ResponseEntity.ok(service.bruttoToNetto(priceBrutto,tax));
    }
    @GetMapping("/calculateTotalValue")
    ResponseEntity<CalculateTotalValue>calculateTotalValue(@RequestParam BigDecimal priceBrutto, @RequestParam int quantity){
        return ResponseEntity.ok(service.calculateTotalValue(priceBrutto, quantity));
    }
    @GetMapping("/reduceTotalValues")
    ResponseEntity<ReduceTotalValues>reduceTotalValues(@RequestParam BigDecimal price,@RequestParam int quantity){
        return ResponseEntity.ok(service.reduceTotalValues(price, quantity));
    }




}
