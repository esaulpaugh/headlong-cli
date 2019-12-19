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

import com.esaulpaugh.headlong.abi.ValidationException;
import com.esaulpaugh.headlong.util.FastHex;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    @Test
    public void test() throws ParseException, ValidationException {

        String[] n = new String[] { "-n", "(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] a = new String[] { "-a", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] sf = new String[] { "-f", "nam(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
        String[] af = new String[] { "-af", "nam", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };

        final String tupleEncoding = "00000000000000000000000000000000000000000000005d92d2a10d4e107b1d";
        assertEquals(tupleEncoding, encode(n));
        assertEquals(tupleEncoding, encode(a));

        final String functionCall = "62279c72" + tupleEncoding;
        assertEquals(functionCall, encode(sf));
        assertEquals(functionCall, encode(af));
    }

    private static String encode(String[] args) throws ParseException, ValidationException {
        return FastHex.encodeToString(Main.encodeResult(args).array());
    }
}
