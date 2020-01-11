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

    public static JsonArray serializeValues(Tuple tuple) {
        JsonArray valuesArray = new JsonArray();
        for(Object val : tuple) {
            valuesArray.add(toJsonElement(val));
        }
        return valuesArray;
    }

    private static JsonElement toJsonElement(Object val) {
        if(val.getClass().isArray()) {
            JsonArray array = new JsonArray();
            if(val instanceof boolean[]) {
                for(boolean e : (boolean[]) val) array.add(toJsonElement(e));
            } else if(val instanceof int[]) {
                for(int e : (int[]) val) array.add(toJsonElement(e));
            } else if(val instanceof long[]) {
                for(long e : (long[]) val) array.add(toJsonElement(e));
                return array;
            } else if(val instanceof Object[]) {
                for(Object e : (Object[]) val) array.add(toJsonElement(e));
            } else {
                throw new Error();
            }
            return array;
        }
        JsonObject object = new JsonObject();
        if(val instanceof Boolean) {
            object.add("type", new JsonPrimitive("bool"));
            object.add("value", new JsonPrimitive(val.toString()));
        } else if(val instanceof Integer || val instanceof Long) {
            object.add("type", new JsonPrimitive("number"));
            object.add("value", new JsonPrimitive(val.toString()));
        } else if(val instanceof BigInteger) {
            object.add("type", new JsonPrimitive("string"));
            object.add("value", new JsonPrimitive("0x" + FastHex.encodeToString(((BigInteger) val).toByteArray())));
        } else if(val instanceof BigDecimal) {
            object.add("type", new JsonPrimitive("number"));
            object.add("value", new JsonPrimitive(((BigDecimal) val).unscaledValue().toString()));
        } else if(val instanceof byte[]) {
            object.add("type", new JsonPrimitive("buffer"));
            object.add("value", new JsonPrimitive("0x" + FastHex.encodeToString((byte[]) val)));
        } else if(val instanceof String) {
            object.add("type", new JsonPrimitive("buffer"));
            object.add("value", new JsonPrimitive((String) val));
        } else if(val instanceof Tuple) {
            object.add("type", new JsonPrimitive("tuple"));
            JsonArray array = new JsonArray();
            for(Object e : (Tuple) val) {
                array.add(toJsonElement(e));
            }
            object.add("value", array);
        } else {
            throw new Error();
        }
        return object;
    }
}
