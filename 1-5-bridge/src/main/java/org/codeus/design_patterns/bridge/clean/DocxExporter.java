package org.codeus.design_patterns.bridge.clean;

/**
 * Concrete Implementor for DOCX export format.
 * Handles all DOCX-specific formatting logic independently of document types.
 */
public class DocxExporter implements ExportFormat {
    
    @Override
    public String startDocument() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\">\n" +
               "<w:body>\n";
    }
    
    @Override
    public String endDocument() {
        return "</w:body>\n</w:document>";
    }
    
    @Override
    public String formatHeader(String documentType, String title, String timestamp, String subType) {
        return "<w:p><w:r><w:t>" + documentType + "</w:t></w:r></w:p>\n" +
               "<w:p><w:r><w:t>================</w:t></w:r></w:p>\n" +
               "<w:p><w:r><w:t>Title: " + title + "</w:t></w:r></w:p>\n" +
               "<w:p><w:r><w:t>Generated: " + timestamp + "</w:t></w:r></w:p>\n" +
               "<w:p><w:r><w:t>Type: " + subType + "</w:t></w:r></w:p>\n";
    }
    
    @Override
    public String formatMetadata(String fieldName, String fieldValue) {
        return "<w:p><w:r><w:t>" + fieldName + ": " + fieldValue + "</w:t></w:r></w:p>\n";
    }
    
    @Override
    public String formatSectionHeader(String sectionTitle) {
        return "<w:p><w:r><w:t>" + sectionTitle + "</w:t></w:r></w:p>\n";
    }
    
    @Override
    public String formatDataItem(String item) {
        return "<w:p><w:r><w:t>• " + item + "</w:t></w:r></w:p>\n";
    }
    
    @Override
    public String closeSectionContent() {
        return ""; // DOCX doesn't need closing tags for lists
    }
    
    @Override
    public String getFormatPrefix() {
        return "[DOCX]";
    }
}
