/*
   Copyright 2019 Evan Saulpaugh

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.esaulpaugh.headlong.cli;

import com.esaulpaugh.headlong.abi.ABIException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    public void testEncode() throws ABIException {
        String[] n = new String[] { "-e", "-n", "(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] a = new String[] { "-e", "-a", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] sf = new String[] { "-e", "-f", "nam(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] af = new String[] { "-e", "-af", "nam", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };

        final String tupleEncoding = "00000000000000000000000000000000000000000000005d92d2a10d4e107b1d";
        assertEquals(tupleEncoding, Main.encodeABI(n));
        assertEquals(tupleEncoding, Main.encodeABI(a));

        final String functionCall = "62279c72" + tupleEncoding;
        assertEquals(functionCall, Main.encodeABI(sf));
        assertEquals(functionCall, Main.encodeABI(af));
    }

    @Test
    public void testDecode() throws ABIException {
        String[] n = new String[] { "-d", "-n", "(uint112)", "00000000000000000000000000000000000000000000005d92d2a10d4e107b1d" };
        String[] a = new String[] { "-d", "-a", "[\"uint112\"]", "00000000000000000000000000000000000000000000005d92d2a10d4e107b1d" };
        String[] sf = new String[] { "-d", "-f", "nam(uint112)", "62279c7200000000000000000000000000000000000000000000005d92d2a10d4e107b1d" };
        String[] af = new String[] { "-d", "-af", "nam", "[\"uint112\"]",  "62279c7200000000000000000000000000000000000000000000005d92d2a10d4e107b1d" };

        final String values = "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]";
        assertEquals(values, Main.decodeABI(n));
        assertEquals(values, Main.decodeABI(a));

        assertEquals(values, Main.decodeABI(sf));
        assertEquals(values, Main.decodeABI(af));
    }
}
