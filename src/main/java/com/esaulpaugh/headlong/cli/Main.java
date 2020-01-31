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
import com.esaulpaugh.headlong.exception.DecodeException;
import com.esaulpaugh.headlong.util.FastHex;
import com.joemelsha.crypto.hash.Keccak;

import java.nio.ByteBuffer;

import static com.esaulpaugh.headlong.cli.Argument.DATA_FIRST;
import static com.esaulpaugh.headlong.cli.Argument.DATA_SECOND;
import static com.esaulpaugh.headlong.cli.Argument.DATA_THIRD;
import static com.esaulpaugh.headlong.cli.Argument.OPTION_PRIMARY;
import static com.esaulpaugh.headlong.cli.Argument.OPTION_SECONDARY;

public class Main {

    // java -jar .\headlong-cli-0.1-SNAPSHOT.jar -e -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'
    // java -jar headlong-cli-0.1-SNAPSHOT.jar -e -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'

    public static void main(String[] args0) {
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

        System.out.println(eval(args0));
    }

    static String eval(String[] args) {
        try {
            switch (args[OPTION_PRIMARY.ordinal()]) {
            case "-e": return encodeABI(args, false);
            case "-d": return decodeABI(args, false);
            case "-em": return encodeABI(args, true);
            case "-dm": return decodeABI(args, true);
            default: throw new IllegalArgumentException("bad primary option");
            }
        } catch (IllegalArgumentException | DecodeException | ABIException e) {
            return e.getClass() + " " + e.getMessage() + " " + e.getCause();
        } catch (Throwable t) {
            return "THROWABLE " + t.getClass() + " " + t.getMessage() + " " + t.getCause();
        }
    }

    private static String encodeABI(String[] args, boolean machine) throws ABIException, DecodeException {
        return FastHex.encodeToString(_encode(args, machine).array());
    }

    private static ByteBuffer _encode(String[] args, boolean machine) throws ABIException, DecodeException {
        final String encodeOptions = args[OPTION_SECONDARY.ordinal()];
        switch (encodeOptions) {
        case "-f": {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return f.encodeCall(SuperSerial.deserialize(f.getParamTypes(), args[DATA_SECOND.ordinal()], machine));
        }
        case "-af": {
            String name = args[DATA_FIRST.ordinal()];
            TupleType tt = Deserializer.parseTupleType(args[DATA_SECOND.ordinal()]);
            Function f = new Function(Function.Type.FUNCTION, name, tt, TupleType.EMPTY, null, new Keccak(256));
            return f.encodeCall(SuperSerial.deserialize(f.getParamTypes(), args[4], machine));
        }
        case "-a": {
            TupleType tt = Deserializer.parseTupleType(args[DATA_FIRST.ordinal()]);
            return tt.encode(SuperSerial.deserialize(tt, args[DATA_SECOND.ordinal()], machine));
        }
        case "-n": {
            TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
            Tuple t = SuperSerial.deserialize(tt, args[DATA_SECOND.ordinal()], machine);
            return tt.encode(t);
        }
        default:
            throw new IllegalArgumentException("bad secondary option");
        }
    }

    private static String decodeABI(String[] args, boolean machine) throws ABIException, DecodeException {
        final String decodeOptions = args[OPTION_SECONDARY.ordinal()];
        switch (decodeOptions) {
        case "-f": {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return decodeValues(f, args[3], machine);
        }
        case "-af": {
            String name = args[DATA_FIRST.ordinal()];
            TupleType tt = Deserializer.parseTupleType(args[DATA_SECOND.ordinal()]);
            Function f = new Function(Function.Type.FUNCTION, name, tt, TupleType.EMPTY, null, new Keccak(256));
            return decodeValues(f, args[DATA_THIRD.ordinal()], machine);
        }
        case "-a": {
            TupleType tt = Deserializer.parseTupleType(args[DATA_FIRST.ordinal()]);
            return decodeValues(tt, args[DATA_SECOND.ordinal()], machine);
        }
        case "-n": {
            TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
            return decodeValues(tt, args[DATA_SECOND.ordinal()], machine);
        }
        default:
            throw new IllegalArgumentException("bad secondary option");
        }
    }

    static String decodeValues(Function f, String hex, boolean machine) throws ABIException, DecodeException {
        Tuple values = f.decodeCall(FastHex.decode(hex));
        return SuperSerial.serialize(f.getParamTypes(), values, machine);
    }

    static String decodeValues(TupleType tt, String hex, boolean machine) throws ABIException, DecodeException {
        Tuple values = tt.decode(FastHex.decode(hex));
        return SuperSerial.serialize(tt, values, machine);
    }
}
