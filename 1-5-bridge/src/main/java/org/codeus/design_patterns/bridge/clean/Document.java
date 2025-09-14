package org.codeus.design_patterns.bridge.clean;

import org.apache.commons.lang3.NotImplementedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        throw new NotImplementedException();
    }
}
