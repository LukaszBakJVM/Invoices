package org.lukasz.faktury.Seller;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller,Long> {
    Optional<Seller>findByNip(String nip);
}
