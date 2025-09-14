package org.codeus.design_patterns.bridge.dirty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DOCX Contract - Tightly coupled implementation
 * Combines DOCX formatting logic with Contract business logic
 */
public class DocxContract {
    private String contractName;
    private List<String> terms;
    private String department;
    private String timestamp;

    public DocxContract(String contractName, List<String> terms, String department) {
        this.contractName = contractName;
        this.terms = terms;
        this.department = department;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generate() {
        StringBuilder docx = new StringBuilder();
        
        // DOCX-specific formatting (duplicated from DocxReport and DocxInvoice)
        docx.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        docx.append("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n");
        docx.append("<w:body>\n");
        
        // Contract-specific content (duplicated in PdfContract and HtmlContract)
        docx.append("<w:p><w:r><w:t>CONTRACT DOCUMENT</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>==================</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Contract: ").append(contractName).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Department: ").append(department).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Generated: ").append(timestamp).append("</w:t></w:r></w:p>\n");
        docx.append("<w:p><w:r><w:t>Type: Legal Contract</w:t></w:r></w:p>\n");
        
        docx.append("<w:p><w:r><w:t>CONTRACT TERMS:</w:t></w:r></w:p>\n");
        for (String term : terms) {
            docx.append("<w:p><w:r><w:t>• ").append(term).append("</w:t></w:r></w:p>\n");
        }
        
        docx.append("</w:body>\n</w:document>");
        
        return "[DOCX] " + docx.toString().replace("\n", " ");
    }
}
