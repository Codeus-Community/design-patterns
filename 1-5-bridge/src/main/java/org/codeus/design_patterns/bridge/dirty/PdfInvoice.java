package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF Invoice - Tightly coupled implementation
 * Combines PDF formatting logic with Invoice business logic
 */
public class PdfInvoice {
    private String invoiceNumber;
    private List<String> items;
    private String company;
    private String timestamp;

    public PdfInvoice(String invoiceNumber, List<String> items, String company) {
        this.invoiceNumber = invoiceNumber;
        this.items = items;
        this.company = company;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder pdf = new StringBuilder();
        
        // PDF-specific formatting (duplicated from PdfReport)
        pdf.append("%%PDF-1.4\n");
        pdf.append("1 0 obj\n");
        pdf.append("<<\n/Type /Catalog\n/Pages 2 0 R\n>>\n");
        pdf.append("endobj\n\n");
        
        // Invoice-specific content (duplicated in DocxInvoice and HtmlInvoice)
        pdf.append("INVOICE DOCUMENT\n");
        pdf.append("=================\n");
        pdf.append("Invoice #: ").append(invoiceNumber).append("\n");
        pdf.append("Company: ").append(company).append("\n");
        pdf.append("Generated: ").append(timestamp).append("\n");
        pdf.append("Type: Commercial Invoice\n\n");
        
        pdf.append("INVOICE ITEMS:\n");
        for (String item : items) {
            pdf.append("• ").append(item).append("\n");
        }
        
        pdf.append("\n%%EOF");
        
        return "[PDF] " + pdf.toString().replace("\n", " | ");
    }
}
