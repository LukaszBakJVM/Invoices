package org.lukasz.faktury.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoicesRepo extends JpaRepository<Invoices,Long> {

    List<Invoices> findAllBySeller_User_EmailAndGeneratedDateOfIssueBetween(String email, LocalDateTime start, LocalDateTime end);
}
