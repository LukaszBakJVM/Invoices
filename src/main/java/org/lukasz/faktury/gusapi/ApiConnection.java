package org.lukasz.faktury.gusapi;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Component
public class ApiConnection {
    private final RestClient restClient;
    private final LocalDate localDate = LocalDate.now();

    public ApiConnection(RestClient restClient) {
        this.restClient = restClient;
    }

    public NipApiResponse result(String nip) {
        return restClient.get().uri(searchByNip(nip)).retrieve().body(NipApiResponse.class);
    }

    private String searchByNip(final String nip) {

        return UriComponentsBuilder.fromUriString("/api/search/nip/{nip}?date={date}").buildAndExpand(nip, localDate).toUriString();
    }
}
