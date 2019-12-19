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
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.abi.ValidationException;
import com.esaulpaugh.headlong.util.FastHex;
import com.joemelsha.crypto.hash.Keccak;

import java.nio.ByteBuffer;
import java.text.ParseException;

public class Main {

    // java -jar .\headlong-cli-0.1-SNAPSHOT.jar -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'
    // java -jar headlong-cli-0.1-SNAPSHOT.jar -n '(uint112)' '[{"type":"string","value":"0x5d92d2a10d4e107b1d"}]'

    public static void main(String[] args0) throws ValidationException, ParseException {
        System.out.println(FastHex.encodeToString(encodeResult(args0).array()));
    }

    static ByteBuffer encodeResult(String[] args) throws ValidationException, ParseException {
        final String options = args[0];
        if(options.equals("-f")) {
            Function f = Function.parse(args[1]);
            return f.encodeCall(createTuple(f.getParamTypes(), args[2]));
        } else if(options.equals("-af")) {
            String name = args[1];
            TupleType tt = Deserializer.parseTupleType(args[2]);
            Function f = new Function(Function.Type.FUNCTION, name, tt, TupleType.EMPTY, null, new Keccak(256));
            return f.encodeCall(createTuple(f.getParamTypes(), args[3]));
        } else if(options.equals("-a")) {
            TupleType tt = Deserializer.parseTupleType(args[1]);
            return tt.encode(createTuple(tt, args[2]));
        } else if(options.equals("-n")) {
            TupleType tt = TupleType.parse(args[1]);
            return tt.encode(createTuple(tt, args[2]));
        } else {
            throw new IllegalArgumentException("bad options arg. specify -n for no options");
        }
    }

    private static Tuple createTuple(TupleType tt, String tupleStr) {
        return Deserializer.parseTupleValue(tt, tupleStr);
    }
}
