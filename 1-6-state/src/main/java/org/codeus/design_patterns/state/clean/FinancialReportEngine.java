package org.codeus.design_patterns.state.clean;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Workflow engine for financial reports with regulatory compliance
 */
public class FinancialReportEngine extends WorkflowEngine {

    @Override
    public String getDocumentType() {
        return "FINANCIAL_REPORT";
    }

    @Override
    public void configureWorkflow(DocumentContext context) {
        context.getReviewers().clear();
        context.getApprovers().clear();

        if (context.isUrgent()) {
            // Urgent reports have streamlined review
            context.getReviewers().add("senior_financial_analyst");
        } else {
            context.getReviewers().addAll(Arrays.asList("financial_analyst", "accounting_specialist"));
        }

        context.getApprovers().add("finance_manager");

        // Regulatory content needs compliance review and C-level approval
        if (contentContains(context.getContent(), "audit", "sox", "regulatory")) {
            context.getReviewers().add("compliance_officer");
            context.getApprovers().add("cfo");
        }

        context.setExpiryDate(calculateExpiryDate());

        context.addToHistory("Workflow configured: " + context.getReviewers().size() +
                " reviewers, " + context.getApprovers().size() + " approvers");
    }

    @Override
    public boolean validateContent(DocumentContext context) {
        String content = context.getContent();
        if (content == null || content.trim().length() < 100) {
            return false;
        }

        if (!contentContains(content, "financial", "period")) {
            return false;
        }

        // Forward-looking statements need disclaimers
        return !contentContains(content, "forward-looking") ||
                contentContains(content, "disclaimer");
    }

    @Override
    public boolean reviewsPassApprovalThreshold(DocumentContext context) {
        // Financial reports need at least 75% positive reviews
        int positiveReviews = 0;

        for (String comment : context.getReviews().values()) {
            if (isPositiveReview(comment) || (!isNegativeReview(comment))) {
                positiveReviews++;
            }
        }

        return positiveReviews >= Math.ceil(context.getReviewers().size() * 0.75);
    }

    @Override
    public int getMaxRevisions() {
        return 3;
    }

    @Override
    public boolean prePublicationValidation(DocumentContext context) {
        return !context.getReviews().isEmpty() && !context.getApprovals().isEmpty();
    }

    @Override
    public boolean shouldAutoPublish() {
        return false; // Financial reports require manual publishing
    }

    @Override
    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusYears(7); // SOX compliance
    }
}
