package org.codeus.design_patterns.state.clean;

import java.time.LocalDateTime;

/**
 * Intelligent document states that know their own behavior
 * Each state encapsulates what actions are valid and how to handle them
 */
public enum DocumentState {
    DRAFT {
        @Override
        public DocumentContext submitForReview(DocumentContext context) {
            if (!context.getWorkflowEngine().validateContent(context)) {
                return context.addError("Content validation failed");
            }

            context.getWorkflowEngine().configureWorkflow(context);
            context.addToHistory("Document submitted for review");

            if (context.isUrgent()) {
                context.addToHistory("URGENT: Fast-track review process initiated");
                context.getReviewers().forEach(reviewer ->
                        context.addToHistory("Auto-assigned to urgent reviewer: " + reviewer));
            }

            return context.transitionTo(UNDER_REVIEW);
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            context.setContent(newContent);
            context.setLastModified(LocalDateTime.now());
            context.addToHistory("Content updated by: " + updatedBy);
            context.getWorkflowEngine().configureWorkflow(context); // Re-configure workflow
            return context;
        }
    },

    UNDER_REVIEW {
        @Override
        public DocumentContext addReview(DocumentContext context, String reviewerId,
                                         String reviewComment, boolean approved) {
            if (!context.getReviewers().contains(reviewerId)) {
                return context.addError(reviewerId + " not authorized to review this document");
            }

            if (context.getReviews().containsKey(reviewerId)) {
                context.addToHistory("WARNING: Overwriting previous review from " + reviewerId);
            }

            context.addReview(reviewerId, reviewComment);
            context.addToHistory("Review added by " + reviewerId + ": " +
                    (approved ? "APPROVED" : "NEEDS REVISION"));

            // Check if all reviews are complete
            if (context.getReviews().size() >= context.getReviewers().size()) {
                return processAllReviews(context);
            }

            return context;
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            context.addToHistory("WARNING: Updating document will reset approval process");
            return resetToRevision(context, newContent, updatedBy);
        }

        private DocumentContext processAllReviews(DocumentContext context) {
            if (context.getWorkflowEngine().reviewsPassApprovalThreshold(context)) {
                return moveToApproval(context);
            } else {
                context.incrementRevisionCount();
                context.addToHistory("Reviews complete: NEEDS REVISION (attempt " +
                        context.getRevisionCount() + ")");

                if (context.getRevisionCount() >= context.getWorkflowEngine().getMaxRevisions()) {
                    context.addToHistory("AUTO-REJECTED: Too many revision attempts");
                    return context.transitionTo(REJECTED);
                }

                return context.transitionTo(NEEDS_REVISION);
            }
        }

        private DocumentContext moveToApproval(DocumentContext context) {
            if (context.getApprovers().isEmpty()) {
                return autoApprove(context);
            }

            context.addToHistory("Reviews passed - moving to approval process");

            // Auto-approve urgent marketing content
            if (context.isUrgent() &&
                    context.getWorkflowEngine() instanceof MarketingContentEngine) {
                return autoApprove(context);
            }

            return context.transitionTo(APPROVED);
        }
    },

    NEEDS_REVISION {
        @Override
        public DocumentContext submitForReview(DocumentContext context) {
            return DRAFT.submitForReview(context);
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            return DRAFT.updateContent(context, newContent, updatedBy);
        }
    },

    APPROVED {
        @Override
        public DocumentContext addApproval(DocumentContext context, String approverId,
                                           boolean approved, String reason) {
            if (!context.getApprovers().contains(approverId)) {
                return context.addError(approverId + " not authorized to approve this document");
            }

            context.addApproval(approverId, approved);
            context.addToHistory("Approval from " + approverId + ": " +
                    (approved ? "APPROVED" : "REJECTED") +
                    (reason != null ? " - " + reason : ""));

            if (!approved) {
                context.addToHistory("Document REJECTED by " + approverId);
                return context.transitionTo(REJECTED);
            }

            // Check if all approvals are received
            if (context.getApprovals().size() >= context.getApprovers().size()) {
                return finalizeApproval(context);
            }

            return context;
        }

        @Override
        public DocumentContext publish(DocumentContext context) {
            if (!context.getWorkflowEngine().prePublicationValidation(context)) {
                return context.addError("Pre-publication check failed");
            }

            context.setPublishedAt(LocalDateTime.now());
            context.addToHistory("Document PUBLISHED");
            context.scheduleArchival();

            return context.transitionTo(PUBLISHED);
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            context.addToHistory("WARNING: Updating document will reset approval process");
            return resetToRevision(context, newContent, updatedBy);
        }

        private DocumentContext finalizeApproval(DocumentContext context) {
            // All approvers must approve
            for (Boolean approval : context.getApprovals().values()) {
                if (!approval) {
                    context.addToHistory("Document REJECTED - not all approvers approved");
                    return context.transitionTo(REJECTED);
                }
            }

            return autoApprove(context);
        }
    },

    REJECTED {
        // Terminal state - no transitions allowed except archive (if published first)
        @Override
        public DocumentContext submitForReview(DocumentContext context) {
            return context.addError("Cannot resubmit rejected document");
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            return context.addError("Cannot update rejected document");
        }
    },

    PUBLISHED {
        @Override
        public DocumentContext archive(DocumentContext context, String reason) {
            context.addToHistory("Document archived: " + reason);
            return context.transitionTo(ARCHIVED);
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            return context.addError("Cannot update published/archived document");
        }

        @Override
        public void incrementViews(DocumentContext context) {
            context.incrementViewCount();
        }
    },

    ARCHIVED {
        @Override
        public DocumentContext archive(DocumentContext context, String reason) {
            context.addToHistory("Document already archived");
            return context; // Idempotent
        }

        @Override
        public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
            return context.addError("Cannot update published/archived document");
        }
    };

    // Default implementations (can be overridden by states)
    public DocumentContext submitForReview(DocumentContext context) {
        return context.addError("Cannot submit for review from state: " + this);
    }

    public DocumentContext addReview(DocumentContext context, String reviewerId,
                                     String reviewComment, boolean approved) {
        return context.addError("Cannot add review in state: " + this);
    }

    public DocumentContext addApproval(DocumentContext context, String approverId,
                                       boolean approved, String reason) {
        return context.addError("Cannot add approval in state: " + this);
    }

    public DocumentContext publish(DocumentContext context) {
        return context.addError("Cannot publish from state: " + this);
    }

    public DocumentContext updateContent(DocumentContext context, String newContent, String updatedBy) {
        return context.addError("Cannot update content in state: " + this);
    }

    public DocumentContext archive(DocumentContext context, String reason) {
        return context.addError("Cannot archive from state: " + this);
    }

    public void incrementViews(DocumentContext context) {
        // Only PUBLISHED state increments views
    }

    // Helper methods shared by states
    protected DocumentContext autoApprove(DocumentContext context) {
        context.addToHistory("Document FULLY APPROVED");

        // Auto-publish some document types
        if (context.getWorkflowEngine().shouldAutoPublish()) {
            context.addToHistory("AUTO-PUBLISHING approved document");
            return APPROVED.publish(context);
        }

        return context;
    }

    protected DocumentContext resetToRevision(DocumentContext context, String newContent, String updatedBy) {
        context.setContent(newContent);
        context.setLastModified(LocalDateTime.now());
        context.clearReviews();
        context.clearApprovals();
        context.incrementRevisionCount();
        context.addToHistory("Content updated by: " + updatedBy);
        context.addToHistory("Approval process reset due to content update");
        context.getWorkflowEngine().configureWorkflow(context);
        return context.transitionTo(NEEDS_REVISION);
    }
}