package org.codeus.design_patterns.state;

import org.codeus.design_patterns.state.clean.DocumentWorkflow;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DocumentWorkflowTest {

    // ---------- Helpers ----------

    private static DocumentWorkflow wf(String type, String author, String content, boolean urgent) {
        return new DocumentWorkflow(UUID.randomUUID().toString(), type, content, author, urgent);
    }

    private static String legalContent() {
        return String.join(" ",
                "Contract Agreement between Parties with detailed terms and conditions,",
                "confidentiality and non-disclosure, indemnification, limitation of liability,",
                "governing law, jurisdiction, signatures, international and cross-border compliance,",
                "remedies, warranties, termination, amendments, exhibits, schedules, assignment,",
                "severability, and entire-agreement clauses."
        );
    }

    private static String hrContent() {
        return String.join(" ",
                "HR Policy Document. Effective Date: 2025-01-01.",
                "Equal opportunity, anti-discrimination, harassment prevention, disciplinary procedure,",
                "health and safety guidance, time-off, parental leave, benefits eligibility,",
                "performance management, remote-work policy, training, code of conduct, grievance process, review cadence."
        );
    }

    private static String financeContent() {
        return String.join(" ",
                "Q4 Financial Report summarizing revenue, expenses, balance sheet, cash flow, footnotes,",
                "MD&A discussion, forward-looking statements with disclaimer of risks and uncertainties,",
                "audit references, GAAP/IFRS alignment, SOX controls, and regulatory compliance."
        );
    }

    private static String marketingContent() {
        // IMPORTANT: no “#1/best/guaranteed/always/never” absolute claims.
        return String.join(" ",
                "Marketing content for product launch describing features, benefits, use cases, and user stories.",
                "Compliant claims only; messaging pillars, distribution channels, campaign plan, and CTA guidelines.",
                "Text is intentionally long to satisfy validators without absolute promises."
        );
    }

    // ---------- Tests ----------

    @Test
    void testRevisionProcess() {
        DocumentWorkflow wf = wf("LEGAL_CONTRACT", "alice", legalContent(), false);

        assertTrue(wf.submitForReview(), "submit failed");

        // Approve all but the last review to keep it UNDER_REVIEW
        List<String> reviewers = wf.getReviewers();
        int keepPending = reviewers.isEmpty() ? 0 : 1;
        for (int i = 0; i < reviewers.size() - keepPending; i++) {
            assertTrue(wf.addReview(reviewers.get(i), "ok", true));
        }

        int revBefore = wf.getRevisionCount();
        assertTrue(wf.updateContent(legalContent() + " Minor edit by QA.", "alice"),
                "update should succeed before approvals");

        assertTrue(wf.getReviews().isEmpty(), "reviews must reset after update");
        assertTrue(wf.getApprovals().isEmpty(), "approvals must reset after update");
        assertTrue(wf.getRevisionCount() >= revBefore + 1, "revision must increment");
    }

    @Test
    void testLegalContractWorkflow() {
        DocumentWorkflow wf = wf("LEGAL_CONTRACT", "alice", legalContent(), false);
        assertTrue(wf.submitForReview());

        // All reviews
        for (String r : wf.getReviewers()) {
            assertTrue(wf.addReview(r, "approve", true));
        }
        // All approvals; engines may auto-publish once fully approved
        for (String a : wf.getApprovers()) {
            if (wf.getPublishedAt() != null) break; // stop if auto-published already
            boolean ok = wf.addApproval(a, true, "business need");
            if (!ok && wf.getPublishedAt() != null) break; // acceptable: just transitioned
            assertTrue(ok, "approval failed before publish");
        }

        if (wf.getPublishedAt() == null) {
            wf.publish(); // ignore boolean; already-published engines return false
        }

        assertNotNull(wf.getPublishedAt(), "publishedAt must be set");
        assertNotNull(wf.getExpiryDate(), "expiryDate must be set");
        assertFalse(wf.getExpiryDate().isBefore(LocalDateTime.now()));
    }

    @Test
    void testMarketingContentCompliance() {
        DocumentWorkflow wf = wf("MARKETING_CONTENT", "marketer", marketingContent(), false);

        // Content should validate and allow submission
        assertTrue(wf.submitForReview(), "marketing content should pass validation and submit");

        // Positive flow to publish (auto or manual)
        for (String r : wf.getReviewers()) {
            assertTrue(wf.addReview(r, "approve", true));
        }
        for (String a : wf.getApprovers()) {
            if (wf.getPublishedAt() != null) break;
            boolean ok = wf.addApproval(a, true, "ok");
            if (!ok && wf.getPublishedAt() != null) break;
            assertTrue(ok, "approval failed before publish");
        }
        if (wf.getPublishedAt() == null) wf.publish();

        Map<String, Object> report = wf.getStatusReport();
        assertNotNull(wf.getPublishedAt());
        assertNotNull(wf.getExpiryDate());
        assertEquals("Published", wf.getStatusName());
        assertTrue(((Number)report.getOrDefault("views", 0)).intValue() >= 0);
    }

    @Test
    void testUrgentFinancialReport() {
        DocumentWorkflow wf = wf("FINANCIAL_REPORT", "cfo", financeContent(), true);
        assertTrue(wf.submitForReview());

        // Should have urgent / fast-track hint in history
        boolean flagged = wf.getActionHistory().stream()
                .map(String::toLowerCase)
                .anyMatch(h -> h.contains("urgent") || h.contains("fast-track"));
        assertTrue(flagged, "urgent/fast-track marker expected in history");

        // Complete and ensure we end up published
        for (String r : wf.getReviewers()) {
            assertTrue(wf.addReview(r, "approve", true));
        }
        for (String a : wf.getApprovers()) {
            if (wf.getPublishedAt() != null) break;
            boolean ok = wf.addApproval(a, true, "ok");
            if (!ok && wf.getPublishedAt() != null) break;
            assertTrue(ok);
        }
        if (wf.getPublishedAt() == null) wf.publish();

        assertNotNull(wf.getPublishedAt());
        assertEquals("Published", wf.getStatusName());
    }
}
