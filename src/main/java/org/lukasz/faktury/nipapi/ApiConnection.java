package org.lukasz.faktury.nipapi;

import org.lukasz.faktury.nipapi.ceidgapi.CeidgNipApiResponse;

public interface ApiConnection {
    CeidgNipApiResponse result(String nip);

}
