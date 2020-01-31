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
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.exception.DecodeException;
import com.esaulpaugh.headlong.util.Strings;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private static final String SIGNATURE = "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])";

    private static final String MACHINE_SERIALIZATION = "9PPymOKQlGDhoGO30wIfruK6xhrl0hjfCu8AHZjikJRg4aBjt9MCH67iusYa5dIY3wrvAB2Y4pCUYOGgY7fTAh-u4rrGGuXSGN8K7wAdwsF61pUA_wDuAd0CzAPK_rq-mQaIB3cIZgmKAP3________-BMEHysnIwQnFhP____XIwRHFhP___-2F_KUnkjvMwX7JiP________-CwQoBhmZhcm91dMIBAccDFIT____6xYT____-";

    private static final String VALUES_ABI =
                            "0000000000000000000000000000000000000000000000000000000000000220" +
                            "e2909460e1a063b7d3021faee2bac61ae5d218df0aef001d0000000000000000" +
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
                            "e2909460e1a063b7d3021faee2bac61ae5d218df0aef001d0000000000000000" +
                            "e2909460e1a063b7d3021faee2bac61ae5d218df0aef001d0000000000000000" +
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
        String[] n = new String[] { "-e", "-n", SIGNATURE, MACHINE_SERIALIZATION };
//        String[] a = new String[] { "-e", "-a", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
//        String[] sf = new String[] { "-e", "-f", "nam(uint112)", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };
//        String[] af = new String[] { "-e", "-af", "nam", "[\"uint112\"]", "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]" };

        System.out.println(TupleType.format(Strings.decode(Main.encodeABI(n))));

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

        assertEquals(MACHINE_SERIALIZATION, Main.decodeABI(n));
//        assertEquals(values, Main.decodeABI(a));

        assertEquals(MACHINE_SERIALIZATION, Main.decodeABI(sf));
//        assertEquals(values, Main.decodeABI(af));
    }

    @Test
    public void testSerial() throws ABIException, DecodeException {

        TupleType tt = TupleType.parse("(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])");

        byte[] func = new byte[24];
        new Random(System.currentTimeMillis() + System.nanoTime()).nextBytes(func);

        Object[] argsIn = new Object[] {
                new byte[][][][] { new byte[][][] { new byte[][] { func, func } } },
                func,
                new String[][] { new String[] { "z" } },
                new BigInteger[] { new BigInteger("ff00ee01dd02cc03cafebabe9906880777086609", 16) },
                BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Byte.MAX_VALUE << 2)),
                new Tuple(7),
                new Tuple[][][] { new Tuple[][] { new Tuple[] { new Tuple(9), new Tuple(-11) } } },
                new Tuple[] { new Tuple(17), new Tuple(-19) },
                Long.MAX_VALUE / 8_500_000,
                new Tuple[] { new Tuple((long) 0x7e), new Tuple((long) -0x7e) },
                new Tuple(BigInteger.TEN),
                true,
                "farout",
                new boolean[] { true, true },
                new int[] { 3, 20, -6 },
                new long[] { Integer.MAX_VALUE * 2L }
        };

        Tuple tuple = new Tuple(argsIn);

        String str = SuperSerial.toMachine(tt, tuple);

        System.out.println(str);

        Tuple deserial = SuperSerial.fromMachine(tt, str);

        assertEquals(tuple, deserial);
    }
}
