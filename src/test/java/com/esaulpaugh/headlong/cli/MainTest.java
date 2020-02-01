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
import com.esaulpaugh.headlong.util.SuperSerial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private static final String SIGNATURE = "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])";

    private static final String SERIALIZATION = "(\n" +
            "  { { { '191c766e29a65787b7155dd05f41292438467db93420cade', '191c766e29a65787b7155dd05f41292438467db93420cade' } } }, \n" +
            "  '191c766e29a65787b7155dd05f41292438467db93420cade', \n" +
            "  { { '7a' } }, \n" +
            "  { '00ff00ee01dd02cc03cafebabe9906880777086609' }, \n" +
            "  '00fdfffffffffffffe04', \n" +
            "  { '07' }, \n" +
            "  { { { { '09' }, { 'fffffff5' } } } }, \n" +
            "  { { '11' }, { 'ffffffed' } }, \n" +
            "  'fca527923b', \n" +
            "  { { '7e' }, { 'ffffffffffffff82' } }, \n" +
            "  { '0a' }, \n" +
            "  '01', \n" +
            "  '6661726f7574', \n" +
            "  { '01', '01' }, \n" +
            "  { '03', '14', 'fffffffa' }, \n" +
            "  { 'fffffffe' }\n" +
            ")";

    private static final String MACHINE_SERIALIZATION = "9PPymBkcdm4ppleHtxVd0F9BKSQ4Rn25NCDK3pgZHHZuKaZXh7cVXdBfQSkkOEZ9uTQgyt6YGRx2bimmV4e3FV3QX0EpJDhGfbk0IMrewsF61pUA_wDuAd0CzAPK_rq-mQaIB3cIZgmKAP3________-BMEHysnIwQnFhP____XIwRHFhP___-2F_KUnkjvMwX7JiP________-CwQoBhmZhcm91dMIBAccDFIT____6xYT____-";

    private static final String VALUES_ABI =
                                    "0000000000000000000000000000000000000000000000000000000000000220" +
                                    "191c766e29a65787b7155dd05f41292438467db93420cade0000000000000000" +
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
                                    "191c766e29a65787b7155dd05f41292438467db93420cade0000000000000000" +
                                    "191c766e29a65787b7155dd05f41292438467db93420cade0000000000000000" +
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

        String[] emn = new String[] { "-em", SIGNATURE, MACHINE_SERIALIZATION };
        String[] emf = new String[] { "-emf", "nam" + SIGNATURE, MACHINE_SERIALIZATION };

        assertEquals(VALUES_ABI, Main.eval(emn));

        final String functionCall = "9e066e5d" + VALUES_ABI;
        assertEquals(functionCall, Main.eval(emf));

        String[] en = new String[] { "-e", SIGNATURE, SERIALIZATION };
        String[] ef = new String[] { "-ef", "nam" + SIGNATURE, SERIALIZATION };

        assertEquals(VALUES_ABI, Main.eval(en));

        assertEquals(functionCall, Main.eval(ef));
    }

    @Test
    public void testDecode() throws ABIException, DecodeException {
        String[] dm = new String[] { "-dm", SIGNATURE, VALUES_ABI };
        String[] dmf = new String[] { "-dmf", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };

        assertEquals(MACHINE_SERIALIZATION, Main.eval(dm));
        assertEquals(MACHINE_SERIALIZATION, Main.eval(dmf));

        String[] d = new String[] { "-d", SIGNATURE, VALUES_ABI };
        String[] df = new String[] { "-df", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };

        assertEquals(SERIALIZATION, Main.eval(d));
        assertEquals(SERIALIZATION, Main.eval(df));
    }

    @Test
    public void testSerial() throws ABIException, DecodeException {

        TupleType tt = TupleType.parse("(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])");

        byte[] func = Strings.decode("191c766e29a65787b7155dd05f41292438467db93420cade");

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

        String str = SuperSerial.serialize(tt, tuple, false);
        Tuple deserial = SuperSerial.deserialize(tt, str, false);
        assertEquals(tuple, deserial);

        str = SuperSerial.serialize(tt, tuple, true);
        deserial = SuperSerial.deserialize(tt, str, true);
        assertEquals(tuple, deserial);
    }
}
