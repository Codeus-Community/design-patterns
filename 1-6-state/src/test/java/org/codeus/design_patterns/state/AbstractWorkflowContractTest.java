package org.codeus.design_patterns.state;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.*;

/** Behavioral contract for ANY WorkflowContract implementation. */
public abstract class AbstractWorkflowContractTest {

    /** Test data holder for document types. */
    protected record DocCase(String type, String author, String content, boolean urgent) {
        @Override public String toString() { return type + (urgent ? " (urgent)" : ""); }
    }

    /** Factory each subclass must provide. */
    protected abstract WorkflowContract newWorkflow(DocCase dc);

    // ---------- Engine-compliant content ----------
    protected static Stream<DocCase> engines() {
        String legal = String.join(" ",
                "Contract Agreement between Parties with detailed terms and conditions,",
                "confidentiality and non-disclosure, indemnification, limitation of liability,",
                "governing law, jurisdiction, signatures, international and cross-border compliance,",
                "remedies, warranties, termination, amendments, exhibits, schedules, assignment,",
                "severability, and entire-agreement clauses."
        );

        String hr = String.join(" ",
                "HR Policy Document. Effective Date: 2025-01-01.",
                "Equal opportunity, anti-discrimination, harassment prevention, disciplinary procedure,",
                "health and safety guidance, time-off, parental leave, benefits eligibility,",
                "performance management, remote-work policy, training, code of conduct, grievance process, review cadence."
        );

        String fin = String.join(" ",
                "Q4 Financial Report summarizing revenue, expenses, balance sheet, cash flow, footnotes,",
                "MD&A discussion, forward-looking statements with disclaimer of risks and uncertainties,",
                "audit references, GAAP/IFRS alignment, SOX controls, and regulatory compliance."
        );

        // NOTE: no “guaranteed/best/#1” or similar absolute words
        String mkt = String.join(" ",
                "Marketing content for product launch describing features, benefits, use cases, and user stories.",
                "Compliant claims only; messaging pillars, distribution channels, campaign plan, and CTA guidelines.",
                "Text is intentionally long to satisfy validators without absolute promises."
        );

        return Stream.of(
                new DocCase("LEGAL_CONTRACT",     "alice", legal, false),
                new DocCase("HR_POLICY",          "bob",   hr,    false),
                new DocCase("FINANCIAL_REPORT",   "carol", fin,   false),
                new DocCase("MARKETING_CONTENT",  "dave",  mkt,   false),
                new DocCase("MARKETING_CONTENT",  "erin",  mkt,   true) // urgent case
        );
    }

    private static String positive(int i) {
        return switch (i % 3) {
            case 0 -> "approve - looks good to me";
            case 1 -> "acceptable - ok to proceed";
            default -> "ready for next step";
        };
    }

    /** Keep both helpers so either name works. */
    static String tail(List<String> history, int n) {
        if (history == null || history.isEmpty()) return "";
        return history.stream().skip(Math.max(0, history.size() - n))
                .collect(Collectors.joining("\n"));
    }
    static String tailHistory(List<String> history, int n) { return tail(history, n); }

    // ---------- Tests ----------

    @ParameterizedTest(name = "happy path publish: {0}")
    @MethodSource("engines")
    void happy_path_publish(DocCase dc) {
        WorkflowContract wf = newWorkflow(dc);

        assertTrue(wf.submitForReview(), "Submit should succeed. History:\n" + tail(wf.getActionHistory(), 8));

        // All reviews
        for (int i = 0; i < wf.getReviewers().size(); i++) {
            assertTrue(
                    wf.addReview(wf.getReviewers().get(i), positive(i), true),
                    "Review failed. History:\n" + tail(wf.getActionHistory(), 8)
            );
        }

        // Approvals: stop if the engine already auto-published after reviews
        for (String approver : wf.getApprovers()) {
            if (wf.getPublishedAt() != null || "PUBLISHED".equalsIgnoreCase(wf.getStatusName())) break;
            boolean ok = wf.addApproval(approver, true, "business need");
            if (!ok) {
                if (wf.getPublishedAt() != null || "PUBLISHED".equalsIgnoreCase(wf.getStatusName())) break;
                fail("Approval failed while not yet published.\n" + tail(wf.getActionHistory(), 10));
            }
        }

        // If not auto-published, publish once manually (don't assert its boolean)
        if (wf.getPublishedAt() == null) {
            wf.publish();
        }

        // Final state assertions (work for both auto and manual)
        assertNotNull(wf.getPublishedAt(), "Expected publishedAt. History:\n" + tail(wf.getActionHistory(), 12));
        assertNotNull(wf.getExpiryDate(),  "Expected expiryDate. History:\n" + tail(wf.getActionHistory(), 12));
    }

    @ParameterizedTest(name = "update resets BEFORE approvals: {0}")
    @MethodSource("engines")
    void update_resets_before_approvals(DocCase dc) {
        WorkflowContract wf = newWorkflow(dc);

        assertTrue(wf.submitForReview(), tail(wf.getActionHistory(), 8));

        // Leave at least ONE review pending so we don't complete the review stage (prevents auto-publish)
        int reviewers = wf.getReviewers().size();
        int toApprove = Math.max(0, reviewers - 1);   // add all-but-last (or none if 0/1)
        for (int i = 0; i < toApprove; i++) {
            assertTrue(
                    wf.addReview(wf.getReviewers().get(i), positive(i), true),
                    "Adding partial reviews failed\n" + tail(wf.getActionHistory(), 8)
            );
        }

        // Now update while still UNDER_REVIEW (and definitely not published)
        int revBefore = wf.getRevisionCount();
        String updated = dc.content() + " Minor edit by QA.";

        assertTrue(
                wf.updateContent(updated, dc.author()),
                "Update should succeed before final review/approvals\n" + tail(wf.getActionHistory(), 12)
        );

        // Post-conditions: reset + revision bump
        assertTrue(wf.getReviews().isEmpty(), "Reviews should be cleared after update");
        assertTrue(wf.getApprovals().isEmpty(), "Approvals should be cleared after update");
        assertTrue(wf.getRevisionCount() >= revBefore + 1, "Revision count should increase");
    }

    @ParameterizedTest(name = "rejection blocks publish: {0}")
    @MethodSource("engines")
    void rejection_blocks_publish(DocCase dc) {
        WorkflowContract wf = newWorkflow(dc);

        assertTrue(wf.submitForReview(), tail(wf.getActionHistory(), 8));
        if (!wf.getReviewers().isEmpty()) {
            assertTrue(wf.addReview(wf.getReviewers().get(0), "reject - needs revision", false));
        }
        assertFalse(wf.publish(), "Publish must be blocked\n" + tail(wf.getActionHistory(), 10));
        assertNull(wf.getPublishedAt());
    }

    @Test
    void urgent_marks_history() {
        DocCase urgent = engines().filter(DocCase::urgent).findFirst().orElseThrow();
        WorkflowContract wf = newWorkflow(urgent);

        assertTrue(wf.submitForReview(), tail(wf.getActionHistory(), 8));
        boolean flagged = wf.getActionHistory().stream()
                .anyMatch(h -> h.toLowerCase().contains("urgent") || h.toLowerCase().contains("fast-track"));
        assertTrue(flagged, "Urgent/fast-track marker missing");
    }

    @ParameterizedTest(name = "views after publish (if exposed): {0}")
    @MethodSource("engines")
    void views_after_publish(DocCase dc) {
        WorkflowContract wf = newWorkflow(dc);
        assertTrue(wf.submitForReview(), tail(wf.getActionHistory(), 8));
        for (int i = 0; i < wf.getReviewers().size(); i++) {
            wf.addReview(wf.getReviewers().get(i), positive(i), true);
        }
        for (String approver : wf.getApprovers()) {
            wf.addApproval(approver, true, null);
        }
        if (wf.getPublishedAt() == null) wf.publish();
        assertNotNull(wf.getPublishedAt(), "Expected publishedAt");

        Integer v0 = extractViews(wf.getStatusReport());
        wf.incrementViews();
        wf.incrementViews();
        Integer v1 = extractViews(wf.getStatusReport());
        if (v0 != null && v1 != null) {
            assertTrue(v1 >= v0 + 2, "Views should increase by >= 2");
        }
    }

    private static Integer extractViews(Map<String, Object> status) {
        if (status == null) return null;
        Object v = status.get("views");
        return (v instanceof Number) ? ((Number) v).intValue() : null;
    }
}
