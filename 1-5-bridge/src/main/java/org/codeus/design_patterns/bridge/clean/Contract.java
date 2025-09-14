package org.codeus.design_patterns.bridge.clean;

import java.util.List;

/**
 * Refined Abstraction for Contract documents.
 * Implements contract-specific business logic while delegating format operations
 * to the ExportFormat implementor through the bridge.
 */
public class Contract extends Document {
    
    private String contractTitle;
    private List<String> terms;
    private String department;
    
    /**
     * Creates a new Contract with the specified export format
     * @param exportFormat the format to use for exporting this contract
     * @param contractTitle the contract title
     * @param terms the contract terms and conditions
     * @param department the responsible department
     */
    public Contract(ExportFormat exportFormat, String contractTitle, List<String> terms, String department) {
        super(exportFormat);
        this.contractTitle = contractTitle;
        this.terms = terms;
        this.department = department;
    }
    
    @Override
    protected void addMetadata(StringBuilder document) {
        // Add contract-specific metadata
        addMetadataField(document, "Contract Title", contractTitle);
        addMetadataField(document, "Department", department);
    }
    
    @Override
    protected void addContent(StringBuilder document) {
        addDataItems(document, terms);
    }
    
    @Override
    protected String getDocumentType() {
        return "CONTRACT DOCUMENT";
    }
    
    @Override
    protected String getTitle() {
        return contractTitle;
    }
    
    @Override
    protected String getSubType() {
        return "Legal Contract";
    }
    
    @Override
    protected String getDataSectionTitle() {
        return "CONTRACT TERMS:";
    }
}
