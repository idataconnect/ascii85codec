/*
 * Copyright (c) 2009, i Data Connect!
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

import java.io.ByteArrayOutputStream;
import junit.framework.TestCase;

/**
 * JUnit 3.x test cases for the output stream (encoder) class.
 * @author Ben Upsavs
 */
public class Ascii85OutputStreamTest extends TestCase
{
    protected String phrase = "Man is distinguished, not only by his reason, but by this singular passion from other animals, which is a lust of the mind, that by a perseverance of delight in the continued and indefatigable generation of knowledge, exceeds the short vehemence of any carnal pleasure.";
    protected byte[] test1;

    public Ascii85OutputStreamTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws Exception
    {
        test1 = ("<~9jqo^BlbD-BleB1DJ+*+F(f,q/0JhKF<GL>Cj@.4Gp$d7F!,L7@<6@)/0JDEF<G%<+EV:2F!,\n" +
            "O<DJ+*.@<*K0@<6L(Df-\\0Ec5e;DffZ(EZee.Bl.9pF\"AGXBPCsi+DGm>@3BB/F*&OCAfu2/AKY\n" +
            "i(DIb:@FD,*)+C]U=@3BN#EcYf8ATD3s@q?d$AftVqCh[NqF<G:8+EV:.+Cf>-FD5W8ARlolDIa\n" +
            "l(DId<j@<?3r@:F%a+D58'ATD4$Bl@l3De:,-DJs`8ARoFb/0JMK@qB4^F!,R<AKZ&-DfTqBG%G" +
            ">uD.RTpAKYo'+CT/5+Cei#DII?(E,9)oF*2M7/c~>\n").getBytes("US-ASCII");

        super.setUp();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test encoding.
     */
    public void testEncode() throws Exception
    {
        System.out.println("encode");

        ByteArrayOutputStream boas = new ByteArrayOutputStream(1024);
        Ascii85OutputStream os = new Ascii85OutputStream(boas);
        os.write(phrase.getBytes("US-ASCII"));
        os.close();

        byte[] encoded = boas.toByteArray();

        // compare input and output while ignoring whitespace.
        search:
        for (int inPos = 0, outPos = 0; ; )
        {
            char inChar, outChar;
            do
            {
                if (inPos == test1.length)
                    break search;
                inChar = (char)test1[inPos++];
            }
            while (Character.isWhitespace(inChar));
            do
            {
                if (outPos == encoded.length)
                    fail("end of encoded data received while testing for equality");
                outChar = (char)encoded[outPos++];
            }
            while (Character.isWhitespace(outChar));
            if (inChar != outChar)
                fail("input data at " + inPos + " is " + inChar + " != output data at " + outPos + " is " + outChar);
        }
    }

    /**
     * Test encoding multiple ascii85 blocks to one output stream.
     */
    public void testEncodeMultiple() throws Exception
    {
        System.out.println("encodeMultiple");

        ByteArrayOutputStream boas = new ByteArrayOutputStream(1024);
        Ascii85OutputStream os = new Ascii85OutputStream(boas);
        os.write(phrase.getBytes("US-ASCII"));
        os.flush();
        os.write(phrase.getBytes("US-ASCII"));
        os.close();

        byte[] encoded = boas.toByteArray();

        int inPos, outPos;
        search:
        for (inPos = 0, outPos = 0; ; )
        {
            char inChar, outChar;
            do
            {
                if (inPos == test1.length)
                    break search;
                inChar = (char)test1[inPos++];
            }
            while (Character.isWhitespace(inChar));
            do
            {
                if (outPos == encoded.length)
                    fail("end of encoded data received while testing for equality");
                outChar = (char)encoded[outPos++];
            }
            while (Character.isWhitespace(outChar));
            if (inChar != outChar)
                fail("input data at " + inPos + " is " + inChar + " != output data at " + outPos + " is " + outChar);
        }

        search2:
        for (inPos = 0; ;)
        {
            char inChar, outChar;
            do
            {
                if (inPos == test1.length)
                    break search2;
                inChar = (char)test1[inPos++];
            }
            while (Character.isWhitespace(inChar));
            do
            {
                if (outPos == encoded.length)
                    fail("end of encoded data received while testing for equality");
                outChar = (char)encoded[outPos++];
            }
            while (Character.isWhitespace(outChar));
            if (inChar != outChar)
                fail("input data at " + inPos + " is " + inChar + " != output data at " + outPos + " is " + outChar);
        }
    }
}
