package org.lukasz.faktury.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoicesRepo extends JpaRepository<Invoices,Long> {


    List<Invoices> findAllBySellers_User_Email(String email);
}
