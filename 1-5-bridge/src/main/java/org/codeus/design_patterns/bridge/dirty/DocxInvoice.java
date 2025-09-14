package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DOCX Invoice - Tightly coupled implementation
 * Combines DOCX formatting logic with Invoice business logic
 */
public class DocxInvoice {
    private String invoiceNumber;
    private List<String> items;
    private String company;
    private String timestamp;

    public DocxInvoice(String invoiceNumber, List<String> items, String company) {
        this.invoiceNumber = invoiceNumber;
        this.items = items;
        this.company = company;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder docx = new StringBuilder();
        
        // DOCX-specific formatting (duplicated from DocxReport)
        docx.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        docx.append("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n");
        docx.append("<w:body>\n");
        
        // Invoice-specific content (duplicated in PdfInvoice and HtmlInvoice)
        docx.append("<w:p><w:r><w:t>INVOICE DOCUMENT</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>=================</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Invoice #: ").append(invoiceNumber).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Company: ").append(company).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Generated: ").append(timestamp).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Type: Commercial Invoice</w:t></w:r></w:p>\n");
        
        docx.append("<w:p><w:r><w:t>INVOICE ITEMS:</w:t></w:r></w:p>\n");
        for (String item : items) {
            docx.append("<w:p><w:r><w:t>• ").append(item).append("</w:t></w:r></w:p>\n");
        }
        
        docx.append("</w:body>\n</w:document>");
        
        return "[DOCX] " + docx.toString().replace("\n", " ");
    }
}
