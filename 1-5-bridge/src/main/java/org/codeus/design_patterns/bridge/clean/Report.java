package org.codeus.design_patterns.bridge.clean;

import java.util.List;

/**
 * Refined Abstraction for Report documents.
 * Implements report-specific business logic while delegating format operations
 * to the ExportFormat implementor through the bridge.
 */
public class Report extends Document {
    
    private String title;
    private List<String> data;
    
    /**
     * Creates a new Report with the specified export format
     * @param exportFormat the format to use for exporting this report
     * @param title the report title
     * @param data the report data items
     */
    public Report(ExportFormat exportFormat, String title, List<String> data) {
        super(exportFormat);
        this.title = title;
        this.data = data;
    }
    
    @Override
    protected void addMetadata(StringBuilder document) {
        // Reports don't have additional metadata beyond the standard header
        // This could be extended to add report-specific metadata like:
        // addMetadataField(document, "Report Period", "Q4 2024");
        // addMetadataField(document, "Department", "Finance");
    }
    
    @Override
    protected void addContent(StringBuilder document) {
        addDataItems(document, data);
    }
    
    @Override
    protected String getDocumentType() {
        return "REPORT DOCUMENT";
    }
    
    @Override
    protected String getTitle() {
        return title;
    }
    
    @Override
    protected String getSubType() {
        return "Financial Report";
    }
    
    @Override
    protected String getDataSectionTitle() {
        return "REPORT DATA:";
    }
}