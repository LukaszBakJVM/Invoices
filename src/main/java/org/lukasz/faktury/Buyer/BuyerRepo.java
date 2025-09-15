package org.lukasz.faktury.Buyer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuyerRepo extends JpaRepository<Buyer,Long> {
    Optional<Buyer>findByNip(String nip);
}
