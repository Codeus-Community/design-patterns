package org.codeus.design_patterns.decorator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class HardcodedFileDataSource implements DataSource {
    private final String name;
    private final int compLevel = 6;

    public HardcodedFileDataSource(String name) {
        this.name = name;
    }

    public void writeData(String data) {
        String compressed = compress(data);
        String encrypted = encode(compressed);
        File file = new File(name);
        try (OutputStream fos = new FileOutputStream(file)) {
            fos.write(encrypted.getBytes(), 0, encrypted.length());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String readData() {
        char[] buffer;
        File file = new File(name);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File does not exist or is empty: " + name);
            return "";
        }
        try (FileReader reader = new FileReader(file)) {
            buffer = new char[(int) file.length()];
            reader.read(buffer);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return "";
        }
        if (buffer == null) {
            return "";
        }
        String encrypted = new String(buffer);
        String compressed = decode(encrypted);
        return decompress(compressed);
    }

    private String compress(String stringData) {
        byte[] data = stringData.getBytes();
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            DeflaterOutputStream dos = new DeflaterOutputStream(bout, new Deflater(compLevel));
            dos.write(data);
            dos.close();
            bout.close();
            return Base64.getEncoder().encodeToString(bout.toByteArray());
        } catch (IOException ex) {
            return null;
        }
    }

    private String decompress(String stringData) {
        byte[] data = Base64.getDecoder().decode(stringData);
        try {
            InputStream in = new ByteArrayInputStream(data);
            InflaterInputStream iin = new InflaterInputStream(in);
            ByteArrayOutputStream bout = new ByteArrayOutputStream(512);
            int b;
            while ((b = iin.read()) != -1) {
                bout.write(b);
            }
            return bout.toString();
        } catch (IOException ex) {
            return null;
        }
    }

    private String encode(String data) {
        byte[] result = data.getBytes();
        for (int i = 0; i < result.length; i++) {
            result[i] += (byte) 1;
        }
        return Base64.getEncoder().encodeToString(result);
    }

    private String decode(String data) {
        byte[] result = Base64.getDecoder().decode(data);
        for (int i = 0; i < result.length; i++) {
            result[i] -= (byte) 1;
        }
        return new String(result);
    }
}
