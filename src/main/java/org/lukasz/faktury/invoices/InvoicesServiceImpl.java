package org.lukasz.faktury.invoices;

import org.lukasz.faktury.gusapi.NipApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Service
public class InvoicesServiceImpl {
    private final RestClient restClient;
    private final LocalDate localDate = LocalDate.now();

    public InvoicesServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

   NipApiResponse result(String nip) {
        return restClient.get().uri(searchByNip(nip)).retrieve().body(NipApiResponse.class);
    }

    private String searchByNip(final String nip) {

        return UriComponentsBuilder.fromUriString("/api/search/nip/{nip}?date={date}").buildAndExpand(nip, localDate).toUriString();
    }
}
