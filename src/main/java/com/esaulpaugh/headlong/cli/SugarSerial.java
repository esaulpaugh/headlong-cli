/*
   Copyright 2020 Evan Saulpaugh

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
//package com.esaulpaugh.headlong.cli;
//
//import com.esaulpaugh.headlong.abi.ABIType;
//import com.esaulpaugh.headlong.abi.ArrayType;
//import com.esaulpaugh.headlong.abi.Tuple;
//import com.esaulpaugh.headlong.abi.TupleType;
//import com.esaulpaugh.headlong.abi.TypeFactory;
//import com.esaulpaugh.headlong.util.Strings;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//public class SugarSerial {
//
//    static final String BEGIN_LIST = "[";
//    static final String END_LIST = "]";
//    static final String BEGIN_STRING = "'";
//    static final String END_STRING = "'";
//
//    private static final int LIST = 0;
//    private static final int STRING = 1;
//
//    private static final int LIST_PREFIX_LEN = BEGIN_LIST.length();
//    private static final int LIST_SUFFIX_LEN = END_LIST.length();
//    private static final int STRING_PREFIX_LEN = BEGIN_STRING.length();
//    private static final int STRING_SUFFIX_LEN = END_STRING.length();
//
//    private static Tuple deserial(String sugar) {
//        List<Object> elements = parse(sugar);
//        return new Tuple(elements.toArray());
//    }
//
//    public static List<Object> parse(String notation) {
//        if(notation.charAt(0) != '[') throw new IllegalArgumentException("must start with [");
//        if(notation.charAt(notation.length() - 1) != ']') throw new IllegalArgumentException("must end with ]");
//        List<Object> topLevelObjects = new ArrayList<>(); // a sequence (as in encodeSequentially)
//        parse(notation, 1, notation.length() - 1, topLevelObjects, new int[2]);
//        return topLevelObjects; // (List<Object>) topLevelObjects.get(0);
//    }
//
//    private static int parse(String notation, int i, final int end, List<Object> parent, int[] resultHolder) {
//
//        int nextArrayEnd = -1;
//
//        while (i < end) {
//            if(!findNextObject(notation, i, resultHolder)) {
//                return Integer.MAX_VALUE;
//            }
//
//            if(i > nextArrayEnd) { // only update nextArrayEnd when i has passed it
//                nextArrayEnd = notation.indexOf(END_LIST, i);
//                if(nextArrayEnd < 0) {
//                    nextArrayEnd = Integer.MAX_VALUE;
//                }
//            }
//
//            int nextObjectIndex = resultHolder[0];
//
//            if(nextArrayEnd < nextObjectIndex) {
//                return nextArrayEnd + LIST_SUFFIX_LEN;
//            }
//
//            if(STRING == resultHolder[1] /* nextObjectType */) {
//                int datumStart = nextObjectIndex + STRING_PREFIX_LEN;
//                int datumEnd = notation.indexOf(END_STRING, datumStart);
//                if(datumEnd < 0) {
//                    throw new IllegalArgumentException("unterminated string @ " + datumStart);
//                }
//                String typeStr = notation.substring(datumStart, datumEnd);
//                System.out.println("typeStr = " + typeStr);
//                i = datumEnd + STRING_SUFFIX_LEN;
//                findNextObject(notation, i, resultHolder);
//                nextObjectIndex = resultHolder[0];
//                datumStart = nextObjectIndex + STRING_PREFIX_LEN;
//                datumEnd = notation.indexOf(END_STRING, datumStart);
//                if(datumEnd < 0) {
//                    throw new IllegalArgumentException("unterminated string @ " + datumStart);
//                }
//                String valueStr = notation.substring(datumStart, datumEnd);
//                Object value;
//                ABIType<?> type = TypeFactory.create(typeStr, null);
//                switch (type.typeCode()) {
//                case ABIType.TYPE_CODE_BOOLEAN:
//                case ABIType.TYPE_CODE_BYTE:
//                case ABIType.TYPE_CODE_INT:
//                case ABIType.TYPE_CODE_LONG:
//                case ABIType.TYPE_CODE_BIG_INTEGER:
//                case ABIType.TYPE_CODE_BIG_DECIMAL: value = type.parseArgument(valueStr); break;
//                case ABIType.TYPE_CODE_ARRAY: value = parseArray((ArrayType<?, ?>) type, valueStr); break;
//                case ABIType.TYPE_CODE_TUPLE:
//                default: value = null;
//                }
//                parent.add(value);
//                i = datumEnd + STRING_SUFFIX_LEN;
//            } else {
//                List<Object> childList = new ArrayList<>();
//                i = parse(notation, nextObjectIndex + LIST_PREFIX_LEN, end, childList, resultHolder);
//                parent.add(childList);
//            }
//        }
//
//        return end + LIST_SUFFIX_LEN;
//    }
//
//    private static Object parseArray(ArrayType<?, ?> type, String valueStr) {
//        switch (type.getElementType().typeCode()) {
//        case ABIType.TYPE_CODE_BOOLEAN: return parseBooleanArray(valueStr);
//        case ABIType.TYPE_CODE_BYTE: return Strings.decode(valueStr);
//        case ABIType.TYPE_CODE_INT: return parseIntArray(valueStr);
//        case ABIType.TYPE_CODE_LONG: return parseLongArray(valueStr);
//        case ABIType.TYPE_CODE_BIG_INTEGER: return parseBigIntArray(valueStr);
//        case ABIType.TYPE_CODE_BIG_DECIMAL: return parseBigDecimalArray(valueStr);
//        case ABIType.TYPE_CODE_ARRAY:
//        case ABIType.TYPE_CODE_TUPLE:
//        default: throw new Error();
//        }
//    }
//
//    private static void checkArrayStr(String valueStr, int open) {
//        if(valueStr.charAt(open) != '[') throw new IllegalArgumentException("array/tuple must open with [");
//        if(valueStr.charAt(valueStr.length() - 1) != ']') throw new IllegalArgumentException("array/tuple must end with ]");
//    }
//
//    private static int[] parseIntArray(String valueStr) {
//        List<Integer> ints = new ArrayList<>();
//        parse(valueStr, ints, Integer::parseInt);
//        int[] arr = new int[ints.size()];
//        int i = 0;
//        for(Integer e : ints) {
//            arr[i++] = e;
//        }
//        return arr;
//    }
//
//    private static long[] parseLongArray(String valueStr) {
//        List<Long> longs = new ArrayList<>();
//        parse(valueStr, longs, Long::parseLong);
//        long[] arr = new long[longs.size()];
//        int i = 0;
//        for(Long e : longs) {
//            arr[i++] = e;
//        }
//        return arr;
//    }
//
//    private static BigInteger[] parseBigIntArray(String valueStr) {
//        List<BigInteger> bigInts = new ArrayList<>();
//        parse(valueStr, bigInts, (s) -> new BigInteger(s, 10));
//        BigInteger[] arr = new BigInteger[bigInts.size()];
//        int i = 0;
//        for(BigInteger e : bigInts) {
//            arr[i++] = e;
//        }
//        return arr;
//    }
//
//    private static BigDecimal[] parseBigDecimalArray(String valueStr) {
//        List<BigDecimal> bigDecs = new ArrayList<>();
//        if(valueStr.startsWith("scale=")) {
//            int open = valueStr.indexOf('[');
//            final int scale = Integer.parseInt(valueStr.substring("scale=".length(), open));
//            parse(valueStr, open + 1, bigDecs, (String elementStr) -> new BigDecimal(new BigInteger(elementStr, 10), scale));
//        } else {
//            parse(valueStr, bigDecs, BigDecimal::new);
//        }
//        BigDecimal[] arr = new BigDecimal[bigDecs.size()];
//        int i = 0;
//        for(BigDecimal e : bigDecs) {
//            arr[i++] = e;
//        }
//        return arr;
//    }
//
//    private static <T extends Number> void parse(String valueStr, List<T> list, Function<String, T> elementParser) {
//        parse(valueStr, 1, list, elementParser);
//    }
//
//    private static <T extends Number> void parse(String valueStr, int start, List<T> list, Function<String, T> elementParser) {
//        checkArrayStr(valueStr, start - 1);
//        final int chars = valueStr.length();
//        while(true) {
//            int end = valueStr.indexOf(',', start);
//            if(end < 0) break;
//            list.add(elementParser.apply(valueStr.substring(start, end)));
//            start = end + 1;
//        }
//        if(chars != 2) {
//            list.add(elementParser.apply(valueStr.substring(start, valueStr.indexOf(']', start))));
//        }
//    }
//
//    private static boolean[] parseBooleanArray(String valueStr) {
//        final int len = valueStr.length();
//        final boolean[] arr = new boolean[len];
//        for (int i = 0; i < len; i++) {
//            char c = valueStr.charAt(i);
//            if(c == '1') {
//                arr[i] = true;
//            } else if(c != '0') {
//                throw new IllegalArgumentException("invalid boolean value");
//            }
//        }
//        return arr;
//    }
//
//    private static boolean findNextObject(String notation, int startIndex, int[] resultHolder) {
//        final int indexString = notation.indexOf(BEGIN_STRING, startIndex);
//        final int indexList = notation.indexOf(BEGIN_LIST, startIndex);
//        if(indexString == -1) {
//            if(indexList == -1) {
//                return false;
//            }
//        } else if(indexString < indexList || indexList == -1) {
//            resultHolder[0] = indexString;
//            resultHolder[1] = STRING;
//            return true;
//        }
//        // indexString == -1 || indexList <= indexString
//        resultHolder[0] = indexList;
//        resultHolder[1] = LIST;
//        return true;
//    }
//
//    public static void main(String[] args) {
//        String sugar = "[" +
//                "'int':'-30000'," +
//                "'bool[]':'00101'," +
//                "'bytes':'00ff019A'" +
//                "'decimal[]':'scale=10[3001,20,-300,411,2109999999]'" +
//                "'decimal[]':'[30.0123456789,20.0123456789,-300.0123456789,411.0123456789,2109999999.0123456789]'" +
//                "]";
//        try {
//            TupleType tt = TupleType.parse("(int,bool[],bytes,decimal[],decimal[])");
//            Tuple t = deserial(sugar);
//            tt.validate(t);
//            System.out.println(t);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//}
