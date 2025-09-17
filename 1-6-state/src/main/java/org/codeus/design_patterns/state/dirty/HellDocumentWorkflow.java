package org.codeus.design_patterns.state.dirty;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * WORKING NIGHTMARE: All business logic works correctly but is unmaintainable
 * Your mission: Transform this into an intelligent State-driven system
 */
@Getter
@Setter
public class HellDocumentWorkflow {
    // Document types
    public static final String LEGAL_CONTRACT = "LEGAL_CONTRACT";
    public static final String HR_POLICY = "HR_POLICY";
    public static final String FINANCIAL_REPORT = "FINANCIAL_REPORT";
    public static final String MARKETING_CONTENT = "MARKETING_CONTENT";

    // Status codes - the magic numbers from hell
    public static final int DRAFT = 1;
    public static final int UNDER_REVIEW = 2;
    public static final int NEEDS_REVISION = 3;
    public static final int APPROVED = 4;
    public static final int REJECTED = 5;
    public static final int PUBLISHED = 6;
    public static final int ARCHIVED = 7;

    private String documentId;
    private String documentType;
    private int currentStatus;
    private String content;
    private String authorId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    private List<String> reviewers;
    private List<String> approvers;
    private Map<String, String> reviews; // reviewer -> review comment
    private Map<String, Boolean> approvals; // approver -> approved/rejected
    private int revisionCount;
    private LocalDateTime publishedAt;
    private LocalDateTime expiryDate;
    private List<String> actionHistory;
    private boolean isUrgent;

    public HellDocumentWorkflow(String documentId, String documentType, String content,
                                String authorId, boolean isUrgent) {
        this.documentId = documentId;
        this.documentType = documentType;
        this.content = content;
        this.authorId = authorId;
        this.isUrgent = isUrgent;
        this.currentStatus = DRAFT;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.reviewers = new ArrayList<>();
        this.approvers = new ArrayList<>();
        this.reviews = new HashMap<>();
        this.approvals = new HashMap<>();
        this.revisionCount = 0;
        this.actionHistory = new ArrayList<>();

        addToHistory("Document created: " + documentType);
        setupWorkflow(); // THE MONSTER METHOD STARTS HERE
    }

    // THE ULTIMATE MONSTER - 100+ lines of nested business logic that WORKS
    private void setupWorkflow() {
        reviewers.clear();
        approvers.clear();

        // Different workflows for different document types
        if (LEGAL_CONTRACT.equals(documentType)) {
            reviewers.add("legal_reviewer_1");
            reviewers.add("legal_reviewer_2");
            if (content != null && content.length() > 10000) {
                reviewers.add("senior_legal_reviewer"); // Complex documents need senior review
            }
            approvers.add("legal_manager");
            if (isUrgent) {
                approvers.add("legal_director"); // Urgent contracts need director approval
            }
            if (content != null && (content.toLowerCase().contains("international") ||
                    content.toLowerCase().contains("cross-border"))) {
                approvers.add("compliance_officer"); // International contracts need compliance
            }

        } else if (HR_POLICY.equals(documentType)) {
            reviewers.add("hr_specialist");
            if (content != null && (content.toLowerCase().contains("salary") ||
                    content.toLowerCase().contains("compensation"))) {
                reviewers.add("compensation_specialist");
                approvers.add("hr_director");
            } else {
                approvers.add("hr_manager");
            }
            if (content != null && (content.toLowerCase().contains("legal") ||
                    content.toLowerCase().contains("compliance"))) {
                reviewers.add("legal_reviewer_1");
                approvers.add("legal_manager");
            }

        } else if (FINANCIAL_REPORT.equals(documentType)) {
            reviewers.add("financial_analyst");
            reviewers.add("accounting_specialist");
            approvers.add("finance_manager");
            if (content != null && (content.toLowerCase().contains("audit") ||
                    content.toLowerCase().contains("sox") ||
                    content.toLowerCase().contains("regulatory"))) {
                reviewers.add("compliance_officer");
                approvers.add("cfo");
            }
            if (isUrgent) {
                // Urgent financial reports bypass some reviews
                reviewers.clear();
                reviewers.add("senior_financial_analyst");
            }

        } else if (MARKETING_CONTENT.equals(documentType)) {
            reviewers.add("marketing_specialist");
            reviewers.add("brand_manager");
            approvers.add("marketing_manager");
            if (content != null && (content.toLowerCase().contains("legal") ||
                    content.toLowerCase().contains("disclaimer") ||
                    content.toLowerCase().contains("regulatory"))) {
                reviewers.add("legal_reviewer_1");
            }
            if (content != null && (content.toLowerCase().contains("financial") ||
                    content.toLowerCase().contains("investment"))) {
                reviewers.add("compliance_officer");
                approvers.add("compliance_manager");
            }
        }

        // Set expiry dates based on document type
        if (LEGAL_CONTRACT.equals(documentType)) {
            expiryDate = LocalDateTime.now().plusYears(5);
        } else if (HR_POLICY.equals(documentType)) {
            expiryDate = LocalDateTime.now().plusYears(3);
        } else if (FINANCIAL_REPORT.equals(documentType)) {
            expiryDate = LocalDateTime.now().plusYears(7);
        } else {
            expiryDate = LocalDateTime.now().plusYears(2);
        }

        addToHistory("Workflow configured: " + reviewers.size() + " reviewers, " +
                approvers.size() + " approvers");
    }

    // ANOTHER MONSTER METHOD - Submit for review with validation
    public boolean submitForReview() {
        lastModified = LocalDateTime.now();

        if (currentStatus != DRAFT && currentStatus != NEEDS_REVISION) {
            addToHistory("ERROR: Cannot submit for review from status: " + getStatusName());
            return false;
        }

        // Validate content based on document type
        if (!validateContent()) {
            addToHistory("ERROR: Content validation failed");
            return false;
        }

        currentStatus = UNDER_REVIEW;
        addToHistory("Document submitted for review");

        // Auto-assign urgent documents
        if (isUrgent) {
            addToHistory("URGENT: Fast-track review process initiated");
            for (String reviewer : reviewers) {
                addToHistory("Auto-assigned to urgent reviewer: " + reviewer);
            }
        }

        return true;
    }

    // VALIDATION
    private boolean validateContent() {
        if (content == null || content.trim().length() < 100) {
            return false;
        }

        if (LEGAL_CONTRACT.equals(documentType)) {
            // Legal contracts must have specific sections
            String lowerContent = content.toLowerCase();
            if (!lowerContent.contains("terms and conditions") ||
                    !lowerContent.contains("liability") ||
                    !lowerContent.contains("governing law")) {
                return false;
            }
            if (content.length() > 50000) {
                addToHistory("WARNING: Contract exceeds recommended length");
            }

        } else if (HR_POLICY.equals(documentType)) {
            String lowerContent = content.toLowerCase();
            if (!lowerContent.contains("policy") ||
                    !lowerContent.contains("effective date")) {
                return false;
            }
            if (lowerContent.contains("discrimination") &&
                    !lowerContent.contains("equal opportunity")) {
                return false; // Must include equal opportunity statement
            }

        } else if (FINANCIAL_REPORT.equals(documentType)) {
            String lowerContent = content.toLowerCase();
            if (!lowerContent.contains("financial")) {
                return false;
            }
            // Financial reports need specific disclaimers
            if (lowerContent.contains("forward-looking") &&
                    !lowerContent.contains("disclaimer")) {
                return false;
            }

        } else if (MARKETING_CONTENT.equals(documentType)) {
            String lowerContent = content.toLowerCase();
            if (lowerContent.contains("guaranteed") ||
                    lowerContent.contains("risk-free") ||
                    lowerContent.contains("100% safe")) {
                return false; // Marketing compliance - no absolute claims
            }
        }

        return true;
    }

    // REVIEW PROCESSING
    public boolean addReview(String reviewerId, String reviewComment, boolean approved) {
        lastModified = LocalDateTime.now();

        if (currentStatus != UNDER_REVIEW) {
            addToHistory("ERROR: Cannot add review in status: " + getStatusName());
            return false;
        }

        if (!reviewers.contains(reviewerId)) {
            addToHistory("ERROR: " + reviewerId + " not authorized to review this document");
            return false;
        }

        if (reviews.containsKey(reviewerId)) {
            addToHistory("WARNING: Overwriting previous review from " + reviewerId);
        }

        reviews.put(reviewerId, reviewComment);
        addToHistory("Review added by " + reviewerId + ": " + (approved ? "APPROVED" : "NEEDS REVISION"));

        // Check if all reviews are complete
        if (reviews.size() >= reviewers.size()) {
            return processAllReviews();
        }

        return true;
    }

    // COMPLEX REVIEW PROCESSING
    private boolean processAllReviews() {
        // Analyze review comments to determine sentiment
        int positiveReviews = 0;
        int negativeReviews = 0;

        for (Map.Entry<String, String> review : reviews.entrySet()) {
            String comment = review.getValue().toLowerCase();
            if (comment.contains("approve") || comment.contains("accept") ||
                    comment.contains("good") || comment.contains("ready") ||
                    comment.contains("acceptable") || comment.contains("ok")) {
                positiveReviews++;
            } else if (comment.contains("reject") || comment.contains("needs work") ||
                    comment.contains("revision") || comment.contains("fix") ||
                    comment.contains("unacceptable") || comment.contains("poor")) {
                negativeReviews++;
            } else {
                // Neutral comments count as positive
                positiveReviews++;
            }
        }

        // Different approval thresholds for different document types
        boolean reviewsPassed = false;
        if (LEGAL_CONTRACT.equals(documentType)) {
            // Legal contracts need ALL reviews to be positive
            reviewsPassed = negativeReviews == 0 && positiveReviews >= reviewers.size();
        } else if (FINANCIAL_REPORT.equals(documentType)) {
            // Financial reports need at least 75% positive reviews
            reviewsPassed = positiveReviews >= Math.ceil(reviewers.size() * 0.75);
        } else {
            // Other documents need simple majority
            reviewsPassed = positiveReviews > negativeReviews;
        }

        if (reviewsPassed) {
            return moveToApproval();
        } else {
            currentStatus = NEEDS_REVISION;
            revisionCount++;
            addToHistory("Reviews complete: NEEDS REVISION (attempt " + revisionCount + ")");

            // Auto-reject after too many revisions
            if (revisionCount >= getMaxRevisions()) {
                currentStatus = REJECTED;
                addToHistory("AUTO-REJECTED: Too many revision attempts");
            }

            return false;
        }
    }

    private int getMaxRevisions() {
        switch (documentType) {
            case LEGAL_CONTRACT:
                return 5;
            case FINANCIAL_REPORT:
                return 3;
            case HR_POLICY:
                return 4;
            case MARKETING_CONTENT:
                return 3;
            default:
                return 3;
        }
    }

    private boolean moveToApproval() {
        if (approvers.isEmpty()) {
            // Some documents auto-approve after review
            return autoApprove();
        }

        currentStatus = APPROVED; // Move to approved state for approval process
        addToHistory("Reviews passed - moving to approval process");

        // Auto-approve urgent marketing content if all reviews were positive
        if (isUrgent && MARKETING_CONTENT.equals(documentType)) {
            return autoApprove();
        }

        return true;
    }

    // APPROVAL NIGHTMARE - but works correctly
    public boolean addApproval(String approverId, boolean approved, String reason) {
        lastModified = LocalDateTime.now();

        if (currentStatus != APPROVED) {
            addToHistory("ERROR: Cannot add approval in status: " + getStatusName());
            return false;
        }

        if (!approvers.contains(approverId)) {
            addToHistory("ERROR: " + approverId + " not authorized to approve this document");
            return false;
        }

        approvals.put(approverId, approved);
        addToHistory("Approval from " + approverId + ": " + (approved ? "APPROVED" : "REJECTED") +
                (reason != null ? " - " + reason : ""));

        if (!approved) {
            currentStatus = REJECTED;
            addToHistory("Document REJECTED by " + approverId);
            return false;
        }

        // Check if all approvals are received
        if (approvals.size() >= approvers.size()) {
            return finalizeApproval();
        }

        return true;
    }

    private boolean finalizeApproval() {
        // All approvers must approve
        for (Boolean approval : approvals.values()) {
            if (!approval) {
                currentStatus = REJECTED;
                addToHistory("Document REJECTED - not all approvers approved");
                return false;
            }
        }

        return autoApprove();
    }

    private boolean autoApprove() {
        currentStatus = APPROVED;
        addToHistory("Document FULLY APPROVED");

        // Auto-publish some document types
        if (MARKETING_CONTENT.equals(documentType) ||
                (HR_POLICY.equals(documentType) && !isUrgent)) {
            return autoPublish();
        }

        return true;
    }

    // PUBLISHING COMPLEXITY
    public boolean publish() {
        lastModified = LocalDateTime.now();

        if (currentStatus == PUBLISHED) {
            addToHistory("Document already published");
            return true;
        }

        if (currentStatus != APPROVED) {
            addToHistory("ERROR: Cannot publish document in status: " + getStatusName());
            return false;
        }

        // Pre-publication validation
        if (!prePublicationCheck()) {
            addToHistory("ERROR: Pre-publication check failed");
            return false;
        }

        currentStatus = PUBLISHED;
        publishedAt = LocalDateTime.now();
        addToHistory("Document PUBLISHED");

        // Set up automatic archival
        scheduleArchival();

        return true;
    }

    private boolean autoPublish() {
        addToHistory("AUTO-PUBLISHING approved document");
        return publish();
    }

    private boolean prePublicationCheck() {
        // Different checks for different document types
        if (LEGAL_CONTRACT.equals(documentType)) {
            // Legal contracts need final compliance check
            String lowerContent = content.toLowerCase();
            return lowerContent.contains("signature") &&
                    lowerContent.contains("date");
        } else if (FINANCIAL_REPORT.equals(documentType)) {
            // Financial reports need audit trail
            return reviews.size() > 0 && approvals.size() > 0;
        } else if (HR_POLICY.equals(documentType)) {
            // HR policies need effective date
            return content.toLowerCase().contains("effective date");
        } else if (MARKETING_CONTENT.equals(documentType)) {
            // Marketing content needs basic compliance
            return content.length() > 100;
        }

        return true;
    }

    private void scheduleArchival() {
        // In real system, this would be scheduled
        addToHistory("Archival scheduled for: " + expiryDate);
    }

    // UPDATE METHODS WITH BUSINESS RULES
    public boolean updateContent(String newContent, String updatedBy) {
        if (currentStatus == PUBLISHED || currentStatus == ARCHIVED) {
            addToHistory("ERROR: Cannot update published/archived document");
            return false;
        }

        if (currentStatus == UNDER_REVIEW || currentStatus == APPROVED) {
            addToHistory("WARNING: Updating document will reset approval process");
            resetToRevision();
        }

        this.content = newContent;
        this.lastModified = LocalDateTime.now();
        addToHistory("Content updated by: " + updatedBy);

        // Re-setup workflow in case content changes affect reviewers/approvers
        setupWorkflow();

        return true;
    }

    private void resetToRevision() {
        currentStatus = NEEDS_REVISION;
        reviews.clear();
        approvals.clear();
        revisionCount++;
        addToHistory("Approval process reset due to content update");
    }

    // ARCHIVE LOGIC
    public boolean archive(String reason) {
        if (currentStatus == ARCHIVED) {
            addToHistory("Document already archived");
            return true;
        }

        if (currentStatus != PUBLISHED) {
            addToHistory("ERROR: Can only archive published documents");
            return false;
        }

        currentStatus = ARCHIVED;
        addToHistory("Document archived: " + reason);
        return true;
    }

    // STATUS REPORTING
    public Map<String, Object> getStatusReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("documentId", documentId);
        report.put("type", documentType);
        report.put("status", getStatusName());
        report.put("author", authorId);
        report.put("created", createdAt);
        report.put("lastModified", lastModified);
        report.put("revisionCount", revisionCount);

        // Status-specific information
        if (currentStatus == UNDER_REVIEW) {
            report.put("pendingReviewers", getPendingReviewers());
            report.put("completedReviews", reviews.size());
            report.put("totalReviewers", reviewers.size());
        } else if (currentStatus == APPROVED) {
            report.put("pendingApprovers", getPendingApprovers());
            report.put("completedApprovals", approvals.size());
            report.put("totalApprovers", approvers.size());
        } else if (currentStatus == PUBLISHED) {
            report.put("publishedAt", publishedAt);
            report.put("expiryDate", expiryDate);
            if (expiryDate != null) {
                long daysUntilExpiry = ChronoUnit.DAYS.between(LocalDateTime.now(), expiryDate);
                report.put("daysUntilExpiry", daysUntilExpiry);
            }
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

    // UTILITY METHODS
    public String getStatusName() {
        switch (currentStatus) {
            case DRAFT:
                return "Draft";
            case UNDER_REVIEW:
                return "Under Review";
            case NEEDS_REVISION:
                return "Needs Revision";
            case APPROVED:
                return "Approved";
            case REJECTED:
                return "Rejected";
            case PUBLISHED:
                return "Published";
            case ARCHIVED:
                return "Archived";
            default:
                return "Unknown";
        }
    }

    private void addToHistory(String event) {
        actionHistory.add(LocalDateTime.now() + ": " + event);
    }
}
