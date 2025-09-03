package org.codeus.design_patterns.decorator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DataSourceDecoratorTest {
    private static final String FILE = "src/test/java/org/codeus/design_patterns/decorator/out/OutputDemo.txt";

    @BeforeEach
    void cleanFile() {
        new File(FILE).delete();
    }

    @Test
    void testWriteRead() {
        String input = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
        DataSource ds = new CompressionDecorator(
            new EncryptionDecorator(
                new FileDataSource(FILE)));
        ds.writeData(input);
        String output = ds.readData();
        assertEquals(input, output);
    }

    @Test
    void testEncodedDiffersFromInput() {
        String input = "Name,Salary\nJohn Smith,100000\nSteven Jobs,912000";
        DataSource ds = new CompressionDecorator(
            new EncryptionDecorator(
                new FileDataSource(FILE)));
        ds.writeData(input);
        DataSource plain = new FileDataSource(FILE);
        String encoded = plain.readData();
        assertNotEquals(input, encoded);
    }

    @Test
    void testFileDataSourceAlone() {
        String input = "Simple data";
        DataSource ds = new FileDataSource(FILE);
        ds.writeData(input);
        String output = ds.readData();
        assertEquals(input, output, "FileDataSource should store and retrieve plain data");
    }

    @Test
    void testCompressionDecoratorAlone() {
        String input = "Compress me!";
        DataSource ds = new CompressionDecorator(new FileDataSource(FILE));
        ds.writeData(input);
        String output = ds.readData();
        assertEquals(input, output, "CompressionDecorator should compress and decompress data correctly");
        // Check that file content is not equal to input
        File file = new File(FILE);
        StringBuilder raw = new StringBuilder();
        try (java.io.FileReader reader = new java.io.FileReader(file)) {
            char[] buf = new char[(int) file.length()];
            reader.read(buf);
            raw.append(buf);
        } catch (Exception e) { fail("File read failed"); }
        assertNotEquals(input, raw.toString(), "File should contain compressed data");
    }

    @Test
    void testEncryptionDecoratorAlone() {
        String input = "Encrypt me!";
        DataSource ds = new EncryptionDecorator(new FileDataSource(FILE));
        ds.writeData(input);
        String output = ds.readData();
        assertEquals(input, output, "EncryptionDecorator should encode and decode data correctly");
        // Check that file content is not equal to input
        File file = new File(FILE);
        StringBuilder raw = new StringBuilder();
        try (java.io.FileReader reader = new java.io.FileReader(file)) {
            char[] buf = new char[(int) file.length()];
            reader.read(buf);
            raw.append(buf);
        } catch (Exception e) { fail("File read failed"); }
        assertNotEquals(input, raw.toString(), "File should contain encoded data");
    }

    @Test
    void testCompressionAndEncryptionCombined() {
        String input = "Combo test!";
        DataSource ds = new CompressionDecorator(new EncryptionDecorator(new FileDataSource(FILE)));
        ds.writeData(input);
        String output = ds.readData();
        assertEquals(input, output, "Combined decorators should work together");
        File file = new File(FILE);
        StringBuilder raw = new StringBuilder();
        try (java.io.FileReader reader = new java.io.FileReader(file)) {
            char[] buf = new char[(int) file.length()];
            reader.read(buf);
            raw.append(buf);
        } catch (Exception e) { fail("File read failed"); }
        assertNotEquals(input, raw.toString(), "File should contain transformed data");
    }
}