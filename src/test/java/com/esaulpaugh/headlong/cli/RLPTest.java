package com.esaulpaugh.headlong.cli;

import org.junit.jupiter.api.Test;

import static com.esaulpaugh.headlong.cli.MainTest.MACHINE_SERIALIZATION;
import static com.esaulpaugh.headlong.cli.MainTest.SERIALIZATION;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RLPTest {

    @Test
    public void testEncode() {

        String[] re = new String[] { "-re", SERIALIZATION };

        assertEquals(MACHINE_SERIALIZATION, Main.eval(re));
    }

    @Test
    public void testDecode() {

        String[] re = new String[] { "-rd", MACHINE_SERIALIZATION };

        assertEquals(SERIALIZATION, Main.eval(re));
    }
}
