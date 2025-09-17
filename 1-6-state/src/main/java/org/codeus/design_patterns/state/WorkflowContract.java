package org.codeus.design_patterns.state;

import java.util.List;
import java.util.Map;

public interface WorkflowContract {
    boolean submitForReview();
    boolean addReview(String reviewerId, String reviewComment, boolean approved);
    boolean addApproval(String approverId, boolean approved, String reason);
    boolean publish();
    boolean updateContent(String newContent, String updatedBy);
    boolean archive(String reason);
    void incrementViews();

    Map<String,Object> getStatusReport();
    String getStatusName();

    List<String> getReviewers();
    List<String> getApprovers();
    Map<String,String> getReviews();
    Map<String,Boolean> getApprovals();
    List<String> getActionHistory();
    java.time.LocalDateTime getCreatedAt();
    java.time.LocalDateTime getPublishedAt();
    java.time.LocalDateTime getExpiryDate();
    int getRevisionCount();
}
