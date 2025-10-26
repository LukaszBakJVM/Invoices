package org.lukasz.faktury.utils.pdfenerator;

import org.lukasz.faktury.invoices.dto.InvoicesPdf;

import java.io.IOException;

public interface PDFGenerator {

     byte[] generatePDF(InvoicesPdf invoice,String email) throws IOException;
}
