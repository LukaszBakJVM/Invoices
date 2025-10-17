package org.lukasz.faktury.nipapi;

import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.nipapi.ceidgapi.*;
import org.lukasz.faktury.nipapi.mf.Address;
import org.lukasz.faktury.nipapi.mf.MfNipApiResponse;
import org.lukasz.faktury.nipapi.mf.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Component
public class ApiConnectionImpl implements ApiConnection {
    private final RestClient restClient;
    @Value("${ceidgApi}")
    private String ceidgUrl;
    @Value("${mfApi}")
    private String mfUrl;
    @Value("${tokenCeidg}")
    private String jwtToken;


    public ApiConnectionImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CeidgNipApiResponse result(String nip) {
        if (nip.isEmpty()) {
            throw new NipNotFoundException("Uzupełnij nip nabywcy");

        }

        ResponseEntity<CeidgNipApiResponse> ceidgNipApiResponse = restClient.get().uri(searchByNipCeidg(nip)).header("Authorization", "Bearer " + jwtToken).accept(MediaType.APPLICATION_JSON).retrieve().onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
            if (response.getStatusCode().value() == 400) {
                throw new NipNotFoundException(String.format("Niepoprawny identyfikator NIP [%s]", nip));
            }
        }).toEntity(CeidgNipApiResponse.class);
        if (ceidgNipApiResponse.getStatusCode().value() == 204) {
            return mfResult(nip);

        }

        return checkAddress(ceidgNipApiResponse.getBody());


    }

    private CeidgNipApiResponse checkAddress(CeidgNipApiResponse response) {
        List<CeidgResult> updatedCompany = response.firma().stream().filter(f -> !f.status().equals("WYKRESLONY")).map(firma -> {
            if (firma.adresDzialalnosci().miasto() == null && firma.adresKorespondencyjny().miasto() != null) {
                CorrespondenceAddress ca = firma.adresKorespondencyjny();
                BusinessAddress businessAddress = new BusinessAddress(ca.ulica(), ca.budynek(), ca.kod(), ca.miasto());
                return new CeidgResult(firma.nazwa(), businessAddress, firma.wlasciciel(), firma.status(), firma.adresKorespondencyjny());
            } else {
                return firma;
            }
        }).toList();

        return new CeidgNipApiResponse(updatedCompany);
    }


    private String searchByNipCeidg(final String nip) {
        return fromUriString(ceidgUrl).path("/api/ceidg/v3/firma?nip={nip}").buildAndExpand(nip).toUriString();

    }


    private CeidgNipApiResponse mfResult(String nip) {
        Subject subject = restClient.get().uri(searchByNipMf(nip)).accept(MediaType.APPLICATION_JSON).retrieve().body(MfNipApiResponse.class).result().subject();
        Address address;
        if (subject.workingAddress() != null) {
            address = splitAddress(subject.workingAddress());
        } else {
            address = splitAddress(subject.residenceAddress());

        }
        CeidgResult ceidgResult = new CeidgResult(subject.name(), new BusinessAddress(address.street(), address.houseNumber(), address.zipcode(), address.city()), new Owner(subject.nip(), subject.regon()), "mf", new CorrespondenceAddress(address.street(), address.houseNumber(), address.zipcode(), address.city()));
        return new CeidgNipApiResponse(List.of(ceidgResult));
    }

    private String searchByNipMf(final String nip) {
        LocalDate date = LocalDate.now();
        return fromUriString(mfUrl).path("/api/search/nip/{nip}?date={date}").buildAndExpand(nip, date).toUriString();

    }


    private Address splitAddress(String workingAddress) {
        List<String> data = Arrays.stream(workingAddress.split("[ ,]+")).toList();

        return new Address(data.get(2), data.get(3), data.get(0), data.get(1));

    }
}

