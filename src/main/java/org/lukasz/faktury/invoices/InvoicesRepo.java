package org.lukasz.faktury.invoices;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoicesRepo extends JpaRepository<Invoices,Long> {
}
