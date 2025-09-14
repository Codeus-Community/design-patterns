package org.codeus.design_patterns.bridge.clean;

import java.util.List;

/**
 * Refined Abstraction for Invoice documents.
 * Implements invoice-specific business logic while delegating format operations
 * to the ExportFormat implementor through the bridge.
 */
public class Invoice extends Document {
    
    private String invoiceNumber;
    private List<String> items;
    private String company;
    
    /**
     * Creates a new Invoice with the specified export format
     * @param exportFormat the format to use for exporting this invoice
     * @param invoiceNumber the invoice number (used as title)
     * @param items the invoice line items
     * @param company the company name
     */
    public Invoice(ExportFormat exportFormat, String invoiceNumber, List<String> items, String company) {
        super(exportFormat);
        this.invoiceNumber = invoiceNumber;
        this.items = items;
        this.company = company;
    }
    
    @Override
    protected void addMetadata(StringBuilder document) {
        // Add invoice-specific metadata
        addMetadataField(document, "Invoice #", invoiceNumber);
        addMetadataField(document, "Company", company);
    }
    
    @Override
    protected void addContent(StringBuilder document) {
        addDataItems(document, items);
    }
    
    @Override
    protected String getDocumentType() {
        return "INVOICE DOCUMENT";
    }
    
    @Override
    protected String getTitle() {
        return invoiceNumber; // Using invoice number as title
    }
    
    @Override
    protected String getSubType() {
        return "Commercial Invoice";
    }
    
    @Override
    protected String getDataSectionTitle() {
        return "INVOICE ITEMS:";
    }
}
