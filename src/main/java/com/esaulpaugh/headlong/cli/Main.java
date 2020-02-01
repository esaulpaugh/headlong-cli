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
import com.esaulpaugh.headlong.util.SuperSerial;

import java.nio.ByteBuffer;

import static com.esaulpaugh.headlong.cli.Argument.DATA_FIRST;
import static com.esaulpaugh.headlong.cli.Argument.DATA_SECOND;
import static com.esaulpaugh.headlong.cli.Argument.OPTION;

public class Main {

    public static void main(String[] args0) {
        evalPrint(args0);
    }

    static void evalPrint(String[] args) {
        try {
            System.out.println(eval(args));
        } catch (IllegalArgumentException | DecodeException | ABIException e) {
            System.err.println("class: " + e.getClass().getSimpleName() + "\nmessage: " + e.getMessage() + "\ncause: " + e.getCause());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static String eval(String[] args) throws ABIException, DecodeException {
        switch (args[OPTION.ordinal()]) {
        case "-e": return encodeABI(args, false, false);
        case "-ef": return encodeABI(args, false, true);
        case "-d": return decodeABI(args, false, false);
        case "-df": return decodeABI(args, false, true);
        case "-em": return encodeABI(args, true, false);
        case "-emf": return encodeABI(args, true, true);
        case "-dm": return decodeABI(args, true, false);
        case "-dmf": return decodeABI(args, true, true);
        default: throw new IllegalArgumentException("bad primary option");
        }
    }

    private static String encodeABI(String[] args, boolean machine, boolean function) throws ABIException, DecodeException {
        return FastHex.encodeToString(_encode(args, machine, function).array());
    }

    private static ByteBuffer _encode(String[] args, boolean machine, boolean function) throws ABIException, DecodeException {
        if(function) {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return f.encodeCall(SuperSerial.deserialize(f.getParamTypes(), args[DATA_SECOND.ordinal()], machine));
        }
        TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
        Tuple t = SuperSerial.deserialize(tt, args[DATA_SECOND.ordinal()], machine);
        return tt.encode(t);
    }

    private static String decodeABI(String[] args, boolean machine, boolean function) throws ABIException, DecodeException {
        if(function) {
            Function f = Function.parse(args[DATA_FIRST.ordinal()]);
            return decodeValues(f, args[DATA_SECOND.ordinal()], machine);
        }
        TupleType tt = TupleType.parse(args[DATA_FIRST.ordinal()]);
        return decodeValues(tt, args[DATA_SECOND.ordinal()], machine);
    }

    private static String decodeValues(Function f, String hex, boolean machine) throws ABIException, DecodeException {
        Tuple values = f.decodeCall(FastHex.decode(hex));
        return SuperSerial.serialize(f.getParamTypes(), values, machine);
    }

    private static String decodeValues(TupleType tt, String hex, boolean machine) throws ABIException, DecodeException {
        Tuple values = tt.decode(FastHex.decode(hex));
        System.out.println(values);
        return SuperSerial.serialize(tt, values, machine);
    }
}
