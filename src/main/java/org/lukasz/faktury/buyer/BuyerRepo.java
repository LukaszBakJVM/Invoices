package org.lukasz.faktury.buyer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuyerRepo extends JpaRepository<Buyer,Long> {

    Optional<Buyer> findByNipAndName(String nip, String name);

    List<Buyer> findByNip(String nip);

}