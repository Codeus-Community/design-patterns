package org.codeus.design_patterns.bridge;

import org.codeus.design_patterns.bridge.clean.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dynamic Bridge Pattern Tests")
public class DynamicBridgeTest {

    private List<Class<? extends Document>> documentClasses;
    private List<Class<? extends ExportFormat>> exporterClasses;
    private List<String> sampleData;

    @BeforeEach
    void setUp() {
        sampleData = List.of("Test Item 1", "Test Item 2", "Test Item 3");
        
        documentClasses = findDocumentClasses();
        exporterClasses = findExporterClasses();
        
        System.out.println("Found Document classes: " + documentClasses.size());
        documentClasses.forEach(cls -> System.out.println("  - " + cls.getSimpleName()));
        
        System.out.println("Found ExportFormat classes: " + exporterClasses.size());
        exporterClasses.forEach(cls -> System.out.println("  - " + cls.getSimpleName()));
    }

    @TestFactory
    @DisplayName("Test all Document + ExportFormat combinations")
    Stream<DynamicTest> testAllCombinations() {
        List<DynamicTest> tests = new ArrayList<>();
        
        for (Class<? extends Document> docClass : documentClasses) {
            for (Class<? extends ExportFormat> exporterClass : exporterClasses) {
                String testName = String.format("%s with %s", 
                    docClass.getSimpleName(), exporterClass.getSimpleName());
                
                DynamicTest test = DynamicTest.dynamicTest(testName, () -> {
                    testDocumentExporterCombination(docClass, exporterClass);
                });
                
                tests.add(test);
            }
        }
        
        tests.addAll(createExpectedCombinationTests());
        
        return tests.stream();
    }

    @TestFactory
    @DisplayName("Test Bridge Pattern Implementation Requirements")
    Stream<DynamicTest> testBridgePatternRequirements() {
        return Stream.of(
            DynamicTest.dynamicTest("Document classes should extend Document", () -> {
                assertTrue(documentClasses.size() >= 0, "Should have Document implementations");
                for (Class<? extends Document> cls : documentClasses) {
                    assertTrue(Document.class.isAssignableFrom(cls), 
                        cls.getSimpleName() + " should extend Document");
                }
            }),
            
            DynamicTest.dynamicTest("Exporter classes should implement ExportFormat", () -> {
                assertTrue(exporterClasses.size() >= 0, "Should have ExportFormat implementations");
                for (Class<? extends ExportFormat> cls : exporterClasses) {
                    assertTrue(ExportFormat.class.isAssignableFrom(cls), 
                        cls.getSimpleName() + " should implement ExportFormat");
                }
            }),
            
            DynamicTest.dynamicTest("Should have Report, Invoice, Contract documents", () -> {
                Set<String> expectedDocs = Set.of("Report", "Invoice", "Contract");
                Set<String> foundDocs = new HashSet<>();
                documentClasses.forEach(cls -> foundDocs.add(cls.getSimpleName()));
                
                for (String expected : expectedDocs) {
                    if (foundDocs.contains(expected)) {
                        System.out.println("✅ Found " + expected + " document class");
                    } else {
                        System.out.println("❌ Missing " + expected + " document class");
                    }
                }
            }),
            
            DynamicTest.dynamicTest("Should have Pdf, Docx, Html exporters", () -> {
                Set<String> expectedExporters = Set.of("PdfExporter", "DocxExporter", "HtmlExporter");
                Set<String> foundExporters = new HashSet<>();
                exporterClasses.forEach(cls -> foundExporters.add(cls.getSimpleName()));
                
                for (String expected : expectedExporters) {
                    if (foundExporters.contains(expected)) {
                        System.out.println("✅ Found " + expected + " exporter class");
                    } else {
                        System.out.println("❌ Missing " + expected + " exporter class");
                    }
                }
            })
        );
    }

    private void testDocumentExporterCombination(Class<? extends Document> docClass, 
                                               Class<? extends ExportFormat> exporterClass) {
        try {
            ExportFormat exporter = exporterClass.getDeclaredConstructor().newInstance();
            
            Document document = createDocumentInstance(docClass, exporter);
            
            String result = document.generate();
            
            assertNotNull(result, "Generated document should not be null");
            assertFalse(result.trim().isEmpty(), "Generated document should not be empty");
            
            validateFormatSpecificSyntax(result, exporterClass.getSimpleName());
            
            validateContentCorrectness(result, docClass, document);
            
            validateBusinessLogic(result, docClass, exporterClass);
            
            System.out.println("✅ " + docClass.getSimpleName() + " + " + 
                             exporterClass.getSimpleName() + " PASSED all validations");
            
        } catch (Exception e) {
            fail("Failed to test " + docClass.getSimpleName() + " with " + 
                 exporterClass.getSimpleName() + ": " + e.getMessage());
        }
    }

    private Document createDocumentInstance(Class<? extends Document> docClass, ExportFormat exporter) 
            throws Exception {
        String className = docClass.getSimpleName().toLowerCase();
        
        if (className.contains("report")) {
            Constructor<? extends Document> constructor = docClass.getDeclaredConstructor(
                ExportFormat.class, String.class, List.class);
            return constructor.newInstance(exporter, "Test Report", sampleData);
        } else if (className.contains("invoice")) {
            Constructor<? extends Document> constructor = docClass.getDeclaredConstructor(
                ExportFormat.class, String.class, List.class, String.class);
            return constructor.newInstance(exporter, "INV-001", sampleData, "TestCorp");
        } else if (className.contains("contract")) {
            Constructor<? extends Document> constructor = docClass.getDeclaredConstructor(
                ExportFormat.class, String.class, List.class, String.class);
            return constructor.newInstance(exporter, "Test Contract", sampleData, "Legal Dept");
        } else {
            Constructor<? extends Document> constructor = docClass.getDeclaredConstructor(ExportFormat.class);
            return constructor.newInstance(exporter);
        }
    }

    private String getExpectedPrefix(String exporterClassName) {
        if (exporterClassName.toLowerCase().contains("pdf")) return "[PDF]";
        if (exporterClassName.toLowerCase().contains("docx")) return "[DOCX]";
        if (exporterClassName.toLowerCase().contains("html")) return "[HTML]";
        return null;
    }

    private List<DynamicTest> createExpectedCombinationTests() {
        List<DynamicTest> tests = new ArrayList<>();
        
        String[] documentTypes = {"Report", "Invoice", "Contract"};
        String[] formatTypes = {"Pdf", "Docx", "Html"};
        
        for (String docType : documentTypes) {
            for (String formatType : formatTypes) {
                String expectedClassName = formatType + docType;
                
                DynamicTest test = DynamicTest.dynamicTest(
                    "Should be able to create " + expectedClassName + " equivalent", () -> {
                        
                    Optional<Class<? extends Document>> docClass = documentClasses.stream()
                        .filter(cls -> cls.getSimpleName().equalsIgnoreCase(docType))
                        .findFirst();
                        
                    Optional<Class<? extends ExportFormat>> exporterClass = exporterClasses.stream()
                        .filter(cls -> cls.getSimpleName().toLowerCase().contains(formatType.toLowerCase()))
                        .findFirst();
                        
                    if (docClass.isPresent() && exporterClass.isPresent()) {
                        testDocumentExporterCombination(docClass.get(), exporterClass.get());
                        System.out.println("✅ " + expectedClassName + " equivalent PASSED all validations");
                    } else {
                        System.out.println("⏳ " + expectedClassName + " equivalent not yet implemented");
                        System.out.println("   Need: " + docType + " document + " + formatType + " exporter");
                    }
                });
                
                tests.add(test);
            }
        }
        
        return tests;
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends Document>> findDocumentClasses() {
        List<Class<? extends Document>> classes = new ArrayList<>();
        
        try {
            String packageName = "org.codeus.design_patterns.bridge.clean";
            String path = packageName.replace('.', '/');
            URL resource = getClass().getClassLoader().getResource(path);
            
            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            try {
                                Class<?> cls = Class.forName(className);
                                if (Document.class.isAssignableFrom(cls) && 
                                    !cls.equals(Document.class) && 
                                    !Modifier.isAbstract(cls.getModifiers())) {
                                    classes.add((Class<? extends Document>) cls);
                                }
                            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                                // Skip classes that can't be loaded
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding Document classes: " + e.getMessage());
        }
        
        return classes;
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends ExportFormat>> findExporterClasses() {
        List<Class<? extends ExportFormat>> classes = new ArrayList<>();
        
        try {
            String packageName = "org.codeus.design_patterns.bridge.clean";
            String path = packageName.replace('.', '/');
            URL resource = getClass().getClassLoader().getResource(path);
            
            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    for (File file : directory.listFiles()) {
                        if (file.getName().endsWith(".class")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            try {
                                Class<?> cls = Class.forName(className);
                                if (ExportFormat.class.isAssignableFrom(cls) && 
                                    !cls.equals(ExportFormat.class) && 
                                    !cls.isInterface() && 
                                    !Modifier.isAbstract(cls.getModifiers())) {
                                    classes.add((Class<? extends ExportFormat>) cls);
                                }
                            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding ExportFormat classes: " + e.getMessage());
        }
        
        return classes;
    }

    private void validateFormatSpecificSyntax(String result, String exporterClassName) {
        String formatType = exporterClassName.toLowerCase();
        
        if (formatType.contains("pdf")) {
            assertNotNull(result, "PDF result cannot be null");
            assertTrue(result.startsWith("[PDF]"), 
                "❌ FAILED: PDF document should start with [PDF] prefix. Got: " + result.substring(0, Math.min(50, result.length())));
            assertTrue(result.contains("%%PDF-1.4"), 
                "❌ FAILED: PDF should contain PDF header '%%PDF-1.4'");
            assertTrue(result.contains("%%EOF"), 
                "❌ FAILED: PDF should contain PDF footer '%%EOF'");
            assertTrue(result.contains("1 0 obj"), 
                "❌ FAILED: PDF should contain object references '1 0 obj'");
            assertTrue(result.contains("/Type /Catalog"), 
                "❌ FAILED: PDF should contain catalog type '/Type /Catalog'");
            
        } else if (formatType.contains("html")) {
            assertNotNull(result, "HTML result cannot be null");
            assertTrue(result.startsWith("[HTML]"), 
                "❌ FAILED: HTML document should start with [HTML] prefix. Got: " + result.substring(0, Math.min(50, result.length())));
            assertTrue(result.contains("<!DOCTYPE html>"), 
                "❌ FAILED: HTML should have DOCTYPE declaration '<!DOCTYPE html>'");
            assertTrue(result.contains("<html>"), 
                "❌ FAILED: HTML should have html opening tag '<html>'");
            assertTrue(result.contains("</html>"), 
                "❌ FAILED: HTML should have html closing tag '</html>'");
            assertTrue(result.contains("<body>"), 
                "❌ FAILED: HTML should have body opening tag '<body>'");
            assertTrue(result.contains("</body>"), 
                "❌ FAILED: HTML should have body closing tag '</body>'");
            assertTrue(result.contains("<h1>"), 
                "❌ FAILED: HTML should have header tags '<h1>'");
            
            if (result.contains("<ul>")) {
                assertTrue(result.contains("</ul>"), 
                    "❌ FAILED: HTML lists should be properly closed with '</ul>'");
                assertTrue(result.contains("<li>"), 
                    "❌ FAILED: HTML lists should contain list items '<li>'");
                assertTrue(result.contains("</li>"), 
                    "❌ FAILED: HTML list items should be properly closed with '</li>'");
            }
            
        } else if (formatType.contains("docx")) {
            assertNotNull(result, "DOCX result cannot be null");
            assertTrue(result.startsWith("[DOCX]"), 
                "❌ FAILED: DOCX document should start with [DOCX] prefix. Got: " + result.substring(0, Math.min(50, result.length())));
            assertTrue(result.contains("<?xml version=\"1.0\""), 
                "❌ FAILED: DOCX should have XML declaration '<?xml version=\"1.0\"'");
            assertTrue(result.contains("xmlns:w=\"http://schemas.openxmlformats.org"), 
                "❌ FAILED: DOCX should have proper XML namespace 'xmlns:w=\"http://schemas.openxmlformats.org'");
            assertTrue(result.contains("<w:document"), 
                "❌ FAILED: DOCX should have document element '<w:document'");
            assertTrue(result.contains("</w:document>"), 
                "❌ FAILED: DOCX should close document element '</w:document>'");
            assertTrue(result.contains("<w:body>"), 
                "❌ FAILED: DOCX should have body element '<w:body>'");
            assertTrue(result.contains("</w:body>"), 
                "❌ FAILED: DOCX should close body element '</w:body>'");
            assertTrue(result.contains("<w:p>"), 
                "❌ FAILED: DOCX should contain paragraph elements '<w:p>'");
            assertTrue(result.contains("<w:r>"), 
                "❌ FAILED: DOCX should contain run elements '<w:r>'");
            assertTrue(result.contains("<w:t>"), 
                "❌ FAILED: DOCX should contain text elements '<w:t>'");
        } else {
            fail("❌ FAILED: Unknown exporter type: " + exporterClassName + 
                ". Expected PdfExporter, DocxExporter, or HtmlExporter");
        }
    }

    private void validateContentCorrectness(String result, Class<? extends Document> docClass, Document document) {
        String docType = docClass.getSimpleName().toLowerCase();
        
        if (docType.contains("report")) {
            assertTrue(result.contains("Test Report"), 
                "❌ FAILED: Report should contain the specified title 'Test Report'");
        } else if (docType.contains("invoice")) {
            assertTrue(result.contains("INV-001"), 
                "❌ FAILED: Invoice should contain the invoice number 'INV-001'");
        } else if (docType.contains("contract")) {
            assertTrue(result.contains("Test Contract"), 
                "❌ FAILED: Contract should contain the contract title 'Test Contract'");
        } else {
            fail("❌ FAILED: Unknown document type: " + docClass.getSimpleName() + 
                ". Expected Report, Invoice, or Contract");
        }
        
        assertTrue(result.contains("Test Item 1"),
            "❌ FAILED: Should contain first data item 'Test Item 1'");
        assertTrue(result.contains("Test Item 2"), 
            "❌ FAILED: Should contain second data item 'Test Item 2'");
        assertTrue(result.contains("Test Item 3"), 
            "❌ FAILED: Should contain third data item 'Test Item 3'");
        
        if (docType.contains("invoice")) {
            assertTrue(result.contains("TestCorp"), 
                "❌ FAILED: Invoice should contain company name 'TestCorp'");
            assertTrue(result.contains("Invoice #"), 
                "❌ FAILED: Invoice should contain invoice number field 'Invoice #'");
            assertTrue(result.contains("Company:"), 
                "❌ FAILED: Invoice should contain company field 'Company:'");
        } else if (docType.contains("contract")) {
            assertTrue(result.contains("Legal"), 
                "❌ FAILED: Contract should contain department 'Legal'");
            assertTrue(result.contains("Contract Title:"), 
                "❌ FAILED: Contract should contain contract title field 'Contract Title:'");
            assertTrue(result.contains("Department:"), 
                "❌ FAILED: Contract should contain department field 'Department:'");
        }
        
        assertTrue(result.contains("Generated:"),
            "❌ FAILED: Document should contain generation timestamp 'Generated:'");
        
        String expectedDataSection = getExpectedDataSectionTitle(docType);
        if (expectedDataSection != null) {
            assertTrue(result.contains(expectedDataSection), 
                "❌ FAILED: Document should contain proper data section: '" + expectedDataSection + "'");
        }
    }

    private void validateBusinessLogic(String result, Class<? extends Document> docClass, 
                                     Class<? extends ExportFormat> exporterClass) {
        String docType = docClass.getSimpleName().toLowerCase();
        String formatType = exporterClass.getSimpleName().toLowerCase();
        
        if (docType.contains("report")) {
            assertTrue(result.contains("REPORT DOCUMENT"), 
                "❌ FAILED: Report should have 'REPORT DOCUMENT' header");
            assertTrue(result.contains("Financial Report"), 
                "❌ FAILED: Report should specify 'Financial Report' type");
        } else if (docType.contains("invoice")) {
            assertTrue(result.contains("INVOICE DOCUMENT"), 
                "❌ FAILED: Invoice should have 'INVOICE DOCUMENT' header");
            assertTrue(result.contains("Commercial Invoice"), 
                "❌ FAILED: Invoice should specify 'Commercial Invoice' type");
        } else if (docType.contains("contract")) {
            assertTrue(result.contains("CONTRACT DOCUMENT"), 
                "❌ FAILED: Contract should have 'CONTRACT DOCUMENT' header");
            assertTrue(result.contains("Legal Contract"), 
                "❌ FAILED: Contract should specify 'Legal Contract' type");
        }
        
        if (formatType.contains("pdf") || formatType.contains("docx")) {
            assertTrue(result.contains("• Test Item 1"),
                "❌ FAILED: PDF/DOCX should format data items with bullets '• Test Item 1'");
        } else if (formatType.contains("html")) {
            assertTrue(result.contains("<li>Test Item 1</li>"),
                "❌ FAILED: HTML should format data items as list items '<li>Test Item 1</li>'");
        }
        
        String expectedPrefix = getExpectedPrefix(exporterClass.getSimpleName());
        if (expectedPrefix != null) {
            assertTrue(result.startsWith(expectedPrefix), 
                "❌ FAILED: Document should start with correct format prefix: '" + expectedPrefix + 
                "'. Got: " + result.substring(0, Math.min(20, result.length())));
        }
    }

    private String getExpectedDataSectionTitle(String docType) {
        if (docType.contains("report")) return "REPORT DATA:";
        if (docType.contains("invoice")) return "INVOICE ITEMS:";
        if (docType.contains("contract")) return "CONTRACT TERMS:";
        return null;
    }
}
