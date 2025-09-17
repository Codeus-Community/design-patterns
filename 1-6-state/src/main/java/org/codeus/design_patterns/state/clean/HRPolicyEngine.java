package org.codeus.design_patterns.state.clean;

import java.time.LocalDateTime;

/**
 * Workflow engine for HR policies with content-driven approval requirements
 */
public class HRPolicyEngine extends WorkflowEngine {

    @Override
    public String getDocumentType() {
        return "HR_POLICY";
    }

    @Override
    public void configureWorkflow(DocumentContext context) {
        context.getReviewers().clear();
        context.getApprovers().clear();

        context.getReviewers().add("hr_specialist");

        // Salary/compensation content needs specialist review
        if (contentContains(context.getContent(), "salary", "compensation")) {
            context.getReviewers().add("compensation_specialist");
            context.getApprovers().add("hr_director");
        } else {
            context.getApprovers().add("hr_manager");
        }

        // Legal content needs legal review and approval
        if (contentContains(context.getContent(), "legal", "compliance")) {
            context.getReviewers().add("legal_reviewer_1");
            context.getApprovers().add("legal_manager");
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

        if (!contentContains(content, "policy", "effective date")) {
            return false;
        }

        // Discrimination content must include equal opportunity
        return !contentContains(content, "discrimination") ||
                contentContains(content, "equal opportunity");
    }

    @Override
    public boolean reviewsPassApprovalThreshold(DocumentContext context) {
        // HR policies need simple majority
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
        return 4;
    }

    @Override
    public boolean prePublicationValidation(DocumentContext context) {
        return contentContains(context.getContent(), "effective date");
    }

    @Override
    public boolean shouldAutoPublish() {
        return true; // HR policies auto-publish
    }

    @Override
    public LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusYears(3);
    }
}