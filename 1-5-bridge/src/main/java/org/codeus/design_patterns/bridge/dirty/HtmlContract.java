package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * HTML Contract - Tightly coupled implementation
 * Combines HTML formatting logic with Contract business logic
 */
public class HtmlContract {
    private String contractName;
    private List<String> terms;
    private String department;
    private String timestamp;

    public HtmlContract(String contractName, List<String> terms, String department) {
        this.contractName = contractName;
        this.terms = terms;
        this.department = department;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder html = new StringBuilder();
        
        // HTML-specific formatting (duplicated from HtmlReport and HtmlInvoice)
        html.append("<!DOCTYPE html>\n");
        html.append("<html><head><title>Document</title></head><body>\n");
        
        // Contract-specific content (duplicated in PdfContract and DocxContract)
        html.append("<h1>CONTRACT DOCUMENT</h1>\n");
        html.append("<hr>\n");
        html.append("<p><strong>Contract Title:</strong> ").append(contractName).append("</p>\n");
        html.append("<p><strong>Department:</strong> ").append(department).append("</p>\n");
        html.append("<p><strong>Generated:</strong> ").append(timestamp).append("</p>\n");
        html.append("<p><strong>Type:</strong> Legal Contract</p>\n");
        
        html.append("<h2>CONTRACT TERMS:</h2>\n<ul>\n");
        for (String term : terms) {
            html.append("<li>").append(term).append("</li>\n");
        }
        html.append("</ul>\n");
        
        html.append("</body></html>");
        
        return "[HTML] " + html.toString().replace("\n", " ");
    }
}
