package com.esaulpaugh.headlong.cli;

import com.esaulpaugh.headlong.abi.UnitType;
import com.esaulpaugh.headlong.util.Integers;
import com.esaulpaugh.headlong.util.Strings;

import java.math.BigInteger;
import java.util.Arrays;

import static com.esaulpaugh.headlong.util.Strings.EMPTY_BYTE_ARRAY;

/** Experimental */
public class SugarSerial {

    private SugarSerial() {}

    static String desugar(String values) {
        final StringBuilder sb = new StringBuilder();
        final int end = values.length();
        int i = 0;
        int prevIdx = 0;
        while(i < end) {
            final int q = values.indexOf('\'', i);
            if (q <= 0) {
                break;
            }
            sb.append(values, prevIdx, q - 1).append('\'');
            final int valEnd = values.indexOf('\'', q + 1);
            prevIdx = valEnd + 1;
            final String val = values.substring(q + 1, valEnd);
            char prev = values.charAt(q - 1);
            switch (prev) {
            case 'd': {
                BigInteger bi = new BigInteger(val, 10);
                byte[] bytes = serializeBigInteger(bi);
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
                else if(val.equals("false")) sb.append("00");
                else throw new IllegalArgumentException("unexpected boolean syntax");
                break;
            default:
                sb.append(val);
            }
            sb.append('\'');
            i = prevIdx;
        }
        return sb.append(')').toString();
    }

    private static byte[] serializeBigInteger(BigInteger val) {
        if(val.signum() > 0) {
            return Integers.toBytesUnsigned(val);
        }
        if(val.signum() != 0) {
            final byte[] bytes = val.toByteArray();
            return signExtendNegative(bytes);
        }
        return EMPTY_BYTE_ARRAY;
    }

    private static byte[] signExtendNegative(final byte[] negative) {
        final byte[] extended = new byte[UnitType.UNIT_LENGTH_BYTES];
        Arrays.fill(extended, (byte) 0xff);
        System.arraycopy(negative, 0, extended, UnitType.UNIT_LENGTH_BYTES - negative.length, negative.length);
        return extended;
    }
}
