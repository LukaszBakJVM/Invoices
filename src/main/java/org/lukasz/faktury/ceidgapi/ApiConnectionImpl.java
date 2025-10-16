package org.lukasz.faktury.ceidgapi;

import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Component
public class ApiConnectionImpl implements ApiConnection {
    private final RestClient restClient;


    public ApiConnectionImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public NipApiResponse result(String nip) {
        if (nip.isEmpty()) {
            throw new NipNotFoundException("Uzupełnij nip nabywcy");

        }
        return restClient.get().uri(searchByNip(nip)).accept(MediaType.APPLICATION_JSON).retrieve().onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
            if (response.getStatusCode().value() == 400) {
                throw new NipNotFoundException(String.format("Niepoprawny identyfikator NIP  [%s] ", nip));
            }
        })).body(NipApiResponse.class);


    }


    private String searchByNip(final String nip) {


        return fromUriString("/api/ceidg/v3/firma?nip={nip}").buildAndExpand(nip).toUriString();

    }
}

