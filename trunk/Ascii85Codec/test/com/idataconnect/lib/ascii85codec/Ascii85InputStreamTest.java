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
import junit.framework.TestCase;

/**
 * Junit 3.x test cases for the input stream (decoder) class.
 * @author Ben Upsavs
 */
public class Ascii85InputStreamTest extends TestCase {

    protected String phrase = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
    protected byte[] test1;
    protected byte[] test2;
    protected byte[] test3;

    public Ascii85InputStreamTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        test1 = ("<~9jqo^BlbD-BleB1DJ+*+F(f,q/0JhKF<GL>Cj@.4Gp$d7F!,L7@<6@)/0JDEF<G%<+EV:2F!,\n"
                + "O<DJ+*.@<*K0@<6L(Df-\\0Ec5e;DffZ(EZee.Bl.9pF\"AGXBPCsi+DGm>@3BB/F*&OCAfu2/AKY\n"
                + "i(DIb:@FD,*)+C]U=@3BN#EcYf8ATD3s@q?d$AftVqCh[NqF<G:8+EV:.+Cf>-FD5W8ARlolDIa\n"
                + "l(DId<j@<?3r@:F%a+D58'ATD4$Bl@l3De:,-DJs`8ARoFb/0JMK@qB4^F!,R<AKZ&-DfTqBG%G\n"
                + ">uD.RTpAKYo'+CT/5+Cei#DII?(E,9)oF*2M7/c~>\n").getBytes("US-ASCII");
        test2 = new byte[test1.length + 1];
        System.arraycopy(test1, 0, test2, 0, 7);
        test2[7] = 'z';
        System.arraycopy(test1, 7, test2, 8, test1.length - 7);

        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test decoding.
     */
    public void testDecode() throws Exception {
        System.out.println("decode");

        Ascii85InputStream is = new Ascii85InputStream(new ByteArrayInputStream(test1));
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        int ch;
        while ((ch = is.read()) != -1)
            os.write(ch);
        is.close();
        os.close();

        assertEquals(phrase, new String(os.toByteArray(), "US-ASCII"));
    }

    /**
     * Test compression feature.
     */
    public void testCompression() throws Exception {
        System.out.println("compression - nulls");

        Ascii85InputStream is = new Ascii85InputStream(new ByteArrayInputStream(test2));
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        int ch;
        while ((ch = is.read()) != -1)
            os.write(ch);
        is.close();
        os.close();

        byte[] bytes = os.toByteArray();

        assertEquals(' ', bytes[3]);
        assertEquals(0, bytes[4]);
        assertEquals(0, bytes[5]);
        assertEquals(0, bytes[6]);
        assertEquals(0, bytes[7]);
        assertEquals('i', bytes[8]);

        System.out.println("compression - spaces");

        test2[7] = 'y'; // Change z
        is = new Ascii85InputStream(new ByteArrayInputStream(test2));
        os = new ByteArrayOutputStream(1024);
        while ((ch = is.read()) != -1)
            os.write(ch);
        is.close();
        os.close();

        bytes = os.toByteArray();

        assertEquals(' ', bytes[3]); // the space after the 'n'
        assertEquals(' ', bytes[4]);
        assertEquals(' ', bytes[5]);
        assertEquals(' ', bytes[6]);
        assertEquals(' ', bytes[7]);
        assertEquals('i', bytes[8]);
    }

    /**
     * Test of mark method, of class Ascii85InputStream.
     */
    public void testMark() throws Exception {
        System.out.println("mark");

        Ascii85InputStream is = new Ascii85InputStream(new ByteArrayInputStream(test1));
        assertEquals('M', is.read());
        is.mark(5); // mark before 'a'
        is.read(); // a - here it is
        is.read(); // n
        is.reset(); // reset to before 'a'
        assertEquals('a', is.read()); // here it is again
        is.close();
    }

    /**
     * Test of skip method, of class Ascii85InputStream.
     */
    public void testSkip() throws Exception {
        System.out.println("skip");

        Ascii85InputStream is = new Ascii85InputStream(new ByteArrayInputStream(test1));
        assertEquals('M', is.read());
        is.read(); // a
        is.read(); // n
        is.skip(4); // SP, i, s, SP
        assertEquals('d', is.read());
        is.close();
    }
}
