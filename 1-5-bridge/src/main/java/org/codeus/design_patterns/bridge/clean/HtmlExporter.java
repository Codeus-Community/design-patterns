package org.codeus.design_patterns.bridge.clean;

/**
 * Concrete Implementor for HTML export format.
 * Handles all HTML-specific formatting logic independently of document types.
 */
public class HtmlExporter implements ExportFormat {
    
    @Override
    public String startDocument() {
        return "<!DOCTYPE html>\n" +
               "<html><head><title>Document</title></head><body>\n";
    }
    
    @Override
    public String endDocument() {
        return "</body></html>";
    }
    
    @Override
    public String formatHeader(String documentType, String title, String timestamp, String subType) {
        return "<h1>" + documentType + "</h1>\n" +
               "<hr>\n" +
               "<p><strong>Title:</strong> " + title + "</p>\n" +
               "<p><strong>Generated:</strong> " + timestamp + "</p>\n" +
               "<p><strong>Type:</strong> " + subType + "</p>\n";
    }
    
    @Override
    public String formatMetadata(String fieldName, String fieldValue) {
        return "<p><strong>" + fieldName + ":</strong> " + fieldValue + "</p>\n";
    }
    
    @Override
    public String formatSectionHeader(String sectionTitle) {
        return "<h2>" + sectionTitle + "</h2>\n<ul>\n";
    }
    
    @Override
    public String formatDataItem(String item) {
        return "<li>" + item + "</li>\n";
    }
    
    @Override
    public String closeSectionContent() {
        return "</ul>\n"; // HTML needs to close the list
    }
    
    @Override
    public String getFormatPrefix() {
        return "[HTML]";
    }
}