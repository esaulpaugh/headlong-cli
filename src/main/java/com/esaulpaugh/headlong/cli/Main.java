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

import com.esaulpaugh.headlong.abi.ABIJSON;
import com.esaulpaugh.headlong.abi.ABIObject;
import com.esaulpaugh.headlong.abi.ABIType;
import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.Event;
import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.abi.util.JsonUtils;
import com.esaulpaugh.headlong.abi.util.Uint;
import com.esaulpaugh.headlong.rlp.RLPEncoder;
import com.esaulpaugh.headlong.rlp.Notation;
import com.esaulpaugh.headlong.util.Strings;
import com.esaulpaugh.headlong.util.SuperSerial;

import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import static com.esaulpaugh.headlong.cli.Argument.DATA_FIRST;
import static com.esaulpaugh.headlong.cli.Argument.DATA_SECOND;
import static com.esaulpaugh.headlong.cli.Argument.OPTION;

public class Main {

    private static final String VERSION_STRING;

    private static final String HELP_STRING = "primary command format:\n" +
            "-[m/r][e/d][f/p][c]\n" +
            "\tm is for machine interface (ABI only)\n" +
            "\tr is for RLP\n" +
            "\te is for encode\n" +
            "\td is for decode\n" +
            "\tf is for function call (ABI only)\n" +
            "\tp is for packed (ABI only)\n" +
            "\tc is for compact output (decode only)\n" +
            "only [e/d] is mandatory\n" +
            "\n" +
            "secondary commands:\n" +
            "-efform [fn signature] [fn args rlp object notation]\n" +
            "-hexdec [bitlen] [signed] [args...]\n" +
            "-hexdecc [bitlen] [signed] [args...]\n" +
            "-dechex [bitlen] [args...]\n" +
            "-dechexc [bitlen] [args...]\n" +
            "-utfhex [args...]\n" +
            "-utfhexc [args...]\n" +
            "-hexutf [args...]\n" +
            "-hexutfc [args...]\n" +
            "-format [abi hex]\n" +
            "-formatf [abi function call hex]\n" +
            "-parse [abi json array]\n" +
            "-parseobj [abi json object]";

    public static void main(String[] args0) {
        evalPrint(args0);
    }

    private static void evalPrint(String[] args) {
        try {
            System.out.println(eval(args));
        } catch (IllegalArgumentException e) {
            System.err.println("exception: " + e.getClass().getSimpleName() + "\nmessage: " + e.getMessage() + "\ncause: " + e.getCause());
        } catch (Exception t) {
            t.printStackTrace();
        }
    }

    static String eval(String[] args) {
        final String command = args[OPTION.ordinal()];
        switch (validateCommand(command)) {
        case "-help": return HELP_STRING;
        case "-version": return VERSION_STRING;
        case "-e": return encodeABI(args, false, false);
        case "-ef": return encodeABI(args, false, true);
        case "-efform": return Function.formatCall(Strings.decode(encodeABI(args, false, true)));
        case "-ep": return encodeABIPacked(args, false);
        case "-mep": return encodeABIPacked(args, true);
        case "-d": return decodeABI(args, false, false, false);
        case "-df": return decodeABI(args, false, true, false);
        case "-dc": return decodeABI(args, false, false, true);
        case "-dfc": return decodeABI(args, false, true, true);
        case "-dp": return decodeABIPacked(args, false);
        case "-dpc": return decodeABIPacked(args, true);
        case "-me": return encodeABI(args, true, false);
        case "-mef": return encodeABI(args, true, true);
        case "-md": return decodeABI(args, true, false, false);
        case "-mdf": return decodeABI(args, true, true, false);
        case "-re": return encodeRLP(args);
        case "-rd": return decodeRLP(args, false);
        case "-rdc": return decodeRLP(args, true);
        case "-hexdec": return hexToDec(args, false);
        case "-hexdecc": return hexToDec(args, true);
        case "-dechex": return decToHex(args, false);
        case "-dechexc": return decToHex(args, true);
        case "-utfhex": return utf8ToHex(args, false);
        case "-utfhexc": return utf8ToHex(args, true);
        case "-hexutf": return hexToUtf8(args, false);
        case "-hexutfc": return hexToUtf8(args, true);
        case "-format": return format(args);
        case "-formatf": return formatFunctionCall(args);
        case "-parseabijson": return parseAbiJson(args);
        case "-eip55": return Address.toChecksumAddress(args[DATA_FIRST.ordinal()]);
        default: throw new IllegalArgumentException("unrecognized command: " + command);
        }
    }

    private static String parseVals(String raw, boolean machine, boolean extend) {
        return machine ? raw : SugarSerial.desugar(raw, extend);
    }

    private static String validateCommand(String command) {
        if(command.charAt(0) == '-') {
            return command;
        }
        throw new IllegalArgumentException("commands must start with a hyphen: -");
    }

    private static String encodeABIPacked(String[] args, boolean machine) {
        final String signature = args[DATA_FIRST.ordinal()];
        final String values = parseVals(args[DATA_SECOND.ordinal()], machine, true);
        final TupleType tt = TupleType.parse(signature);
        return Strings.encode(tt.encodePacked(SuperSerial.deserialize(tt, values, machine)).array());
    }

    private static String decodeABIPacked(String[] args, boolean compact) {
        final TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
        final byte[] packedAbi = Strings.decode(args[DATA_SECOND.ordinal()]);
        return compacted(SuperSerial.serialize(tt, tt.decodePacked(packedAbi), false), compact);
    }

    private static String encodeABI(String[] args, boolean machine, boolean function) {
        final String signature = args[DATA_FIRST.ordinal()];
        final String values = parseVals(args[DATA_SECOND.ordinal()], machine, true);
        final ByteBuffer abi;
        if(function) {
            Function f = Function.parse(signature);
            abi = f.encodeCall(SuperSerial.deserialize(f.getInputs(), values, machine));
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
            tt = f.getInputs();
            values = f.decodeCall(abiBytes);
        } else {
            tt = TupleType.parse(signature);
            values = tt.decode(abiBytes);
        }
        return compacted(SuperSerial.serialize(tt, values, machine), compact);
    }

    private static String encodeRLP(String[] args) {
        final String values = parseVals(args[DATA_FIRST.ordinal()], false, false);
        final List<Object> objects = Notation.parse(values);
        return Strings.encode(RLPEncoder.encodeSequentially(objects));
    }

    private static String decodeRLP(String[] args, boolean compact) {
        final byte[] rlpBytes = Strings.decode(args[DATA_FIRST.ordinal()]);
        return compacted(Notation.forEncoding(rlpBytes).toString(), compact);
    }

    private static String decToHex(String[] args, boolean compact) {
        final int typeBits = parseTypeBits(args[DATA_FIRST.ordinal()]);
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
        return compacted(Notation.forObjects(objects).toString(), compact);
    }

    private static String hexToDec(String[] args, boolean compact) {
        final int typeBits = parseTypeBits(args[DATA_FIRST.ordinal()]);
        final Uint uint = new Uint(typeBits);
        final char delimiter = compact ? ' ' : '\n';
        final String signedStr = args[DATA_SECOND.ordinal()];
        boolean signed = false;
        if("true".equals(signedStr)) {
            signed = true;
        } else if(!"false".equals(signedStr)) {
            throw new IllegalArgumentException("second datum must specify signedness of the args as \"true\" or \"false\"");
        }
        final int start = DATA_SECOND.ordinal() + 1;
        final int end = args.length;
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            BigInteger val = new BigInteger(args[i], 16);
            if(signed) {
                val = uint.toSigned(val);
            }
            sb.append(val.toString(10)).append(delimiter);
        }
        return trimmed(sb, end > start);
    }

    private static String utf8ToHex(String[] args, boolean compact) {
        final int start = DATA_FIRST.ordinal();
        final int end = args.length;
        Object[] objects = new Object[end - start];
        int idx = 0;
        for (int i = start; i < end; i++) {
            objects[idx++] = Strings.decode(args[i], Strings.UTF_8);
        }
        return compacted(Notation.forObjects(objects).toString(), compact);
    }

    private static String hexToUtf8(String[] args, boolean compact) {
        final int start = DATA_FIRST.ordinal();
        final int end = args.length;
        StringBuilder sb = new StringBuilder();
        final char delimiter = compact ? ' ' : '\n';
        for (int i = start; i < end; i++) {
            sb.append(Strings.encode(Strings.decode(args[i]), Strings.UTF_8)).append(delimiter);
        }
        return trimmed(sb, end > start);
    }

    private static int parseTypeBits(String val) {
        try {
            return checkTypeBits(Integer.parseInt(val));
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("first datum must be the bit length of the args");
        }
    }

    private static int checkTypeBits(int typeBits) {
        if(typeBits > 0) {
            if(typeBits <= 256) {
                if(typeBits % 8 == 0) {
                    return typeBits;
                }
                throw new IllegalArgumentException("specified bit length must be a multiple of 8");
            }
            throw new IllegalArgumentException("specified bit length must be less than or equal to 256");
        }
        throw new IllegalArgumentException("specified bit length must be greater than 0");
    }

    private static String trimmed(StringBuilder sb, boolean trim) {
        if(trim) {
            final int len = sb.length();
            sb.replace(len - 1, len, "");
        }
        return sb.toString();
    }

    private static String compacted(String str, boolean compact) {
        return !compact ? str : str.replaceAll("[\n ]", "");
    }

    private static String format(String[] args) {
        return ABIType.format(Strings.decode(args[DATA_FIRST.ordinal()]));
    }

    private static String formatFunctionCall(String[] args) {
        return Function.formatCall(Strings.decode(args[DATA_FIRST.ordinal()]));
    }

    private static String parseAbiJson(String[] args) {
        final String json = args[DATA_FIRST.ordinal()];
        if(json.startsWith("[")) {
            return ABIJSON.parseElements(json)
                    .stream()
                    .map(Main::describe)
                    .collect(Collectors.joining("\n"));
        } else if(json.startsWith("{")) {
            return describe(ABIJSON.parseABIObject(JsonUtils.parseObject(json)));
        } else {
            throw new IllegalArgumentException("json must start with '[' or '{'");
        }
    }

    private static String describe(ABIObject o) {
        if(o instanceof Function) {
            Function foo = (Function) o;
            return foo.getType().name() + " " + foo.getCanonicalSignature() + getParamNames(foo.getInputs()) + " returns: " + foo.getOutputs().getCanonicalType() + " stateMutability: " + foo.getStateMutability();
        } else {
            Event e = (Event) o;
            return "event " + e.getCanonicalSignature() + getParamNames(e.getInputs()) + " indexed:" + Arrays.toString(e.getIndexManifest());
        }
    }

    private static String getParamNames(TupleType params) {
        boolean hasName = false;
        for (ABIType<?> type : params) {
            hasName |= type.getName() != null;
        }
        if(!hasName) return " ";
        StringBuilder sb = new StringBuilder(" names:(");
        for (ABIType<?> type : params) {
            sb.append(type.getName())
                    .append(',');
        }
        return completeNameString(sb);
    }

    private static String completeNameString(StringBuilder sb) {
        final int len = sb.length();
        return len != 1
                ? sb.replace(len - 1, len, ")").toString() // replace trailing comma
                : " ";
    }

    static {
        try {
            String buildDate = null;
            String headlongVersion = null;
            final Enumeration<URL> urls = Main.class.getClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (urls.hasMoreElements()) {
                final Attributes attrs = new Manifest(urls.nextElement().openStream()).getMainAttributes();
                if ("headlong-cli".equals(attrs.getValue("Implementation-Title"))) {
                    if (buildDate != null) {
                        throw new RuntimeException("multiple matching manifests");
                    }
                    buildDate = attrs.getValue("Build-Date");
                    headlongVersion = attrs.getValue("headlong-version");
                }
            }
            final Package pkg = Main.class.getPackage();
            VERSION_STRING = pkg.getImplementationTitle()
                    + " version "
                    + pkg.getImplementationVersion()
                    + "\ncompiled "
                    + buildDate
                    + "\nusing headlong v" + headlongVersion;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
