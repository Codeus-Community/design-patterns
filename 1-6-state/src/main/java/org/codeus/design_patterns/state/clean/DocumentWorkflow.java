package org.codeus.design_patterns.state.clean;

import org.codeus.design_patterns.state.WorkflowContract;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Clean public API that delegates to the State Pattern implementation
 * Maintains backward compatibility while providing clean architecture
 */

public class DocumentWorkflow implements WorkflowContract {

    private final DocumentContext context;

    public DocumentWorkflow(String documentId, String documentType, String content,
                            String authorId, boolean isUrgent) {
        this.context = new DocumentContext(documentId, documentType, content, authorId, isUrgent);
    }

    public boolean submitForReview() {
        DocumentContext result = context.submitForReview();
        return !wasError(result);
    }

    public boolean addReview(String reviewerId, String reviewComment, boolean approved) {
        DocumentContext result = context.addReview(reviewerId, reviewComment, approved);
        return !wasError(result);
    }

    public boolean addApproval(String approverId, boolean approved, String reason) {
        DocumentContext result = context.addApproval(approverId, approved, reason);
        return !wasError(result);
    }

    public boolean publish() {
        DocumentContext result = context.publish();
        return !wasError(result);
    }

    public boolean updateContent(String newContent, String updatedBy) {
        DocumentContext result = context.updateContent(newContent, updatedBy);
        return !wasError(result);
    }

    public boolean archive(String reason) {
        DocumentContext result = context.archive(reason);
        return !wasError(result);
    }

    public void incrementViews() {
        context.incrementViews();
    }

    private boolean wasError(DocumentContext result) {
        List<String> history = result.getActionHistory();
        if (!history.isEmpty()) {
            String lastEntry = history.get(history.size() - 1);
            return lastEntry.contains("ERROR:");
        }
        return false;
    }

    public String getStatusName() {
        return context.getStatusName();
    }

    public Map<String, Object> getStatusReport() {
        return context.getStatusReport();
    }

    public String getDocumentId() { return context.getDocumentId(); }
    public String getDocumentType() { return context.getWorkflowEngine().getDocumentType(); }
    public int getCurrentStatus() {
        return switch (context.getCurrentState()) {
            case DRAFT -> 1;
            case UNDER_REVIEW -> 2;
            case NEEDS_REVISION -> 3;
            case APPROVED -> 4;
            case REJECTED -> 5;
            case PUBLISHED -> 6;
            case ARCHIVED -> 7;
            default -> 0;
        };
    }
    public String getContent() { return context.getContent(); }
    public String getAuthorId() { return context.getAuthorId(); }
    public LocalDateTime getCreatedAt() { return context.getCreatedAt(); }
    public LocalDateTime getLastModified() { return context.getLastModified(); }
    public List<String> getReviewers() { return context.getReviewers(); }
    public List<String> getApprovers() { return context.getApprovers(); }
    public Map<String, String> getReviews() { return context.getReviews(); }
    public Map<String, Boolean> getApprovals() { return context.getApprovals(); }
    public int getRevisionCount() { return context.getRevisionCount(); }
    public LocalDateTime getPublishedAt() { return context.getPublishedAt(); }
    public LocalDateTime getExpiryDate() { return context.getExpiryDate(); }
    public List<String> getActionHistory() { return context.getActionHistory(); }
    public boolean isUrgent() { return context.isUrgent(); }
    public int getViews() { return context.getViewCount(); }
}