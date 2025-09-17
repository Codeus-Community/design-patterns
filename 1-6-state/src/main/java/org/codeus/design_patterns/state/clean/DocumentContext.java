package org.codeus.design_patterns.state.clean;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Context that holds document data and delegates behavior to current state
 * Acts as a bridge between the client and the state machine
 */
@Getter
@Setter
public class DocumentContext {
    private DocumentState currentState;
    private final WorkflowEngine workflowEngine;

    // Document data
    private final String documentId;
    private final String authorId;
    private final boolean isUrgent;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private LocalDateTime publishedAt;
    private LocalDateTime expiryDate;

    // Workflow data
    private final List<String> reviewers;
    private final List<String> approvers;
    private final Map<String, String> reviews;
    private final Map<String, Boolean> approvals;
    private int revisionCount;
    private int viewCount;
    private final List<String> actionHistory;

    public DocumentContext(String documentId, String documentType, String content,
                           String authorId, boolean isUrgent) {
        this.documentId = documentId;
        this.content = content;
        this.authorId = authorId;
        this.isUrgent = isUrgent;
        this.currentState = DocumentState.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.reviewers = new ArrayList<>();
        this.approvers = new ArrayList<>();
        this.reviews = new HashMap<>();
        this.approvals = new HashMap<>();
        this.revisionCount = 0;
        this.viewCount = 0;
        this.actionHistory = new ArrayList<>();

        this.workflowEngine = WorkflowEngineFactory.createEngine(documentType);

        addToHistory("Document created: " + documentType);
        workflowEngine.configureWorkflow(this);
    }

    public DocumentContext transitionTo(DocumentState newState) {
        addToHistory("State: " + currentState + " → " + newState);
        this.currentState = newState;
        return this;
    }

    public DocumentContext submitForReview() {
        return currentState.submitForReview(this);
    }

    public DocumentContext addReview(String reviewerId, String reviewComment, boolean approved) {
        return currentState.addReview(this, reviewerId, reviewComment, approved);
    }

    public DocumentContext addApproval(String approverId, boolean approved, String reason) {
        return currentState.addApproval(this, approverId, approved, reason);
    }

    public DocumentContext publish() {
        return currentState.publish(this);
    }

    public DocumentContext updateContent(String newContent, String updatedBy) {
        return currentState.updateContent(this, newContent, updatedBy);
    }

    public DocumentContext archive(String reason) {
        return currentState.archive(this, reason);
    }

    public void incrementViews() {
        currentState.incrementViews(this);
    }

    public DocumentContext addError(String error) {
        addToHistory("ERROR: " + error);
        return this;
    }

    public void addToHistory(String event) {
        actionHistory.add(LocalDateTime.now() + ": " + event);
    }

    public void addReview(String reviewerId, String comment) {
        reviews.put(reviewerId, comment);
    }

    public void addApproval(String approverId, boolean approved) {
        approvals.put(approverId, approved);
    }

    public void clearReviews() {
        reviews.clear();
    }

    public void clearApprovals() {
        approvals.clear();
    }

    public void incrementRevisionCount() {
        revisionCount++;
    }

    public void incrementViewCount() {
        viewCount++;
    }

    public void scheduleArchival() {
        addToHistory("Archival scheduled for: " + expiryDate);
    }

    public Map<String, Object> getStatusReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("documentId", documentId);
        report.put("type", workflowEngine.getDocumentType());
        report.put("status", getStatusName());
        report.put("author", authorId);
        report.put("created", createdAt);
        report.put("lastModified", lastModified);
        report.put("revisionCount", revisionCount);

        switch (currentState) {
            case UNDER_REVIEW:
                report.put("pendingReviewers", getPendingReviewers());
                report.put("completedReviews", reviews.size());
                report.put("totalReviewers", reviewers.size());
                break;
            case APPROVED:
                report.put("pendingApprovers", getPendingApprovers());
                report.put("completedApprovals", approvals.size());
                report.put("totalApprovers", approvers.size());
                break;
            case PUBLISHED:
                report.put("publishedAt", publishedAt);
                report.put("expiryDate", expiryDate);
                if (expiryDate != null) {
                    long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS
                            .between(LocalDateTime.now(), expiryDate);
                    report.put("daysUntilExpiry", daysUntilExpiry);
                }
                report.put("views", viewCount);
                break;
        }

        return report;
    }

    private List<String> getPendingReviewers() {
        List<String> pending = new ArrayList<>(reviewers);
        pending.removeAll(reviews.keySet());
        return pending;
    }

    private List<String> getPendingApprovers() {
        List<String> pending = new ArrayList<>(approvers);
        pending.removeAll(approvals.keySet());
        return pending;
    }

    public String getStatusName() {
        return switch (currentState) {
            case DRAFT -> "Draft";
            case UNDER_REVIEW -> "Under Review";
            case NEEDS_REVISION -> "Needs Revision";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
            case PUBLISHED -> "Published";
            case ARCHIVED -> "Archived";
            default -> "Unknown";
        };
    }
}