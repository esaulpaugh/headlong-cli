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

import com.esaulpaugh.headlong.abi.Address;
import com.esaulpaugh.headlong.abi.UnitType;
import com.esaulpaugh.headlong.util.Integers;
import com.esaulpaugh.headlong.util.Strings;

import java.math.BigInteger;
import java.util.Arrays;

import static com.esaulpaugh.headlong.util.Strings.EMPTY_BYTE_ARRAY;

/** Experimental */
public class SugarSerial {

    private SugarSerial() {}

    private static final char SINGLE_QUOTE = '\'';

    static String desugar(String values, boolean extend) {
        final StringBuilder sb = new StringBuilder();
        final int end = values.length();
        int i = 0;
        while(i < end) {
            final int q = values.indexOf(SINGLE_QUOTE, i);
            if (q <= 0) {
                break;
            }
            final int codeIdx = q - 1;
            final int valIdx = q + 1;
            sb.append(values, i, codeIdx).append(SINGLE_QUOTE);
            final int valEnd = values.indexOf(SINGLE_QUOTE, valIdx);
            i = valEnd + 1;
            final String val = values.substring(valIdx, valEnd);
            switch (values.charAt(codeIdx)) {
            case 'a': {
                String str = Address.toChecksumAddress(val).substring("0x".length());
                String hex = Strings.encode(serializeBigInteger(new BigInteger(str, 16), false));
                sb.append(hex);
                break;
            }
            case 'd': {
                BigInteger bi = new BigInteger(val, 10);
                byte[] bytes = serializeBigInteger(bi, extend);
                String hex = Strings.encode(bytes);
                sb.append(hex);
                break;
            }
            case 'u':
                byte[] bytes = Strings.decode(val, Strings.UTF_8);
                String hex = Strings.encode(bytes);
                sb.append(hex);
                break;
            case 'b':
                if(val.equals("true")) sb.append("01");
                else if(!val.equals("false")) throw new IllegalArgumentException("unexpected boolean syntax");
                break;
            default:
                sb.append(val);
            }
            sb.append(SINGLE_QUOTE);
        }
        return sb.append(')').toString();
    }

    private static byte[] serializeBigInteger(BigInteger val, boolean extend) {
        if(val.signum() > 0) {
            final byte[] bytes = Integers.toBytesUnsigned(val);
            return bytes; // extend ? signExtend(bytes, (byte) 0x00) :
        }
        if(val.signum() != 0) {
            final byte[] bytes = val.toByteArray();
            return extend ? signExtend(bytes, (byte) 0xff) : bytes;
        }
        return EMPTY_BYTE_ARRAY;
    }

    private static byte[] signExtend(final byte[] bytes, byte signByte) {
        final byte[] extended = new byte[UnitType.UNIT_LENGTH_BYTES];
        Arrays.fill(extended, signByte);
        System.arraycopy(bytes, 0, extended, UnitType.UNIT_LENGTH_BYTES - bytes.length, bytes.length);
        return extended;
    }
}
