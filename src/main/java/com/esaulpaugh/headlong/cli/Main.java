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

import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.PackedDecoder;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.abi.util.Uint;
import com.esaulpaugh.headlong.rlp.RLPEncoder;
import com.esaulpaugh.headlong.rlp.util.Notation;
import com.esaulpaugh.headlong.rlp.util.NotationParser;
import com.esaulpaugh.headlong.util.Strings;
import com.esaulpaugh.headlong.util.SuperSerial;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static com.esaulpaugh.headlong.cli.Argument.DATA_FIRST;
import static com.esaulpaugh.headlong.cli.Argument.DATA_SECOND;
import static com.esaulpaugh.headlong.cli.Argument.OPTION;

public class Main {

    private static final String HELP_STRING = "command format:\n" +
            "-[m/r][e/d][f][c]\n" +
            "\tm is for machine interface (ABI only)\n" +
            "\tr is for RLP\n" +
            "\te is for encode\n" +
            "\td is for decode\n" +
            "\tf is for function call (ABI only)\n" +
            "\tc is for compact output (decode only)\n" +
            "only [e/d] is mandatory";

    public static void main(String[] args0) {
        evalPrint(args0);
    }

    static void evalPrint(String[] args) {
        try {
            System.out.println(eval(args));
        } catch (IllegalArgumentException e) {
            System.err.println("exception: " + e.getClass().getSimpleName() + "\nmessage: " + e.getMessage() + "\ncause: " + e.getCause());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static String eval(String[] args) {
        switch (args[OPTION.ordinal()]) {
        case "-help": return HELP_STRING;
        case "-e": return encodeABI(args, false, false);
        case "-ep": return encodeABIPacked(args, false);
        case "-mep": return encodeABIPacked(args, true);
        case "-ef": return encodeABI(args, false, true);
        case "-d": return decodeABI(args, false, false, false);
        case "-df": return decodeABI(args, false, true, false);
        case "-dc": return decodeABI(args, false, false, true);
        case "-dfc": return decodeABI(args, false, true, true);
        case "-dp": return decodeABIPacked(args);
        case "-me": return encodeABI(args, true, false);
        case "-mef": return encodeABI(args, true, true);
        case "-md": return decodeABI(args, true, false, false);
        case "-mdf": return decodeABI(args, true, true, false);
        case "-re": return encodeRLP(args);
        case "-yyy": return hexToDec(args, false);
        case "-yyyc": return hexToDec(args, true);
        case "-zzz": return decToHex(args, false);
        case "-zzzc": return decToHex(args, true);
        case "-ggg": return utf8ToHex(args, false);
        case "-gggc": return utf8ToHex(args, true);
        case "-mmm": return hexToUtf8(args, false);
        case "-mmmc": return hexToUtf8(args, true);
        case "-rd": return decodeRLP(args, false);
        case "-rdc": return decodeRLP(args, true);
        default: throw new IllegalArgumentException("bad primary option");
        }
    }

    private static String encodeABIPacked(String[] args, boolean machine) {
        final String signature = args[DATA_FIRST.ordinal()];
        final String values = args[DATA_SECOND.ordinal()];
        final TupleType tt = TupleType.parse(signature);
        return Strings.encode(tt.encodePacked(SuperSerial.deserialize(tt, values, machine)).array());
    }

    private static String decodeABIPacked(String[] args) {
        final String signature = args[DATA_FIRST.ordinal()];
        final String values = args[DATA_SECOND.ordinal()];
        final TupleType tt = TupleType.parse(signature);
        return SuperSerial.serialize(tt, PackedDecoder.decode(tt, Strings.decode(values)), false);
    }

    private static String encodeABI(String[] args, boolean machine, boolean function) {
        final String signature = args[DATA_FIRST.ordinal()];
        final String values = args[DATA_SECOND.ordinal()];
        final ByteBuffer abi;
        if(function) {
            Function f = Function.parse(signature);
            abi = f.encodeCall(SuperSerial.deserialize(f.getParamTypes(), values, machine));
        } else {
            TupleType tt = TupleType.parse(signature);
            abi = tt.encode(SuperSerial.deserialize(tt, values, machine));
        }
        return Strings.encode(abi.array());
    }

    private static String decodeABI(String[] args, boolean machine, boolean function, boolean compact) {
        final String signature = args[DATA_FIRST.ordinal()];
        final byte[] abiBytes = Strings.decode(args[DATA_SECOND.ordinal()]);
        final TupleType tt;
        final Tuple values;
        if(function) {
            Function f = Function.parse(signature);
            tt = f.getParamTypes();
            values = f.decodeCall(abiBytes);
        } else {
            tt = TupleType.parse(signature);
            values = tt.decode(abiBytes);
        }
        String serialization = SuperSerial.serialize(tt, values, machine);
        return compact(serialization, compact);
    }

    private static String encodeRLP(String[] args) {
        final String rlpNotation = args[DATA_FIRST.ordinal()];
        return Strings.encode(RLPEncoder.encodeSequentially(NotationParser.parse(rlpNotation)));
    }

    private static String decodeRLP(String[] args, boolean compact) {
        final String rlpHex = args[DATA_FIRST.ordinal()];
        String notationString = Notation.forEncoding(Strings.decode(rlpHex)).toString();
        return compact(notationString, compact);
    }

    private static String decToHex(String[] args, boolean compact) {
        final int typeBits = checkTypeBits(Integer.parseInt(args[DATA_FIRST.ordinal()]));
        if(typeBits < 0 || typeBits % 8 != 0 || typeBits > 256) {
            throw new IllegalArgumentException("invalid typeBits");
        }
        final Uint uint = new Uint(typeBits);
        final int start = DATA_FIRST.ordinal() + 1;
        final int end = args.length;
        Object[] objects = new Object[end - start];
        int idx = 0;
        for (int i = start; i < end; i++) {
            BigInteger bi = new BigInteger(args[i], 10);
            BigInteger unsigned = uint.toUnsigned(bi);
//            BigInteger signed = uint.toSigned(unsigned);
            String hex = unsigned.toString(16);
            objects[idx++] = Strings.decode(hex.length() % 2 == 0 ? hex : "0" + hex); // SuperSerial.serializeBigInteger(typeBits, unsigned);
        }
        return compact(Notation.forObjects(objects).toString(), compact);
    }

    private static String hexToDec(String[] args, boolean compact) {
        final int a = DATA_FIRST.ordinal();
        final int typeBits = checkTypeBits(Integer.parseInt(args[a]));
        final Uint uint = new Uint(typeBits);
        final char delimiter = compact ? ' ' : '\n';
        final String signedStr = args[a + 1];
        boolean signed = false;
        if("true".equals(signedStr)) {
            signed = true;
        } else if(!"false".equals(signedStr)) {
            throw new IllegalArgumentException("must specify signed as \"true\" or \"false\"");
        }
        final int start = a + 2;
        final int end = args.length;
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            BigInteger bi = new BigInteger(args[i], 16);
            BigInteger x = signed ? uint.toSigned(bi) : bi;
            sb.append(x.toString(10)).append(delimiter);
        }
        return sb.toString();
    }

    private static int checkTypeBits(int typeBits) {
        if(typeBits >= 0 && typeBits % 8 == 0 && typeBits <= 256) {
            return typeBits;
        }
        throw new IllegalArgumentException("invalid typeBits");
    }

    private static String hexToUtf8(String[] args, boolean compact) {
        final int start = DATA_FIRST.ordinal();
        final int end = args.length;
        StringBuilder sb = new StringBuilder();
        final char delimiter = compact ? ' ' : '\n';
        for (int i = start; i < end; i++) {
            sb.append(Strings.encode(Strings.decode(args[i]), Strings.UTF_8)).append(delimiter);
        }
        return sb.toString();
    }

    private static String utf8ToHex(String[] args, boolean compact) {
        final int start = DATA_FIRST.ordinal();
        final int end = args.length;
        Object[] objects = new Object[end - start];
        int idx = 0;
        for (int i = start; i < end; i++) {
            objects[idx++] = Strings.decode(args[i], Strings.UTF_8);
        }
        return compact(Notation.forObjects(objects).toString(), compact);
    }

    private static String compact(String str, boolean compact) {
        return !compact ? str : str.replaceAll("[\n ]", "");
    }
}
