package org.lukasz.faktury.nipapi.ceidgapi;

public record CeidgResult(String nazwa, BusinessAddress adresDzialalnosci, Owner wlasciciel, String status,
                          CorrespondenceAddress adresKorespondencyjny) {
}
