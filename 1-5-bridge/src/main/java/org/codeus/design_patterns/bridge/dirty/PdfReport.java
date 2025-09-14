package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF Report - Tightly coupled implementation
 * Combines PDF formatting logic with Report business logic
 */
public class PdfReport {
    private String title;
    private List<String> data;
    private String timestamp;

    public PdfReport(String title, List<String> data) {
        this.title = title;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder pdf = new StringBuilder();
        
        // PDF-specific formatting (duplicated in PdfInvoice and PdfContract)
        pdf.append("%%PDF-1.4\n");
        pdf.append("1 0 obj\n");
        pdf.append("<<\n/Type /Catalog\n/Pages 2 0 R\n>>\n");
        pdf.append("endobj\n\n");
        
        // Report-specific content (duplicated in DocxReport and HtmlReport)
        pdf.append("REPORT DOCUMENT\n");
        pdf.append("================\n");
        pdf.append("Title: ").append(title).append("\n");
        pdf.append("Generated: ").append(timestamp).append("\n");
        pdf.append("Type: Financial Report\n\n");
        
        pdf.append("REPORT DATA:\n");
        for (String item : data) {
            pdf.append("• ").append(item).append("\n");
        }
        
        // More PDF formatting
        pdf.append("\n%%EOF");
        
        return "[PDF] " + pdf.toString().replace("\n", " | ");
    }
}
