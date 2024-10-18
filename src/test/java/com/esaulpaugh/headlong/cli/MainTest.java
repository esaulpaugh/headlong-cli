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

import com.esaulpaugh.headlong.abi.ABIType;
import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.ArrayType;
import com.esaulpaugh.headlong.abi.ByteType;
import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.Single;
import com.esaulpaugh.headlong.abi.SuperSerial;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.abi.TypeFactory;
import com.esaulpaugh.headlong.util.Strings;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private static final Function FUNCTION = new Function("gogo((fixed[],int8)[1][][5])", "(ufixed,string)");

    private static final Tuple RETURN_ARGS = Tuple.of(new BigDecimal(BigInteger.valueOf(69L), 18), "w00t");

    @Test
    public void testLenient() throws Throwable {
        final byte[] lenientBytes = Strings.decode(
            "0000000000000000000000000000000000000000000000000000000000000045"
          + "00000000000000000000000000000000000000000000000000000000000000a3"
          + "0000000000000000000000000000000000000000000000000000000000000000"
          + "0000000000000000000000000000000000000000000000000000000000000000"
          + "0000000000000000000000000000000000000000000000000000000000000000000000"
          + "0000000000000000000000000000000000000000000000000000000000000004"
          + "7730307400000000000000000000000000000000000000000000000000000000");

        if (!RETURN_ARGS.equals(FUNCTION.decodeReturn(lenientBytes))) throw new Error();
        if (!RETURN_ARGS.equals(FUNCTION.decodeReturn(ByteBuffer.wrap(lenientBytes)))) throw new Error();

        final byte[] tooSmallOffset = Strings.decode(
            "0000000000000000000000000000000000000000000000000000000000000045"
          + "000000000000000000000000000000000000000000000000000000000000003f"
          + "0000000000000000000000000000000000000000000000000000000000000004"
          + "7730307400000000000000000000000000000000000000000000000000000000");

        assertThrown(IllegalArgumentException.class, "unsigned val exceeds bit limit: 254 > 21", () -> FUNCTION.decodeReturn(tooSmallOffset));
    }

    private static final String SIGNATURE = "(function[2][][],bytes24,string[1][1],address[],uint72,(uint8),(int16)[2][][1],(int32)[],uint40,(int48)[],(uint),bool,string,bool[2],int24[],uint40[1])";

    static final String SERIALIZATION = "(\n" +
            "  [ [ [ '191c766e29a65787b7155dd05f41292438467db93420cade', '191c766e29a65787b7155dd05f41292438467db93420cade' ] ] ],\n" +
            "  '191c766e29a65787b7155dd05f41292438467db93420cade',\n" +
            "  [ [ '7a' ] ],\n" +
            "  [ 'ff00ee01dd02cc03cafebabe9906880777086609' ],\n" +
            "  'fdfffffffffffffe04',\n" +
            "  [ '07' ],\n" +
            "  [ [ [ [ '09' ], [ 'fff5' ] ] ] ],\n" +
            "  [ [ '11' ], [ 'ffffffed' ] ],\n" +
            "  'fca527923b',\n" +
            "  [ [ '7e' ], [ 'ffffffffff82' ] ],\n" +
            "  [ '0a' ],\n" +
            "  '01',\n" +
            "  '6661726f7574',\n" +
            "  [ '01', '01' ],\n" +
            "  [ '03', '14', 'fffffa' ],\n" +
            "  [ 'fffffffe' ]\n" +
            ")";

    static final String MACHINE_SERIALIZATION = "f4f3f298191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cade98191c766e29a65787b7155dd05f41292438467db93420cadec2c17ad594ff00ee01dd02cc03cafebabe990688077708660989fdfffffffffffffe04c107c8c7c6c109c382fff5c8c111c584ffffffed85fca527923bcac17ec786ffffffffff82c10a01866661726f7574c20101c6031483fffffac584fffffffe";

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
    public void testEncode() {

        String[] me = new String[] { "-me", SIGNATURE, MACHINE_SERIALIZATION };
        String[] mef = new String[] { "-mef", "nam" + SIGNATURE, MACHINE_SERIALIZATION };

        assertEquals(VALUES_ABI, Main.eval(me));

        final String functionCall = "9e066e5d" + VALUES_ABI;

        assertEquals(functionCall, Main.eval(mef));

        String[] e = new String[] { "-e", SIGNATURE, SERIALIZATION };
        String[] ef = new String[] { "-ef", "nam" + SIGNATURE, SERIALIZATION };

        assertEquals(VALUES_ABI, Main.eval(e));

        assertEquals(functionCall, Main.eval(ef));
    }

    @Test
    public void testDecode() {
        String[] md = new String[] { "-md", SIGNATURE, VALUES_ABI };
        String[] mdf = new String[] { "-mdf", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };

        assertEquals(MACHINE_SERIALIZATION, Main.eval(md));
        assertEquals(MACHINE_SERIALIZATION, Main.eval(mdf));

        String[] d = new String[] { "-d", SIGNATURE, VALUES_ABI };
        String[] df = new String[] { "-df", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };
        String[] dc = new String[] { "-dc", SIGNATURE, VALUES_ABI };
        String[] dfc = new String[] { "-dfc", "nam" + SIGNATURE, "9e066e5d" + VALUES_ABI };

        assertEquals(SERIALIZATION, Main.eval(d));
        assertEquals(SERIALIZATION, Main.eval(df));

        String compact = SERIALIZATION.replaceAll("[\n ]", "");
        assertEquals(compact, Main.eval(dc));
        assertEquals(compact, Main.eval(dfc));
    }

    @Test
    public void testSerial() {

        TupleType<Tuple> tt = TupleType.parse(SIGNATURE);

        byte[] func = Strings.decode("191c766e29a65787b7155dd05f41292438467db93420cade");

        Object[] argsIn = new Object[] {
                new byte[][][][] { new byte[][][] { new byte[][] { func, func } } },
                func,
                new String[][] { new String[] { "z" } },
                new Address[] { Address.wrap("0xFF00eE01dd02cC03cafEBAbe9906880777086609") },
                BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Byte.MAX_VALUE << 2)),
                Single.of(7),
                new Tuple[][][] { new Tuple[][] { new Tuple[] { Single.of(9), Single.of(-11) } } },
                new Tuple[] { Single.of(17), Single.of(-19) },
                Long.MAX_VALUE / 8_500_000,
                new Tuple[] { Single.of((long) 0x7e), Single.of((long) -0x7e) },
                Single.of(BigInteger.TEN),
                true,
                "farout",
                new boolean[] { true, true },
                new int[] { 3, 20, -6 },
                new long[] { Integer.MAX_VALUE * 2L }
        };

        Tuple tuple = Tuple.from(argsIn);

        String str = SuperSerial.serialize(tt, tuple, false);
        Tuple deserial = SuperSerial.deserialize(tt, str, false);
        assertEquals(tuple, deserial);

        str = SuperSerial.serialize(tt, tuple, true);
        deserial = SuperSerial.deserialize(tt, str, true);
        assertEquals(tuple, deserial);
    }

    @Test
    public void testUnrecognizedCommand() throws Throwable {
        assertThrown(IllegalArgumentException.class, "unrecognized command: -utfdec", () -> Main.eval(new String[] { "-utfdec", "08ff", "08fe" }));
    }

    @Test
    public void testHexToDec() throws Throwable {
        assertThrown(IllegalArgumentException.class, "first datum must be the bit length of the args", () -> Main.eval(new String[] { "-hexdec", "08ff", "08fe" }));

        assertThrown(IllegalArgumentException.class, "specified bit length must be greater than 0", () -> Main.eval(new String[] { "-hexdec", "0", "true", "08ff", "08fe" }));
        assertThrown(IllegalArgumentException.class, "specified bit length must be less than or equal to 256", () -> Main.eval(new String[] { "-hexdec", "264", "true", "08ff", "08fe" }));
        assertThrown(IllegalArgumentException.class, "specified bit length must be a multiple of 8", () -> Main.eval(new String[] { "-hexdec", "4", "true", "08ff", "08fe" }));

        assertThrown(IllegalArgumentException.class, "second datum must be either \"signed\" or \"unsigned\"", () -> Main.eval(new String[] { "-hexdec", "16", "1", "08ff", "08fe" }));

        assertEquals("2303\n2302", Main.eval(new String[] { "-hexdec", "248", "unsigned", "08ff", "08fe" }));
        assertEquals("-32513 -32514", Main.eval(new String[] { "-hexdecc", "16", "signed", "80ff", "80fe" }));
    }

    @Test
    public void testDecToHex() throws Throwable {

        assertThrown(IllegalArgumentException.class, "specified bit length must be greater than 0", () -> Main.eval(new String[] { "-dechex", "-32513", "2302" }));

        assertEquals("(\n  '80ff',\n  '08ff'\n)", Main.eval(new String[] { "-dechex", "16", "-32513", "2303" }));
        assertEquals("('ffff80ff','08ff')", Main.eval(new String[] { "-dechexc", "32", "-32513", "2303" }));
    }

    @Test
    public void testUtf8ToHex() {
        assertEquals("(\n" +
                "  '776f727420776f727420776f7274',\n" +
                "  '25486c0a2d2b'\n" +
                ")", Main.eval(new String[] { "-utfhex", "wort wort wort", "%Hl\n-+" }));

        assertEquals("('776f727420776f727420776f7274','25486c0a2d2b')", Main.eval(new String[] { "-utfhexc", "wort wort wort", "%Hl\n-+" }));
    }

    @Test
    public void testHexToUtf8() throws Throwable {

        assertThrown(IllegalArgumentException.class, "illegal hex val @ 0", () -> Main.eval(new String[] { "-hexutf", "-32513", "25486c0a2d2b" }));

        assertEquals("wort wort wort\n%Hl\n-+", Main.eval(new String[] { "-hexutf", "776f727420776f727420776f7274", "25486c0a2d2b" }));
        assertEquals("wort wort wort %Hl\n-+", Main.eval(new String[] { "-hexutfc", "776f727420776f727420776f7274", "25486c0a2d2b" }));
    }

    @Test
    public void testTypeFactory() {
        final ABIType<?> type = TypeFactory.create("string[]");
        assertEquals(ABIType.TYPE_CODE_ARRAY, type.typeCode());
        final ArrayType<ArrayType<ByteType, Byte, String>, String, String[]> arrayType = TypeFactory.create("string[]");
        assertEquals(ArrayType.DYNAMIC_LENGTH, arrayType.getLength());
    }

    @Test
    public void testEIP55() {
        final String expected = "0xfAfFffFFffFFFFFFFfFFfffFfFfFFFFfFfFFfFFF";
        assertEquals(expected, Main.eval(new String[] { "-eip55", "0xfaffffffffffffffffffffffffffffffffffffff" }));
        assertEquals(new BigInteger(expected.substring(2), 16), Address.wrap(expected).value());
    }

    @Test
    public void testAnnotate() {
        assertEquals(
                "foo:\n" +
                "ID       9d879739\n" +
                "     0   ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80\t[0] int8\n" +
                "    20   0000000000000000000000000000000000000000000000000000000000000040\t[1] bytes offset\n" +
                "    40   0000000000000000000000000000000000000000000000000000000000000006\t[1] bytes length\n" +
                "    60   25486c0a2d2b0000000000000000000000000000000000000000000000000000\t[1] bytes",
                Main.eval(new String[] { "-efann", "foo(int8,bytes)",
                        "(\n" +
                                "  '80',\n" +
                                "  '25486c0a2d2b'\n" +
                                ")"
                })
        );
        assertEquals(
                "     0   0000000000000000000000000000000000000000000000000000000000000040\t[0] bytes offset\n" +
                "    20   0000000000000000000000000000000000000000000000000000000000000080\t[1] uint8\n" +
                "    40   0000000000000000000000000000000000000000000000000000000000000006\t[0] bytes length\n" +
                "    60   25486c0a2d2b0000000000000000000000000000000000000000000000000000\t[0] bytes",
                Main.eval(new String[] { "-eann", "(bytes,uint8)",
                        "(\n" +
                                "  '25486c0a2d2b',\n" +
                                "  '80'\n" +
                                ")"
                })
        );

        assertEquals(
                "foo:\n" +
                "ID       9d879739\n" +
                "     0   ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff80\t[0] int8\n" +
                "    20   0000000000000000000000000000000000000000000000000000000000000040\t[1] bytes offset\n" +
                "    40   0000000000000000000000000000000000000000000000000000000000000006\t[1] bytes length\n" +
                "    60   25486c0a2d2b0000000000000000000000000000000000000000000000000000\t[1] bytes",
                Main.eval(new String[] { "-mefann", "foo(int8,bytes)",
                        "81808625486c0a2d2b"
                })
        );
        assertEquals(
                "     0   0000000000000000000000000000000000000000000000000000000000000040\t[0] bytes offset\n" +
                "    20   0000000000000000000000000000000000000000000000000000000000000080\t[1] uint8\n" +
                "    40   0000000000000000000000000000000000000000000000000000000000000006\t[0] bytes length\n" +
                "    60   25486c0a2d2b0000000000000000000000000000000000000000000000000000\t[0] bytes",
                Main.eval(new String[] { "-meann", "(bytes,uint8)",
                        "8625486c0a2d2b8180"
                })
        );
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
