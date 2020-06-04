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

import org.junit.jupiter.api.Test;

import static com.esaulpaugh.headlong.cli.MainTest.MACHINE_SERIALIZATION;
import static com.esaulpaugh.headlong.cli.MainTest.SERIALIZATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RLPTest {

//    java -jar headlong-cli-0.4-SNAPSHOT-interface.jar -e "(bool)" "('00')"
//    0000000000000000000000000000000000000000000000000000000000000000
//
//    java -jar headlong-cli-0.4-SNAPSHOT-interface.jar -e "(bool)" "('0000')"
//    0000000000000000000000000000000000000000000000000000000000000001


    @Test
    public void testEncode() {

        String[] re = new String[] { "-re", SERIALIZATION };

        assertEquals(MACHINE_SERIALIZATION, Main.eval(re));
    }

    @Test
    public void testDecode() {

        String[] rd = new String[] { "-rd", MACHINE_SERIALIZATION };

        assertEquals(SERIALIZATION, Main.eval(rd));

        String[] rdc = new String[] { "-rdc", MACHINE_SERIALIZATION };

        assertEquals(SERIALIZATION.replaceAll("[\n ]", ""), Main.eval(rdc));
    }
}
