package org.lukasz.faktury.utils.pdfenerator;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.invoices.dto.InvoicesDto;
import org.lukasz.faktury.invoices.dto.InvoicesPdf;
import org.lukasz.faktury.invoices.dto.TotalValues;
import org.lukasz.faktury.items.dto.InvoiceItemsDto;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerService;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class PDFGeneratorImpl implements PDFGenerator {
    private final SellerService sellerService;


    public PDFGeneratorImpl(SellerService sellerService) {
        this.sellerService = sellerService;

    }


    @Override
    public byte[] generatePDF(InvoicesPdf invoice,String email) throws IOException {
        PdfFont font = PdfFontFactory.createFont("fonts/DejaVuSans.ttf", PdfEncodings.IDENTITY_H);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream); PdfDocument pdfDocument = new PdfDocument(writer); Document document = new Document(pdfDocument)) {
            document.setFont(font);

            Seller seller = sellerService.findByEmail(email);
            addInvoiceHeader(document, invoice.invoicesDto());
            addSellerBuyer(document, seller, invoice.buyerDto(), invoice.invoicesDto().typOfPayment());
            addInvoiceItems(document, invoice.invoiceItemsDto());
            addInvoiceSummary(document, invoice.totalValues());
        }
        return outputStream.toByteArray();
    }


    private void addInvoiceHeader(Document document, InvoicesDto invoicesDto) {

        Paragraph invoiceNumber = new Paragraph(String.format("Faktura numer: %s ", invoicesDto.number())).setFontSize(14).setBold();
        document.add(invoiceNumber);

        // Daty i płatność
        Paragraph dates = new Paragraph().add(String.format("Data wystawienia:  %s%s Data sprzedaży: %s%s Termin płatności: %s%s Płatność: %s   ", invoicesDto.dateOfIssue(), "\n", invoicesDto.dateOfSale(), "\n", invoicesDto.paymentDate(), "\n", invoicesDto.typOfPayment()));
        document.add(dates);

        document.add(new Paragraph("\n"));
    }

    private void addSellerBuyer(Document document, Seller seller, BuyerDto buyerDto, String typeOfPayment) {
        Table table = new Table(2);
        table.setWidth(UnitValue.createPercentValue(100));

        // Sprzedawca
        Cell sellerCell = new Cell().add(new Paragraph(String.format("Sprzedawca %s %s %s %s %s %s %s %s %s NIP %s %s", "\n", seller.getName(), "\n", seller.getStreet(), seller.getHouseNumber(), "\n", seller.getZipCode(), seller.getCity(), "\n", seller.getNip(),"\n")).setBorder(Border.NO_BORDER));
        if (!typeOfPayment.equals("Gotówka")) {
            sellerCell.add(new Paragraph(String.format("Nr konta %s ", seller.getAccountNb())));
        }
        table.addCell(sellerCell);

        // Nabywca
        Cell buyerCell = new Cell().add(new Paragraph(String.format("Nabywca %s %s %s %s %s %s %s %s %s NIP %s ", "\n", buyerDto.name(), "\n", buyerDto.street(), buyerDto.houseNumber(), "\n", buyerDto.zipCode(), buyerDto.city(), "\n", buyerDto.nip())).setBorder(Border.NO_BORDER));
        table.addCell(buyerCell);

        document.add(table);
        document.add(new Paragraph("\n"));
    }

    private void addInvoiceItems(Document document, List<InvoiceItemsDto> items) {
        Table table = new Table(new float[]{1, 8, 2, 2, 2, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("Lp");
        table.addHeaderCell("Nazwa");
        table.addHeaderCell("Ilość");
        table.addHeaderCell("Jednostka");

        table.addHeaderCell("Cena netto");
        table.addHeaderCell("VAT");
        table.addHeaderCell("Cena Brutto");
        table.addHeaderCell("Wartość brutto");

        IntStream.range(0, items.size()).forEach(i -> {
            InvoiceItemsDto item = items.get(i);
            table.addCell(String.valueOf(i + 1));
            table.addCell(item.description());
            table.addCell(String.valueOf(item.quantity()));
            table.addCell(item.unit());
            table.addCell(String.valueOf(item.priceNetto()));
            table.addCell(String.valueOf(item.tax()));
            table.addCell(String.valueOf(item.priceBrutto()));
            table.addCell(String.valueOf(item.totalValue()));
        });

        document.add(table);
    }

    private void addInvoiceSummary(Document document, TotalValues totalValues) {
        Table summaryTable = new Table(2);
        summaryTable.setWidth(UnitValue.createPercentValue(40));
        summaryTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        summaryTable.addCell(new Cell().add(new Paragraph("Wartość netto")).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalValues.totalNetto()))).setBorder(Border.NO_BORDER));

        summaryTable.addCell(new Cell().add(new Paragraph("VAT")).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalValues.totalVat()))).setBorder(Border.NO_BORDER));

        summaryTable.addCell(new Cell().add(new Paragraph("Wartość brutto")).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(totalValues.totalBrutto()))).setBorder(Border.NO_BORDER));

        document.add(summaryTable);
    }
}

