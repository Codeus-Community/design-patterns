package org.codeus.design_patterns.bridge.dirty;

import java.util.List;

/**
 * Document Management System - Initial "Dirty" Implementation
 * 
 * This implementation demonstrates the problem that the Bridge pattern solves:
 * - Tight coupling between document types and export formats
 * - Code duplication across similar classes
 * - Difficulty adding new document types or formats
 * - Violation of Single Responsibility Principle
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== Document Management System ===");
        System.out.println("Current Implementation: Tightly Coupled Classes");
        System.out.println();

        // Create sample data
        List<String> reportData = List.of("Q4 Revenue: $1.2M", "Growth: 15%", "Expenses: $800K");
        List<String> invoiceData = List.of("Item 1: $100", "Item 2: $250", "Tax: $35", "Total: $385");
        List<String> contractData = List.of("Party A: TechCorp", "Party B: ClientCorp", "Duration: 12 months", "Value: $50K");

        System.out.println("--- Reports ---");
        // PDF Report
        PdfReport pdfReport = new PdfReport("Q4 Financial Report", reportData);
        System.out.println(pdfReport.generate());
        
        // DOCX Report  
        DocxReport docxReport = new DocxReport("Q4 Financial Report", reportData);
        System.out.println(docxReport.generate());
        
        // HTML Report
        HtmlReport htmlReport = new HtmlReport("Q4 Financial Report", reportData);
        System.out.println(htmlReport.generate());

        System.out.println("\n--- Invoices ---");
        // PDF Invoice
        PdfInvoice pdfInvoice = new PdfInvoice("INV-2024-001", invoiceData, "TechCorp Inc.");
        System.out.println(pdfInvoice.generate());
        
        // DOCX Invoice
        DocxInvoice docxInvoice = new DocxInvoice("INV-2024-001", invoiceData, "TechCorp Inc.");
        System.out.println(docxInvoice.generate());
        
        // HTML Invoice
        HtmlInvoice htmlInvoice = new HtmlInvoice("INV-2024-001", invoiceData, "TechCorp Inc.");
        System.out.println(htmlInvoice.generate());

        System.out.println("\n--- Contracts ---");
        // PDF Contract
        PdfContract pdfContract = new PdfContract("Service Agreement", contractData, "Legal Dept");
        System.out.println(pdfContract.generate());
        
        // DOCX Contract
        DocxContract docxContract = new DocxContract("Service Agreement", contractData, "Legal Dept");
        System.out.println(docxContract.generate());
        
        // HTML Contract
        HtmlContract htmlContract = new HtmlContract("Service Agreement", contractData, "Legal Dept");
        System.out.println(htmlContract.generate());

        System.out.println("\n=== Problems with Current Implementation ===");
        System.out.println("1. 9 classes for 3 document types × 3 formats = Combinatorial explosion!");
        System.out.println("2. Duplicated format logic across document types");
        System.out.println("3. Duplicated document logic across formats");
        System.out.println("4. Adding new format requires 3 new classes");
        System.out.println("5. Adding new document type requires 3 new classes");
        System.out.println("6. Difficult to maintain and test");
        System.out.println("\nRefactor this using the Bridge Pattern!");
    }
}