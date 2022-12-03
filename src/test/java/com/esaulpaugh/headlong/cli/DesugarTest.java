/*
   Copyright 2021 Evan Saulpaugh

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

import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import com.esaulpaugh.headlong.abi.SuperSerial;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesugarTest {

    @Test
    public void testBool() throws Throwable {
        TupleType tt = TupleType.parse("(bool)");
        Tuple _true = Tuple.of(true);
        Tuple _false = Tuple.of(false);

        assertEquals("01", SuperSerial.serialize(tt, _true, true));
        assertEquals("80", SuperSerial.serialize(tt, _false, true));
        assertEquals("(\n  '01'\n)", SuperSerial.serialize(tt, _true, false));
        assertEquals("(\n  ''\n)", SuperSerial.serialize(tt, _false, false));

        String[] command = new String[] { "-ef", "sam(bool)", "('02')" };
        assertThrown(IllegalArgumentException.class, "illegal boolean RLP: 0x02. Expected 0x01 or 0x80", () -> Main.eval(command));
    }

    @Test
    public void testVector() {
        String[] command = new String[] { "-ef", "sam(bytes,bool,uint256[])", "(u'dave', b'true', [ d'1', d'2', d'3' ])" };
        String out = Main.eval(command);
        assertEquals(
                "a5643bf2" +
                "0000000000000000000000000000000000000000000000000000000000000060" +
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "00000000000000000000000000000000000000000000000000000000000000a0" +
                "0000000000000000000000000000000000000000000000000000000000000004" +
                "6461766500000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000003" +
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "0000000000000000000000000000000000000000000000000000000000000002" +
                "0000000000000000000000000000000000000000000000000000000000000003",
                out
        );
        Tuple tuple = Function.parse("sam(bytes,bool,uint256[])").decodeCall(FastHex.decode(out));
        System.out.println(tuple);
        assertEquals(
                Tuple.of(
                        "dave".getBytes(StandardCharsets.UTF_8),
                        true,
                        new BigInteger[] {
                            BigInteger.ONE,
                            BigInteger.valueOf(2L),
                            BigInteger.valueOf(3L)
                         }
                ),
                tuple
        );
    }

    @Test
    public void testVector2() {
        String[] command = new String[] { "-ef", "sam(bytes,bool,uint256[])", "(u'\u0009', b'false', [ d'-1', d'-2', d'-3' ])" };
        String out = Main.eval(command);
        assertEquals(
                "a5643bf2" +
                        "0000000000000000000000000000000000000000000000000000000000000060" +
                        "0000000000000000000000000000000000000000000000000000000000000000" +
                        "00000000000000000000000000000000000000000000000000000000000000a0" +
                        "0000000000000000000000000000000000000000000000000000000000000001" +
                        "0900000000000000000000000000000000000000000000000000000000000000" +
                        "0000000000000000000000000000000000000000000000000000000000000003" +
                        "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe" +
                        "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd",
                out
        );
        Tuple tuple = Function.parse("sam(bytes,bool,uint256[])").decodeCall(FastHex.decode(out));
        System.out.println(tuple);
        assertEquals(
                Tuple.of(
                        "\t".getBytes(StandardCharsets.UTF_8),
                        false,
                        new BigInteger[] {
                                new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff", 16),
                                new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe", 16),
                                new BigInteger("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffd", 16)
                        }
                ),
                tuple
        );
    }

    @Test
    public void testAddress() {
        String[] command = new String[] { "-e", "(address)", "( a'0x000000000000F9087ABcDEf00CafeBaBE86244AA' ])" };
        String out = Main.eval(command);
        assertEquals("000000000000000000000000000000000000f9087abcdef00cafebabe86244aa", out);
    }

    @FunctionalInterface
    public interface CustomRunnable {
        void run() throws Throwable;
    }

    public static void assertThrown(Class<? extends Throwable> clazz, String substr, CustomRunnable r) throws Throwable {
        try {
            r.run();
        } catch (Throwable t) {
            if(clazz.isInstance(t)
                    && (t.getMessage() == null || t.getMessage().contains(substr))) {
                return;
            }
            throw t;
        }
        throw new AssertionError("no " + clazz.getName() + " thrown");
    }
}
