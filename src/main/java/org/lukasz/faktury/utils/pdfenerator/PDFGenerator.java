package org.lukasz.faktury.utils.pdfenerator;

import org.lukasz.faktury.invoices.dto.InvoicesDto;

import java.io.IOException;
import java.util.List;

public interface PDFGenerator {
    public byte[] generatePDFsIn(List<InvoicesDto> invoices) throws InterruptedException, IOException;
}
