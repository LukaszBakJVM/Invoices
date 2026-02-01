package org.lukasz.faktury.ksef;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Component
public class KsefConnectionImpl {
    @Value("${apiKsef}")
    private String ksefApi;
    private final RestClient restClient;

    public KsefConnectionImpl(RestClient restClient) {
        this.restClient = restClient;
    }


    private String ksefApi(final String path) {
        return fromUriString(ksefApi).path(path).toUriString();

    }


}
