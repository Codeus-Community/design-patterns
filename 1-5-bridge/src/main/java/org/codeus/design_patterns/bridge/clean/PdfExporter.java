package org.codeus.design_patterns.bridge.clean;

/**
 * Concrete Implementor for PDF export format.
 * Handles all PDF-specific formatting logic independently of document types.
 */
public class PdfExporter implements ExportFormat {

    @Override
    public String startDocument() {
        return "%%PDF-1.4\n" +
                "1 0 obj\n" +
                "<<\n/Type /Catalog\n/Pages 2 0 R\n>>\n" +
                "endobj\n\n";
    }

    @Override
    public String endDocument() {
        return "\n%%EOF";
    }

    @Override
    public String formatHeader(String documentType, String title, String timestamp, String subType) {
        return documentType + "\n" +
                "================\n" +
                "Title: " + title + "\n" +
                "Generated: " + timestamp + "\n" +
                "Type: " + subType + "\n\n";
    }

    @Override
    public String formatMetadata(String fieldName, String fieldValue) {
        return fieldName + ": " + fieldValue + "\n";
    }

    @Override
    public String formatSectionHeader(String sectionTitle) {
        return sectionTitle + "\n";
    }

    @Override
    public String formatDataItem(String item) {
        return "• " + item + "\n";
    }

    @Override
    public String closeSectionContent() {
        return ""; // PDF doesn't need closing tags
    }

    @Override
    public String getFormatPrefix() {
        return "[PDF]";
    }
}