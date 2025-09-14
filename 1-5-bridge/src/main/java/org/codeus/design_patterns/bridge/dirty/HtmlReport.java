package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * HTML Report - Tightly coupled implementation
 * Combines HTML formatting logic with Report business logic
 */
public class HtmlReport {
    private String title;
    private List<String> data;
    private String timestamp;

    public HtmlReport(String title, List<String> data) {
        this.title = title;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder html = new StringBuilder();
        
        // HTML-specific formatting (duplicated in HtmlInvoice and HtmlContract)
        html.append("<!DOCTYPE html>\n");
        html.append("<html><head><title>Document</title></head><body>\n");
        
        // Report-specific content (duplicated in PdfReport and DocxReport)
        html.append("<h1>REPORT DOCUMENT</h1>\n");
        html.append("<hr>\n");
        html.append("<p><strong>Title:</strong> ").append(title).append("</p>\n");
        html.append("<p><strong>Generated:</strong> ").append(timestamp).append("</p>\n");
        html.append("<p><strong>Type:</strong> Financial Report</p>\n");
        
        html.append("<h2>REPORT DATA:</h2>\n<ul>\n");
        for (String item : data) {
            html.append("<li>").append(item).append("</li>\n");
        }
        html.append("</ul>\n");
        
        html.append("</body></html>");
        
        return "[HTML] " + html.toString().replace("\n", " ");
    }
}
