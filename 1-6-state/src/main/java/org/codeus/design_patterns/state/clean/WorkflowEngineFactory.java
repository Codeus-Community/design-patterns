package org.codeus.design_patterns.state.clean;

/**
 * Factory for creating appropriate workflow engines based on document type
 */
public class WorkflowEngineFactory {

    public static WorkflowEngine createEngine(String documentType) {
        return switch (documentType) {
            case "LEGAL_CONTRACT" -> new LegalContractEngine();
            case "HR_POLICY" -> new HRPolicyEngine();
            case "FINANCIAL_REPORT" -> new FinancialReportEngine();
            case "MARKETING_CONTENT" -> new MarketingContentEngine();
            default -> throw new IllegalArgumentException("Unknown document type: " + documentType);
        };
    }
}