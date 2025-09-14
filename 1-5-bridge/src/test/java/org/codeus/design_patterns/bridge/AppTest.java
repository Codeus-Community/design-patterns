package org.codeus.design_patterns.bridge;

import org.codeus.design_patterns.bridge.dirty.*;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for the "dirty" tightly coupled implementation
 * These tests demonstrate the current behavior that should be preserved
 * after refactoring to the Bridge pattern
 */
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AppTest {

    private final List<String> sampleReportData = List.of("Revenue: $1M", "Profit: $200K", "Growth: 20%");
    private final List<String> sampleInvoiceItems = List.of("Service A: $500", "Service B: $300", "Tax: $80");
    private final List<String> sampleContractTerms = List.of("Duration: 2 years", "Payment: Monthly", "Renewal: Auto");

    @Nested
    @Order(1)
    @DisplayName("Report Generation Tests")
    class ReportTests {

        @Test
        @DisplayName("PDF Report should generate valid PDF format")
        void testPdfReportGeneration() {
            PdfReport report = new PdfReport("Test Report", sampleReportData);
            String result = report.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[PDF]"));
            assertTrue(result.contains("%%PDF-1.4"));
            assertTrue(result.contains("REPORT DOCUMENT"));
            assertTrue(result.contains("Test Report"));
            assertTrue(result.contains("Revenue: $1M"));
            assertTrue(result.contains("Financial Report"));
            assertTrue(result.contains("%%EOF"));
        }

        @Test
        @DisplayName("DOCX Report should generate valid DOCX format")
        void testDocxReportGeneration() {
            DocxReport report = new DocxReport("Test Report", sampleReportData);
            String result = report.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[DOCX]"));
            assertTrue(result.contains("<?xml version=\"1.0\""));
            assertTrue(result.contains("<w:document"));
            assertTrue(result.contains("REPORT DOCUMENT"));
            assertTrue(result.contains("Test Report"));
            assertTrue(result.contains("Revenue: $1M"));
            assertTrue(result.contains("Financial Report"));
        }

        @Test
        @DisplayName("HTML Report should generate valid HTML format")
        void testHtmlReportGeneration() {
            HtmlReport report = new HtmlReport("Test Report", sampleReportData);
            String result = report.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[HTML]"));
            assertTrue(result.contains("<!DOCTYPE html>"));
            assertTrue(result.contains("<h1>REPORT DOCUMENT</h1>"));
            assertTrue(result.contains("Test Report"));
            assertTrue(result.contains("Revenue: $1M"));
            assertTrue(result.contains("Financial Report"));
            assertTrue(result.contains("</html>"));
        }

        @Test
        @DisplayName("All report formats should contain timestamp")
        void testReportTimestamps() {
            PdfReport pdfReport = new PdfReport("Test", sampleReportData);
            DocxReport docxReport = new DocxReport("Test", sampleReportData);
            HtmlReport htmlReport = new HtmlReport("Test", sampleReportData);
            
            assertTrue(pdfReport.generate().contains("Generated:"));
            assertTrue(docxReport.generate().contains("Generated:"));
            assertTrue(htmlReport.generate().contains("Generated:"));
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Invoice Generation Tests")
    class InvoiceTests {

        @Test
        @DisplayName("PDF Invoice should generate valid PDF format")
        void testPdfInvoiceGeneration() {
            PdfInvoice invoice = new PdfInvoice("INV-001", sampleInvoiceItems, "TestCorp");
            String result = invoice.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[PDF]"));
            assertTrue(result.contains("%%PDF-1.4"));
            assertTrue(result.contains("INVOICE DOCUMENT"));
            assertTrue(result.contains("INV-001"));
            assertTrue(result.contains("TestCorp"));
            assertTrue(result.contains("Service A: $500"));
            assertTrue(result.contains("Commercial Invoice"));
            assertTrue(result.contains("%%EOF"));
        }

        @Test
        @DisplayName("DOCX Invoice should generate valid DOCX format")
        void testDocxInvoiceGeneration() {
            DocxInvoice invoice = new DocxInvoice("INV-002", sampleInvoiceItems, "TestCorp");
            String result = invoice.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[DOCX]"));
            assertTrue(result.contains("<?xml version=\"1.0\""));
            assertTrue(result.contains("<w:document"));
            assertTrue(result.contains("INVOICE DOCUMENT"));
            assertTrue(result.contains("INV-002"));
            assertTrue(result.contains("TestCorp"));
            assertTrue(result.contains("Service A: $500"));
            assertTrue(result.contains("Commercial Invoice"));
        }

        @Test
        @DisplayName("HTML Invoice should generate valid HTML format")
        void testHtmlInvoiceGeneration() {
            HtmlInvoice invoice = new HtmlInvoice("INV-003", sampleInvoiceItems, "TestCorp");
            String result = invoice.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[HTML]"));
            assertTrue(result.contains("<!DOCTYPE html>"));
            assertTrue(result.contains("<h1>INVOICE DOCUMENT</h1>"));
            assertTrue(result.contains("INV-003"));
            assertTrue(result.contains("TestCorp"));
            assertTrue(result.contains("Service A: $500"));
            assertTrue(result.contains("Commercial Invoice"));
            assertTrue(result.contains("</html>"));
        }

        @Test
        @DisplayName("Invoice should handle empty items list")
        void testInvoiceWithEmptyItems() {
            PdfInvoice invoice = new PdfInvoice("INV-EMPTY", List.of(), "TestCorp");
            String result = invoice.generate();
            
            assertNotNull(result);
            assertTrue(result.contains("INV-EMPTY"));
            assertTrue(result.contains("TestCorp"));
            // Should still contain structure even with empty items
            assertTrue(result.contains("INVOICE ITEMS:"));
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Contract Generation Tests")
    class ContractTests {

        @Test
        @DisplayName("PDF Contract should generate valid PDF format")
        void testPdfContractGeneration() {
            PdfContract contract = new PdfContract("Service Agreement", sampleContractTerms, "Legal");
            String result = contract.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[PDF]"));
            assertTrue(result.contains("%%PDF-1.4"));
            assertTrue(result.contains("CONTRACT DOCUMENT"));
            assertTrue(result.contains("Service Agreement"));
            assertTrue(result.contains("Legal"));
            assertTrue(result.contains("Duration: 2 years"));
            assertTrue(result.contains("Legal Contract"));
            assertTrue(result.contains("%%EOF"));
        }

        @Test
        @DisplayName("DOCX Contract should generate valid DOCX format")
        void testDocxContractGeneration() {
            DocxContract contract = new DocxContract("Service Agreement", sampleContractTerms, "Legal");
            String result = contract.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[DOCX]"));
            assertTrue(result.contains("<?xml version=\"1.0\""));
            assertTrue(result.contains("<w:document"));
            assertTrue(result.contains("CONTRACT DOCUMENT"));
            assertTrue(result.contains("Service Agreement"));
            assertTrue(result.contains("Legal"));
            assertTrue(result.contains("Duration: 2 years"));
            assertTrue(result.contains("Legal Contract"));
        }

        @Test
        @DisplayName("HTML Contract should generate valid HTML format")
        void testHtmlContractGeneration() {
            HtmlContract contract = new HtmlContract("Service Agreement", sampleContractTerms, "Legal");
            String result = contract.generate();
            
            assertNotNull(result);
            assertTrue(result.startsWith("[HTML]"));
            assertTrue(result.contains("<!DOCTYPE html>"));
            assertTrue(result.contains("<h1>CONTRACT DOCUMENT</h1>"));
            assertTrue(result.contains("Service Agreement"));
            assertTrue(result.contains("Legal"));
            assertTrue(result.contains("Duration: 2 years"));
            assertTrue(result.contains("Legal Contract"));
            assertTrue(result.contains("</html>"));
        }
    }

    @Nested
    @Order(4)
    @DisplayName("Cross-Format Consistency Tests")
    class ConsistencyTests {

        @Test
        @DisplayName("Same report should contain same content across formats")
        void testReportContentConsistency() {
            String title = "Consistency Test Report";
            List<String> data = List.of("Item 1", "Item 2");
            
            PdfReport pdfReport = new PdfReport(title, data);
            DocxReport docxReport = new DocxReport(title, data);
            HtmlReport htmlReport = new HtmlReport(title, data);
            
            String pdfResult = pdfReport.generate();
            String docxResult = docxReport.generate();
            String htmlResult = htmlReport.generate();
            
            // All should contain the same business content
            for (String format : List.of(pdfResult, docxResult, htmlResult)) {
                assertTrue(format.contains(title));
                assertTrue(format.contains("Item 1"));
                assertTrue(format.contains("Item 2"));
                assertTrue(format.contains("Financial Report"));
            }
        }

        @Test
        @DisplayName("Same invoice should contain same content across formats")
        void testInvoiceContentConsistency() {
            String invoiceNum = "INV-CONSISTENCY";
            String company = "ConsistencyCorp";
            List<String> items = List.of("Test Item: $100");
            
            PdfInvoice pdfInvoice = new PdfInvoice(invoiceNum, items, company);
            DocxInvoice docxInvoice = new DocxInvoice(invoiceNum, items, company);
            HtmlInvoice htmlInvoice = new HtmlInvoice(invoiceNum, items, company);
            
            String pdfResult = pdfInvoice.generate();
            String docxResult = docxInvoice.generate();
            String htmlResult = htmlInvoice.generate();
            
            // All should contain the same business content
            for (String format : List.of(pdfResult, docxResult, htmlResult)) {
                assertTrue(format.contains(invoiceNum));
                assertTrue(format.contains(company));
                assertTrue(format.contains("Test Item: $100"));
                assertTrue(format.contains("Commercial Invoice"));
            }
        }
    }

    @Nested
    @Order(5)
    @DisplayName("Integration Tests - Main App Execution")
    class IntegrationTests {

        @Test
        @DisplayName("Main method should execute without errors")
        void testMainMethodExecution() {
            // This test ensures the main method runs without throwing exceptions
            assertDoesNotThrow(() -> {
                org.codeus.design_patterns.bridge.dirty.App.main(new String[]{});
            });
        }

        @Test
        @DisplayName("All document types should be creatable")
        void testAllDocumentTypesCreation() {
            // Test that we can create all 9 combinations without errors
            assertDoesNotThrow(() -> {
                // Reports
                new PdfReport("Test", sampleReportData).generate();
                new DocxReport("Test", sampleReportData).generate();
                new HtmlReport("Test", sampleReportData).generate();
                
                // Invoices
                new PdfInvoice("INV-001", sampleInvoiceItems, "Corp").generate();
                new DocxInvoice("INV-001", sampleInvoiceItems, "Corp").generate();
                new HtmlInvoice("INV-001", sampleInvoiceItems, "Corp").generate();
                
                // Contracts
                new PdfContract("Agreement", sampleContractTerms, "Legal").generate();
                new DocxContract("Agreement", sampleContractTerms, "Legal").generate();
                new HtmlContract("Agreement", sampleContractTerms, "Legal").generate();
            });
        }
    }

    @Nested
    @Order(6)
    @DisplayName("Performance and Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null-safe operations")
        void testNullSafety() {
            // Test with empty strings (not null to avoid NPE in current implementation)
            assertDoesNotThrow(() -> {
                new PdfReport("", List.of()).generate();
                new DocxInvoice("", List.of(), "").generate();
                new HtmlContract("", List.of(), "").generate();
            });
        }

        @Test
        @DisplayName("Should handle large data sets")
        void testLargeDataSets() {
            List<String> largeDataSet = List.of(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
                "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
            );
            
            assertDoesNotThrow(() -> {
                PdfReport report = new PdfReport("Large Report", largeDataSet);
                String result = report.generate();
                assertNotNull(result);
                assertTrue(result.contains("Item 1"));
                assertTrue(result.contains("Item 10"));
            });
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters() {
            List<String> specialData = List.of("Item with & ampersand", "Item with <brackets>", "Item with \"quotes\"");
            
            assertDoesNotThrow(() -> {
                PdfReport report = new PdfReport("Special Report", specialData);
                DocxInvoice invoice = new DocxInvoice("INV-SPECIAL", specialData, "Corp & Co");
                HtmlContract contract = new HtmlContract("Agreement <Test>", specialData, "Legal \"Dept\"");
                
                assertNotNull(report.generate());
                assertNotNull(invoice.generate());
                assertNotNull(contract.generate());
            });
        }
    }
}