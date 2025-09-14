package org.codeus.design_patterns.bridge.clean;

/**
 * Implementor interface in the Bridge pattern.
 * Defines the interface for concrete export format implementations.
 * This allows the abstraction (Document) to be independent of the implementation details.
 */
public interface ExportFormat {
    
    /**
     * Starts the document with format-specific headers and structure
     * @return the opening part of the document in the specific format
     */
    String startDocument();
    
    /**
     * Ends the document with format-specific footers and closing tags
     * @return the closing part of the document in the specific format
     */
    String endDocument();
    
    /**
     * Formats a document header with title and metadata
     * @param documentType the type of document (e.g., "REPORT DOCUMENT")
     * @param title the document title
     * @param timestamp when the document was generated
     * @param subType additional type information (e.g., "Financial Report")
     * @return formatted header section
     */
    String formatHeader(String documentType, String title, String timestamp, String subType);
    
    /**
     * Formats document-specific metadata fields
     * @param fieldName the name of the metadata field
     * @param fieldValue the value of the metadata field
     * @return formatted metadata field
     */
    String formatMetadata(String fieldName, String fieldValue);
    
    /**
     * Formats a section header for data content
     * @param sectionTitle the title of the data section
     * @return formatted section header
     */
    String formatSectionHeader(String sectionTitle);
    
    /**
     * Formats a single data item in a list
     * @param item the data item to format
     * @return formatted data item
     */
    String formatDataItem(String item);
    
    /**
     * Closes a data section (e.g., closes HTML list tags)
     * @return closing markup for the data section
     */
    String closeSectionContent();
    
    /**
     * Gets the format prefix for identification
     * @return format identifier (e.g., "[PDF]", "[DOCX]", "[HTML]")
     */
    String getFormatPrefix();
}
