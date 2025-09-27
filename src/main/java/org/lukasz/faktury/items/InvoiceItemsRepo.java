package org.lukasz.faktury.items;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemsRepo extends JpaRepository<InvoiceItems, Long> {
}
