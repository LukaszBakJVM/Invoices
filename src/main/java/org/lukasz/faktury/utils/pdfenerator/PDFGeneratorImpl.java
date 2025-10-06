package org.lukasz.faktury.utils.pdfenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class PDFGeneratorImpl implements PDFGenerator {
    private final ExecutorService executorService;

    public PDFGeneratorImpl() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numThreads);

    }

    @Override

    public byte[] generatePDFsIn(List<InvoicesDto> invoices) throws InterruptedException, IOException {
        ByteArrayOutputStream zipStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipStream)) {
            for (int i = 0; i < invoices.size(); i++) {
                InvoicesDto invoice = invoices.get(i);
                @SuppressWarnings("unused") ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

                final int index = i;
                executorService.execute(() -> {
                    try {
                        byte[] pdfContent = generatePDF(invoice);
                        synchronized (zipOutputStream) {
                            zipOutputStream.putNextEntry(new ZipEntry("invoice_" + (index + 1) + ".pdf"));
                            zipOutputStream.write(pdfContent);
                            zipOutputStream.closeEntry();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
        return zipStream.toByteArray();
    }

    private byte[] generatePDF(InvoicesDto invoice) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PdfWriter writer = new PdfWriter(outputStream); PdfDocument pdfDocument = new PdfDocument(writer); Document document = new Document(pdfDocument)) {
            addTitleText(document, "Dinamic ttitle invoice 2024");

            addInvoiceTable(document, invoice);
        }
        return outputStream.toByteArray();
    }

    private void addTitleText(Document document, String titleText) {
        Paragraph paragraph = new Paragraph(titleText).setBold().setFontSize(20).setMarginTop(20);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        document.add(paragraph);
    }

    private void addInvoiceTable(Document document, InvoicesDto invoice) {
        Table table = new Table(2);
        table.addCell("Invoice Number");

        table.addCell(invoice.number());
        table.addCell("Client Name");
        table.addCell(invoice.typOfPayment().toString());
        table.addCell("Date");
        table.addCell(invoice.dateOfIssue().toString());
        table.addCell("Details");
        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List();
        // invoice..forEach(InvoiceItems::getInvoices);
        table.addCell(list);
        document.add(table);
    }
}

