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
import com.esaulpaugh.headlong.exception.DecodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private static final String SIGNATURE = "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])";

    private static final String VALUES_SERIALIZATION = "9PPymC8zsl7PseasaOghrk-bzio0DeQDhxJpC5gvM7Jez7HmrGjoIa5Pm84qNA3kA4cSaQuYLzOyXs-x5qxo6CGuT5vOKjQN5AOHEmkLwsF61pUA_wDuAd0CzAPK_rq-mQaIB3cIZgmKAP3________-BAfJyMcJhYT____1xxGFhP___-2F_KUnkjvLfomI_________4IKAYZmYXJvdXTCAQHHAxSE____-sWE_____g";

    private static final String VALUES_ABI =
                    "0000000000000000000000000000000000000000000000000000000000000220" +
                    "2f33b25ecfb1e6ac68e821ae4f9bce2a340de4038712690b0000000000000000" +
                    "00000000000000000000000000000000000000000000000000000000000002c0" +
                    "0000000000000000000000000000000000000000000000000000000000000340" +
                    "0000000000000000000000000000000000000000000000fdfffffffffffffe04" +
                    "0000000000000000000000000000000000000000000000000000000000000007" +
                    "0000000000000000000000000000000000000000000000000000000000000380" +
                    "0000000000000000000000000000000000000000000000000000000000000400" +
                    "000000000000000000000000000000000000000000000000000000fca527923b" +
                    "0000000000000000000000000000000000000000000000000000000000000460" +
                    "000000000000000000000000000000000000000000000000000000000000000a" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "00000000000000000000000000000000000000000000000000000000000004c0" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "0000000000000000000000000000000000000000000000000000000000000500" +
                    "00000000000000000000000000000000000000000000000000000000fffffffe" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "0000000000000000000000000000000000000000000000000000000000000020" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "2f33b25ecfb1e6ac68e821ae4f9bce2a340de4038712690b0000000000000000" +
                    "2f33b25ecfb1e6ac68e821ae4f9bce2a340de4038712690b0000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000020" +
                    "0000000000000000000000000000000000000000000000000000000000000020" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "7a00000000000000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "000000000000000000000000ff00ee01dd02cc03cafebabe9906880777086609" +
                    "0000000000000000000000000000000000000000000000000000000000000020" +
                    "0000000000000000000000000000000000000000000000000000000000000001" +
                    "0000000000000000000000000000000000000000000000000000000000000009" +
                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff5" +
                    "0000000000000000000000000000000000000000000000000000000000000002" +
                    "0000000000000000000000000000000000000000000000000000000000000011" +
                    "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffed" +
                    "0000000000000000000000000000000000000000000000000000000000000002" +
                    "000000000000000000000000000000000000000000000000000000000000007e" +
                    "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff82" +
                    "0000000000000000000000000000000000000000000000000000000000000006" +
                    "6661726f75740000000000000000000000000000000000000000000000000000" +
                    "0000000000000000000000000000000000000000000000000000000000000003" +
                    "0000000000000000000000000000000000000000000000000000000000000003" +
                    "0000000000000000000000000000000000000000000000000000000000000014" +
                    "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffa";

    @Test
    public void testEncode() throws ABIException, DecodeException {
        String[] n = new String[] { "-e", "-n", SIGNATURE, VALUES_SERIALIZATION };
//        String[] a = new String[] { "-e", "-a", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
//        String[] sf = new String[] { "-e", "-f", "nam(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
//        String[] af = new String[] { "-e", "-af", "nam", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };

        assertEquals(VALUES_ABI, Main.encodeABI(n));
//        assertEquals(tupleEncoding, Main.encodeABI(a));
//
//        final String functionCall = "62279c72" + tupleEncoding;
//        assertEquals(functionCall, Main.encodeABI(sf));
//        assertEquals(functionCall, Main.encodeABI(af));
    }

    @Test
    public void testDecode() throws ABIException {
        String[] n = new String[] { "-d", "-n", SIGNATURE, VALUES_ABI };
//        String[] a = new String[] { "-d", "-a", "[\"uint112\"]", VALUES_ABI };
        String[] sf = new String[] { "-d", "-f", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };
//        String[] af = new String[] { "-d", "-af", "nam", "[\"uint112\"]",  VALUES_ABI };

        final String values = "9PPymC8zsl7PseasaOghrk-bzio0DeQDhxJpC5gvM7Jez7HmrGjoIa5Pm84qNA3kA4cSaQuYLzOyXs-x5qxo6CGuT5vOKjQN5AOHEmkLwsF61pUA_wDuAd0CzAPK_rq-mQaIB3cIZgmKAP3________-BAfJyMcJhYT____1xxGFhP___-2F_KUnkjvLfomI_________4IKAYZmYXJvdXTCAQHHAxSE____-sWE_____g";
        assertEquals(values, Main.decodeABI(n));
//        assertEquals(values, Main.decodeABI(a));

        assertEquals(values, Main.decodeABI(sf));
//        assertEquals(values, Main.decodeABI(af));
    }
}
