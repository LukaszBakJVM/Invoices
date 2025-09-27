package org.lukasz.faktury.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoicesRepo extends JpaRepository<Invoices,Long> {


    List<Invoices> findAllBySellers_User_EmailAndNumberOfIssueBetween(String email, LocalDate start, LocalDate end);
}
