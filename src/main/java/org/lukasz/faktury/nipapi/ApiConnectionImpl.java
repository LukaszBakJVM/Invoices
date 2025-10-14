package org.lukasz.faktury.nipapi;

import org.lukasz.faktury.exceptions.NipNotFoundException;
import org.lukasz.faktury.nipapi.ceidgapi.AdresDzialalnosci;
import org.lukasz.faktury.nipapi.ceidgapi.CeidgNipApiResponse;
import org.lukasz.faktury.nipapi.ceidgapi.CeidgResult;
import org.lukasz.faktury.nipapi.ceidgapi.Owner;
import org.lukasz.faktury.nipapi.mf.Address;
import org.lukasz.faktury.nipapi.mf.MfNipApiResponse;
import org.lukasz.faktury.nipapi.mf.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
        CeidgNipApiResponse ceidgNipApiResponse = restClient.get().uri(searchByNipCeidg(nip)).header("Authorization", "Bearer " + jwtToken).accept(MediaType.APPLICATION_JSON).retrieve()

                .body(CeidgNipApiResponse.class);
        if (ceidgNipApiResponse == null ) {
            return mfResult(nip);
        }


        return ceidgNipApiResponse;


    }


    private String searchByNipCeidg(final String nip) {
        return fromUriString(ceidgUrl).path("/api/ceidg/v3/firma?nip={nip}").buildAndExpand(nip).toUriString();

    }

    //todo  private
    private CeidgNipApiResponse mfResult(String nip) {
        Subject subject = restClient.get().uri(searchByNipMf(nip)).accept(MediaType.APPLICATION_JSON).retrieve().body(MfNipApiResponse.class).result().subject();
        Address address;
        if (subject.workingAddress() != null) {
            address = address(subject.workingAddress());
        } else {
            address = address(subject.residenceAddress());

        }
        CeidgResult ceidgResult = new CeidgResult(subject.name(), new AdresDzialalnosci(address.street(), address.houseNumber(), address.zipcode(), address.city()), new Owner(subject.nip(), subject.regon()));
        return new CeidgNipApiResponse(List.of(ceidgResult));
    }

    private String searchByNipMf(final String nip) {
        LocalDate date = LocalDate.now();
        return fromUriString(mfUrl).path("/api/search/nip/{nip}?date={date}").buildAndExpand(nip, date).toUriString();

    }


    private Address address(String workingAddress) {
        List<String> data = Arrays.stream(workingAddress.split("[ ,]+")).toList();

        return new Address(data.get(2), data.get(3), data.get(0), data.get(1));

    }
}

