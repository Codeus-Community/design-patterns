# 🌉 Bridge Design Pattern Exercise

> **Master the Bridge Pattern through hands-on refactoring of a real-world document management system**

## 📋 Overview

This exercise demonstrates the **Bridge Design Pattern** by solving the classic **combinatorial explosion problem** in a document management system. You'll transform tightly coupled, unmaintainable code into an elegant, extensible Bridge pattern implementation.

## 🎯 Learning Objectives

- ✅ **Understand combinatorial explosion** and why it's problematic
- ✅ **Master Bridge pattern structure** (Abstraction + Implementation)
- ✅ **Practice separation of concerns** in real-world scenarios

## 🚨 The Problem: Combinatorial Explosion

### Without Bridge Pattern
```
Document Types × Export Formats = Total Classes

3 types × 3 formats = 9 classes
5 types × 4 formats = 20 classes
10 types × 6 formats = 60 classes
```

### Current Situation (Dirty Implementation)
```
📁 dirty/
├── PdfReport.java     ┐
├── DocxReport.java    ├─ Report implementations
├── HtmlReport.java    ┘
├── PdfInvoice.java    ┐
├── DocxInvoice.java   ├─ Invoice implementations  
├── HtmlInvoice.java   ┘
├── PdfContract.java   ┐
├── DocxContract.java  ├─ Contract implementations
└── HtmlContract.java  ┘
```

**Problems:**
- 🔄 **Code duplication** across similar classes
- 🔧 **Hard to maintain** - changes require multiple file edits
- 📈 **Exponential growth** - adding formats/types multiplies classes
- 🐛 **Bug multiplication** - same logic errors repeated everywhere

# Hints

## 🌉 The Solution: Bridge Pattern

### Bridge Pattern Structure
```
        Abstraction                    Implementation
    ┌─────────────────┐              ┌─────────────────┐
    │    Document     │◇─────────────│  ExportFormat   │
    │   (abstract)    │              │   (interface)   │
    └─────────────────┘              └─────────────────┘
            △                                 △
            │                                 │
    ┌───────┴──┐────────────┐        ┌────────┴─────┐───────────┐
    │          │            │        │              │           │
┌───▽───┐ ┌────▽────┐ ┌─────▽─────┐ ┌▽──────┐ ┌─────▽──┐ ┌──────▽──┐
│Report │ │Invoice  │ │ Contract  │ │  PDF  │ │  DOCX  │ │  HTML   │
│       │ │         │ │           │ │Export │ │ Export │ │ Export  │
└───────┘ └─────────┘ └───────────┘ └───────┘ └────────┘ └─────────┘
```

### After Bridge Pattern
```
📁 clean/
├── Document.java      ← Abstract base (3 concrete classes)
├── ExportFormat.java  ← Interface (3 implementations)
└── [Your implementations...]

Total: 3 + 3 = 6 classes (instead of 9!)
```

**Benefits:**
- ✨ **Separation of concerns** - document logic separate from format logic
- 🔧 **Easy maintenance** - changes isolated to specific areas
- 📊 **Linear growth** - adding types/formats adds classes linearly
- 🎯 **Single responsibility** - each class has one reason to change

## 📁 Project Structure

### 🔴 `dirty/` - The Problem (Study This First)
```
📂 src/main/java/.../bridge/dirty/
├── 📄 App.java              ← Main class showing the problem
├── 📄 PdfReport.java        ┐
├── 📄 DocxReport.java       ├─ 9 tightly coupled classes
├── 📄 HtmlReport.java       │  demonstrating combinatorial
├── 📄 PdfInvoice.java       │  explosion problem
├── 📄 DocxInvoice.java      │
├── 📄 HtmlInvoice.java      │
├── 📄 PdfContract.java      │
├── 📄 DocxContract.java     │
└── 📄 HtmlContract.java     ┘
```

### 🟡 `clean/` - Your Workspace (Implement Here)
```
📂 src/main/java/.../bridge/clean/
├── 📄 Document.java         ← Abstract base class ✅ PROVIDED
├── 📄 ExportFormat.java     ← Interface ✅ PROVIDED
├── 📄 Report.java           ← Your implementation ❌ TODO
├── 📄 Invoice.java          ← Your implementation ❌ TODO
├── 📄 Contract.java         ← Your implementation ❌ TODO
├── 📄 PdfExporter.java      ← Your implementation ❌ TODO
├── 📄 DocxExporter.java     ← Your implementation ❌ TODO
└── 📄 HtmlExporter.java     ← Your implementation ❌ TODO
```

## 🚀 Your Task

### Step 1: Study the Problem
```bash
# Run the dirty implementation
mvn exec:java -Dexec.mainClass="org.codeus.design_patterns.bridge.dirty.App"

# Run existing tests
mvn test
```

### Step 2: Implement Bridge Pattern
Create these classes in `clean/` package:

**Document Classes (extend Document):**
- `Report.java` - Financial report implementation
- `Invoice.java` - Commercial invoice implementation  
- `Contract.java` - Legal contract implementation

**Exporter Classes (implement ExportFormat):**
- `PdfExporter.java` - PDF format implementation
- `DocxExporter.java` - DOCX format implementation
- `HtmlExporter.java` - HTML format implementation

### Step 3: Test Your Implementation
```bash
# Run dynamic tests (discovers your classes automatically)
mvn test -Dtest=DynamicBridgeTest
```

### Step 4: Study Reference Solution
Compare your implementation with `completed` branch.

## 🎨 Bridge Pattern Implementation Guide

### 1. Abstraction Layer (Document)
```java
public abstract class Document {
    protected ExportFormat exportFormat;  // Bridge to implementation
    
    public Document(ExportFormat exportFormat) {
        this.exportFormat = exportFormat;
    }
    
    public String generate() {
        // Use bridge to delegate format operations
        return exportFormat.startDocument() + 
               generateContent() + 
               exportFormat.endDocument();
    }
    
    protected abstract String generateContent();
}
```

### 2. Implementation Interface (ExportFormat)
```java
public interface ExportFormat {
    String startDocument();
    String endDocument();
    String formatHeader(String title);
    String formatDataItem(String item);
    // ... other format-specific methods
}
```

### 3. Refined Abstractions (Report, Invoice, Contract)
```java
public class Report extends Document {
    private String title;
    private List<String> data;
    
    public Report(ExportFormat exportFormat, String title, List<String> data) {
        super(exportFormat);  // Bridge connection
        this.title = title;
        this.data = data;
    }
    
    @Override
    protected String generateContent() {
        // Document-specific logic, format-agnostic
        StringBuilder content = new StringBuilder();
        content.append(exportFormat.formatHeader(title));
        for (String item : data) {
            content.append(exportFormat.formatDataItem(item));
        }
        return content.toString();
    }
}
```

### 4. Concrete Implementations (PdfExporter, DocxExporter, HtmlExporter)
```java
public class PdfExporter implements ExportFormat {
    @Override
    public String startDocument() {
        return "%%PDF-1.4\n1 0 obj\n<<\n/Type /Catalog\n>>\nendobj\n\n";
    }
    
    @Override
    public String formatDataItem(String item) {
        return "• " + item + "\n";  // PDF-specific formatting
    }
    
    // ... other PDF-specific implementations
}
```

## 🏆 Success Criteria

Your implementation should:

1. **✅ Eliminate code duplication** - No repeated logic between classes
2. **✅ Enable easy extension** - Add new documents/formats without modifying existing code
3. **✅ Maintain separation** - Document logic separate from format logic
4. **✅ Pass all tests** - Both unit tests and comprehensive validation
5. **✅ Follow SOLID principles** - Single responsibility, open/closed, etc.

---

**🎯 Remember**: The goal isn't just to make it work, but to make it **maintainable**, **extensible**, and **elegant**!

Good luck! 🚀