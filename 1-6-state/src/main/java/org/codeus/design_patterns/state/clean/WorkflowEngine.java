package org.codeus.design_patterns.state.clean;

import java.time.LocalDateTime;

/**
 * Abstract workflow engine that defines document-type-specific behavior
 * Uses Strategy pattern to encapsulate business rules per document type
 */
public abstract class WorkflowEngine {

    public abstract String getDocumentType();

    public abstract void configureWorkflow(DocumentContext context);

    public abstract boolean validateContent(DocumentContext context);

    public abstract boolean reviewsPassApprovalThreshold(DocumentContext context);

    public abstract int getMaxRevisions();

    public abstract boolean prePublicationValidation(DocumentContext context);

    public abstract boolean shouldAutoPublish();

    public abstract LocalDateTime calculateExpiryDate();

    // Helper method for content analysis
    protected boolean contentContains(String content, String... keywords) {
        if (content == null) return false;
        String lowerContent = content.toLowerCase();
        for (String keyword : keywords) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    protected boolean isPositiveReview(String reviewComment) {
        String comment = reviewComment.toLowerCase();
        return comment.contains("approve") || comment.contains("accept") ||
                comment.contains("good") || comment.contains("ready") ||
                comment.contains("acceptable") || comment.contains("ok");
    }

    protected boolean isNegativeReview(String reviewComment) {
        String comment = reviewComment.toLowerCase();
        return comment.contains("reject") || comment.contains("needs work") ||
                comment.contains("revision") || comment.contains("fix") ||
                comment.contains("unacceptable") || comment.contains("poor");
    }
}