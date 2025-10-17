package org.lukasz.faktury.seller;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepo extends JpaRepository<Seller,Long> {

    Optional<Seller>findByUserEmail(String email);

    Optional<Seller> findByNipAndName(String nip, String name);

}
