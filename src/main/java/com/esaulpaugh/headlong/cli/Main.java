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
import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.joemelsha.crypto.hash.Keccak;

import java.nio.ByteBuffer;

import static com.esaulpaugh.headlong.cli.Argument.DATA_FIRST;
import static com.esaulpaugh.headlong.cli.Argument.DATA_SECOND;
import static com.esaulpaugh.headlong.cli.Argument.DATA_THIRD;
import static com.esaulpaugh.headlong.cli.Argument.OPTION_PRIMARY;
import static com.esaulpaugh.headlong.cli.Argument.OPTION_SECONDARY;

public class Main {

    // java -jar .\headlong-cli-0.1-SNAPSHOT.jar -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'
    // java -jar headlong-cli-0.1-SNAPSHOT.jar -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'

    public static void main(String[] args0) throws ABIException { // TODO support decode abi back to json
//        args0 = new String[] {
//                "-e",
//                "-f",
//                "(uint112)",
//                "[{\"type\":\"string\",\"value\":\"0x5d92d2a10d4e107b1d\"}]",
//        };
//        args0 = new String[] {
//                "-d",
//                "-f",
//                "(uint112)",
//                "f745c2d500000000000000000000000000000000000000000000005d92d2a10d4e107b1d"
//        };

        try {
            switch (args0[OPTION_PRIMARY.ordinal()]) {
            case "-e": System.out.println(encodeABI(args0)); break;
            case "-d": System.out.println(decodeABI(args0)); break;
            }
        } catch (IllegalArgumentException | ABIException e) {
            System.out.println(e.getClass() + " " + e.getMessage() + " " + e.getCause());
        } catch (Throwable t) {
            System.out.println("THROWABLE " + t.getClass() + " " + t.getMessage() + " " + t.getCause());
        }
    }

    static String encodeABI(String[] args) throws ABIException {
        return FastHex.encodeToString(_encode(args).array());
    }

    private static ByteBuffer _encode(String[] args) throws ABIException {
        final String encodeOptions = args[OPTION_SECONDARY.ordinal()];
        switch (encodeOptions) {
        case "-f": {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return f.encodeCall(Deserializer.parseTupleValue(f.getParamTypes(), args[DATA_SECOND.ordinal()]));
        }
        case "-af": {
            String name = args[DATA_FIRST.ordinal()];
            TupleType tt = Deserializer.parseTupleType(args[DATA_SECOND.ordinal()]);
            Function f = new Function(Function.Type.FUNCTION, name, tt, TupleType.EMPTY, null, new Keccak(256));
            return f.encodeCall(Deserializer.parseTupleValue(f.getParamTypes(), args[4]));
        }
        case "-a": {
            TupleType tt = Deserializer.parseTupleType(args[DATA_FIRST.ordinal()]);
            return tt.encode(Deserializer.parseTupleValue(tt, args[DATA_SECOND.ordinal()]));
        }
        case "-n": {
            TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
            return tt.encode(Deserializer.parseTupleValue(tt, args[DATA_SECOND.ordinal()]));
        }
        default:
            throw new IllegalArgumentException("bad options arg. specify -n for no options");
        }
    }

    static String decodeABI(String[] args) throws ABIException {
        return new GsonBuilder().disableHtmlEscaping().create().toJson(_decode(args));
    }

    private static JsonArray _decode(String[] args) throws ABIException {
        final String decodeOptions = args[OPTION_SECONDARY.ordinal()];
        switch (decodeOptions) {
        case "-f": {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return decodeValues(f, args[3]);
        }
        case "-af": {
            String name = args[DATA_FIRST.ordinal()];
            TupleType tt = Deserializer.parseTupleType(args[DATA_SECOND.ordinal()]);
            Function f = new Function(Function.Type.FUNCTION, name, tt, TupleType.EMPTY, null, new Keccak(256));
            return decodeValues(f, args[DATA_THIRD.ordinal()]);
        }
        case "-a": {
            TupleType tt = Deserializer.parseTupleType(args[DATA_FIRST.ordinal()]);
            return decodeValues(tt, args[DATA_SECOND.ordinal()]);
        }
        case "-n": {
            TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
            return decodeValues(tt, args[DATA_SECOND.ordinal()]);
        }
        default:
            throw new IllegalArgumentException("bad options arg. specify -n for no options");
        }
    }

    static JsonArray decodeValues(Function f, String hex) throws ABIException {
        Tuple values = f.decodeCall(FastHex.decode(hex));
        return Serializer.serializeValues(values);
    }

    static JsonArray decodeValues(TupleType tt, String hex) throws ABIException {
        Tuple values = tt.decode(FastHex.decode(hex));
        return Serializer.serializeValues(values);
    }
}
