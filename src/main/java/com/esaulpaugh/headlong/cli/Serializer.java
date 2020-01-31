package com.esaulpaugh.headlong.cli;

import com.esaulpaugh.headlong.abi.ABIType;
import com.esaulpaugh.headlong.abi.Tuple;
import com.esaulpaugh.headlong.abi.TupleType;
import com.esaulpaugh.headlong.util.FastHex;
import com.google.gson.*;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Serializer {

    public static JsonArray serializeTypes(TupleType tupleType) {
        JsonArray typesArray = new JsonArray();
        for(ABIType<?> type : tupleType) {
            typesArray.add(new JsonPrimitive(type.getCanonicalType().replace("(", "tuple(")));
        }
        return typesArray;
    }

//    public static JsonArray serializeValues(Tuple tuple) {
//        JsonArray valuesArray = new JsonArray();
//        for(Object val : tuple) {
//            valuesArray.add(toJsonElement(val));
//        }
//        return valuesArray;
//    }
//
//    private static JsonElement toJsonElement(Object val) {
//        if(val instanceof Boolean) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("bool"));
//            object.add("value", new JsonPrimitive(val.toString()));
//            return object;
//        } else if(val instanceof Integer || val instanceof Long) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("number"));
//            object.add("value", new JsonPrimitive(val.toString()));
//            return object;
//        } else if(val instanceof BigInteger) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("string"));
//            object.add("value", new JsonPrimitive("0x" + FastHex.encodeToString(((BigInteger) val).toByteArray())));
//            return object;
//        } else if(val instanceof BigDecimal) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("number"));
//            object.add("value", new JsonPrimitive(((BigDecimal) val).unscaledValue().toString()));
//            return object;
//        } else if(val instanceof byte[]) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("buffer"));
//            object.add("value", new JsonPrimitive("0x" + FastHex.encodeToString((byte[]) val)));
//            return object;
//        } else if(val instanceof String) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("buffer"));
//            object.add("value", new JsonPrimitive((String) val));
//            return object;
//        } else if(val instanceof boolean[]) {
//            JsonArray array = new JsonArray();
//            for(boolean e : (boolean[]) val) {
//                array.add(toJsonElement(e));
//            }
//            return array;
//        } else if(val instanceof int[]) {
//            JsonArray array = new JsonArray();
//            for(int e : (int[]) val) {
//                array.add(toJsonElement(e));
//            }
//            return array;
//        } else if(val instanceof long[]) {
//            JsonArray array = new JsonArray();
//            for(long e : (long[]) val) {
//                array.add(toJsonElement(e));
//            }
//            return array;
//        } else if(val instanceof Object[]) {
//            JsonArray array = new JsonArray();
//            for(Object e : (Object[]) val) {
//                array.add(toJsonElement(e));
//            }
//            return array;
//        } else if(val instanceof Tuple) {
//            JsonObject object = new JsonObject();
//            object.add("type", new JsonPrimitive("tuple"));
//            JsonArray array = new JsonArray();
//            for(Object e : (Tuple) val) {
//                array.add(toJsonElement(e));
//            }
//            object.add("value", array);
//            return object;
//        }
//        throw new Error();
//    }
}
