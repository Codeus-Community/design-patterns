package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DOCX Report - Tightly coupled implementation
 * Combines DOCX formatting logic with Report business logic
 */
public class DocxReport {
    private String title;
    private List<String> data;
    private String timestamp;

    public DocxReport(String title, List<String> data) {
        this.title = title;
        this.data = data;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder docx = new StringBuilder();
        
        // DOCX-specific formatting (duplicated in DocxInvoice and DocxContract)
        docx.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        docx.append("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n");
        docx.append("<w:body>\n");
        
        // Report-specific content (duplicated in PdfReport and HtmlReport)
        docx.append("<w:p><w:r><w:t>REPORT DOCUMENT</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>================</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Title: ").append(title).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Generated: ").append(timestamp).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Type: Financial Report</w:t></w:r></w:p>\n");
        
        docx.append("<w:p><w:r><w:t>REPORT DATA:</w:t></w:r></w:p>\n");
        for (String item : data) {
            docx.append("<w:p><w:r><w:t>• ").append(item).append("</w:t></w:r></w:p>\n");
        }
        
        docx.append("</w:body>\n</w:document>");
        
        return "[DOCX] " + docx.toString().replace("\n", " ");
    }
}
