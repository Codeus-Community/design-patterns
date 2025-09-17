package org.codeus.design_patterns.state.clean;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Workflow engine for marketing content with compliance checks
 */
public class MarketingContentEngine extends WorkflowEngine {

    @Override
    public String getDocumentType() {
        return "MARKETING_CONTENT";
    }

    @Override
    public void configureWorkflow(DocumentContext context) {
        context.getReviewers().clear();
        context.getApprovers().clear();

        context.getReviewers().addAll(Arrays.asList("marketing_specialist", "brand_manager"));
        context.getApprovers().add("marketing_manager");

        // Legal content needs legal review
        if (contentContains(context.getContent(), "legal", "disclaimer", "regulatory")) {
            context.getReviewers().add("legal_reviewer_1");
        }

        // Financial/investment content needs compliance
        if (contentContains(context.getContent(), "financial", "investment")) {
            context.getReviewers().add("compliance_officer");
            context.getApprovers().add("compliance_manager");
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

        // Marketing compliance - no absolute claims
        return !contentContains(content, "guaranteed", "risk-free", "100% safe");
    }

    @Override
    public boolean reviewsPassApprovalThreshold(DocumentContext context) {
        // Marketing content needs simple majority
        int positiveReviews = 0;
        int negativeReviews = 0;

        for (String comment : context.getReviews().values()) {
            if (isPositiveReview(comment)) {
                positiveReviews++;
            } else if (isNegativeReview(comment)) {
                negativeReviews++;
            } else {
                positiveReviews++; // Neutral counts as positive
            }
        }

        return positiveReviews > negativeReviews;
    }

    @Override
    public int getMaxRevisions() {
        return 3;
    }

    @Override
    public boolean prePublicationValidation(DocumentContext context) {
        return context.getContent().length() > 100;
    }

    @Override
    public boolean shouldAutoPublish() {
        return true; // Marketing content auto-publishes
    }

    @Override
    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusYears(2);
    }
}