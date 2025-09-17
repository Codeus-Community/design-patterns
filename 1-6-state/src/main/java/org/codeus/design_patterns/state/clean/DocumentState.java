package org.codeus.design_patterns.state.clean;

/**
 * Intelligent document states that know their own behavior
 * Each state encapsulates what actions are valid and how to handle them
 */
public enum DocumentState {
    DRAFT {
        //TO DO
    },

    UNDER_REVIEW {
        //TO DO
    },

    NEEDS_REVISION {
        //TO DO
    },

    APPROVED {
        //TO DO
    },

    REJECTED {
        //TO DO
    },

    PUBLISHED {
        //TO DO
    },

    ARCHIVED {
        //TO DO
    };

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
}