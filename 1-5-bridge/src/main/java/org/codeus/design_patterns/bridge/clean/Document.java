package org.codeus.design_patterns.bridge.clean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Abstraction in the Bridge pattern.
 * Defines the interface for document operations and maintains a reference
 * to an ExportFormat implementor. This allows documents to be independent
 * of their export format implementation.
 */
public abstract class Document {
    
    protected ExportFormat exportFormat;
    protected String timestamp;
    
    /**
     * Constructor that sets up the bridge to the export format
     * @param exportFormat the format implementation to use for this document
     */
    public Document(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * Template method that defines the structure of document generation.
     * Uses the Bridge pattern to delegate format-specific operations to the implementor.
     * @return the complete formatted document
     */
    public final String generate() {
        StringBuilder document = new StringBuilder();
        
        // Use the bridge to delegate format operations
        document.append(exportFormat.startDocument());
        document.append(exportFormat.formatHeader(getDocumentType(), getTitle(), timestamp, getSubType()));
        
        // Add document-specific metadata
        addMetadata(document);
        
        // Add the main content
        document.append(exportFormat.formatSectionHeader(getDataSectionTitle()));
        addContent(document);
        
        document.append(exportFormat.endDocument());
        
        // Return with format prefix for compatibility with existing tests
        return exportFormat.getFormatPrefix() + " " + document.toString().replace("\n", " ");
    }
    
    /**
     * Adds document-specific metadata fields
     * @param document the document builder to append to
     */
    protected abstract void addMetadata(StringBuilder document);
    
    /**
     * Adds the main document content
     * @param document the document builder to append to
     */
    protected abstract void addContent(StringBuilder document);
    
    /**
     * Gets the document type identifier
     * @return document type string (e.g., "REPORT DOCUMENT")
     */
    protected abstract String getDocumentType();
    
    /**
     * Gets the document title
     * @return the document title
     */
    protected abstract String getTitle();
    
    /**
     * Gets the document subtype
     * @return document subtype (e.g., "Financial Report")
     */
    protected abstract String getSubType();
    
    /**
     * Gets the title for the data section
     * @return data section title (e.g., "REPORT DATA:")
     */
    protected abstract String getDataSectionTitle();
    
    /**
     * Helper method to add metadata using the export format
     * @param document the document builder
     * @param fieldName the metadata field name
     * @param fieldValue the metadata field value
     */
    protected final void addMetadataField(StringBuilder document, String fieldName, String fieldValue) {
        document.append(exportFormat.formatMetadata(fieldName, fieldValue));
    }
    
    /**
     * Helper method to add data items using the export format
     * @param document the document builder
     * @param items the list of data items to add
     */
    protected final void addDataItems(StringBuilder document, List<String> items) {
        for (String item : items) {
            document.append(exportFormat.formatDataItem(item));
        }
        // Close the section using the format-specific method
        document.append(exportFormat.closeSectionContent());
    }
}
