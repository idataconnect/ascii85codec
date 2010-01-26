/*
 * Copyright 2010 i Data Connect!
 */

package com.idataconnect.lib.ascii85codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import junit.framework.TestCase;

/**
 * Tests encoding and subsequent decoding of data using both the input stream
 * and output stream implementations.
 * @author Ben Upsavs
 */
public class EncodeDecodeTest extends TestCase {

    /**
     * Test encoding and decoding binary.
     */
    public void testEncodeBinary() throws Exception {
        System.out.println("encode binary");
        byte[] toEncode = new byte[] {-100, -5, -10, -20, -4, -50};

        ByteArrayOutputStream boas = new ByteArrayOutputStream(1024);
        Ascii85OutputStream os = new Ascii85OutputStream(boas);
        os.write(toEncode);
        os.close();
        boas.close();

        byte[] encodedBytes = boas.toByteArray();

        String encoded = new String(encodedBytes, "US-ASCII");
        assertEquals("<~SGW$Hr6>~>", encoded.replace("\r", "").replace("\n", ""));

        Ascii85InputStream is = new Ascii85InputStream(
                new ByteArrayInputStream(encodedBytes));
        boas.reset();
        int b;
        while ((b = is.read()) != -1)
            boas.write(b);

        boas.close();
        is.close();

        assertTrue(Arrays.equals(boas.toByteArray(), toEncode));
    }
}
