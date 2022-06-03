/*
   Copyright 2022 Evan Saulpaugh

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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    private static final String FUNCTION_JSON = "{\n" +
            "  \"type\": \"function\",\n" +
            "  \"name\": \"foo\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"name\": \"complex_nums\",\n" +
            "      \"type\": \"tuple[][]\",\n" +
            "      \"components\": [\n" +
            "        {\n" +
            "          \"name\": \"real\",\n" +
            "          \"type\": \"fixed168x10\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"name\": \"imaginary\",\n" +
            "          \"type\": \"fixed168x10\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"outputs\": [\n" +
            "    {\n" +
            "      \"name\": \"count\",\n" +
            "      \"type\": \"uint64\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String EVENT_JSON = "{\n" +
            "    \"type\": \"event\",\n" +
            "    \"name\": \"an_event\",\n" +
            "    \"inputs\": [\n" +
            "      {\n" +
            "        \"name\": \"a\",\n" +
            "        \"type\": \"bytes\",\n" +
            "        \"indexed\": true\n" +
            "      },\n" +
            "      {\n" +
            "        \"name\": \"b\",\n" +
            "        \"type\": \"uint256\",\n" +
            "        \"indexed\": false\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    private static final String ERROR_JSON = "{\n" +
            "  \"type\": \"error\",\n" +
            "  \"name\": \"InsufficientBalance\",\n" +
            "  \"inputs\": [\n" +
            "    {\n" +
            "      \"name\": \"available\",\n" +
            "      \"type\": \"uint256\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"required\",\n" +
            "      \"type\": \"uint256\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void testParseABIJson() {
        assertEquals(
                "FUNCTION foo((fixed168x10,fixed168x10)[][]) names:(complex_nums) returns: (uint64) stateMutability: null",
                Main.eval(new String[] { "-parseabijson", FUNCTION_JSON })
        );
        assertEquals(
                "event an_event(bytes,uint256) names:(a,b) indexed:[true, false]",
                Main.eval(new String[] { "-parseabijson", EVENT_JSON })
        );
        assertEquals(
                "error InsufficientBalance(uint256,uint256) names:(available,required)",
                Main.eval(new String[] { "-parseabijson", ERROR_JSON })
        );
    }

    @Test
    public void testJsonToSig() {
        assertEquals(
                "foo((fixed168x10,fixed168x10)[][])",
                Main.eval(new String[] { "-jsontosig", FUNCTION_JSON })
        );
        assertEquals(
                "an_event(bytes,uint256)",
                Main.eval(new String[] { "-jsontosig", EVENT_JSON })
        );
        assertEquals(
                "InsufficientBalance(uint256,uint256)",
                Main.eval(new String[] { "-jsontosig", ERROR_JSON })
        );
    }
}
