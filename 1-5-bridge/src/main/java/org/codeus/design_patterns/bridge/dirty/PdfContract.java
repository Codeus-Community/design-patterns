package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * PDF Contract - Tightly coupled implementation
 * Combines PDF formatting logic with Contract business logic
 */
public class PdfContract {
    private String contractName;
    private List<String> terms;
    private String department;
    private String timestamp;

    public PdfContract(String contractName, List<String> terms, String department) {
        this.contractName = contractName;
        this.terms = terms;
        this.department = department;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder pdf = new StringBuilder();
        
        // PDF-specific formatting (duplicated from PdfReport and PdfInvoice)
        pdf.append("%%PDF-1.4\n");
        pdf.append("1 0 obj\n");
        pdf.append("<<\n/Type /Catalog\n/Pages 2 0 R\n>>\n");
        pdf.append("endobj\n\n");
        
        // Contract-specific content (duplicated in DocxContract and HtmlContract)
        pdf.append("CONTRACT DOCUMENT\n");
        pdf.append("==================\n");
        pdf.append("Contract: ").append(contractName).append("\n");
        pdf.append("Department: ").append(department).append("\n");
        pdf.append("Generated: ").append(timestamp).append("\n");
        pdf.append("Type: Legal Contract\n\n");
        
        pdf.append("CONTRACT TERMS:\n");
        for (String term : terms) {
            pdf.append("• ").append(term).append("\n");
        }
        
        pdf.append("\n%%EOF");
        
        return "[PDF] " + pdf.toString().replace("\n", " | ");
    }
}
