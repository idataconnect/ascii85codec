/*
 * Copyright (c) 2009-2010, i Data Connect!
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
