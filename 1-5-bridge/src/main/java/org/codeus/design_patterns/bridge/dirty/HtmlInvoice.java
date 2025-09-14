package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * HTML Invoice - Tightly coupled implementation
 * Combines HTML formatting logic with Invoice business logic
 */
public class HtmlInvoice {
    private String invoiceNumber;
    private List<String> items;
    private String company;
    private String timestamp;

    public HtmlInvoice(String invoiceNumber, List<String> items, String company) {
        this.invoiceNumber = invoiceNumber;
        this.items = items;
        this.company = company;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder html = new StringBuilder();
        
        // HTML-specific formatting (duplicated from HtmlReport)
        html.append("<!DOCTYPE html>\n");
        html.append("<html><head><title>Document</title></head><body>\n");
        
        // Invoice-specific content (duplicated in PdfInvoice and DocxInvoice)
        html.append("<h1>INVOICE DOCUMENT</h1>\n");
        html.append("<hr>\n");
        html.append("<p><strong>Invoice #:</strong> ").append(invoiceNumber).append("</p>\n");
        html.append("<p><strong>Company:</strong> ").append(company).append("</p>\n");
        html.append("<p><strong>Generated:</strong> ").append(timestamp).append("</p>\n");
        html.append("<p><strong>Type:</strong> Commercial Invoice</p>\n");
        
        html.append("<h2>INVOICE ITEMS:</h2>\n<ul>\n");
        for (String item : items) {
            html.append("<li>").append(item).append("</li>\n");
        }
        html.append("</ul>\n");
        
        html.append("</body></html>");
        
        return "[HTML] " + html.toString().replace("\n", " ");
    }
}
