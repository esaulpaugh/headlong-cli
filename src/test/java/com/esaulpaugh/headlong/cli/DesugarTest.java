package com.esaulpaugh.headlong.cli;

import com.esaulpaugh.headlong.abi.Function;
import com.esaulpaugh.headlong.util.FastHex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DesugarTest {

    @Test
    public void testVector() {
        String[] command = new String[] { "-ef", "sam(bytes,bool,uint256[])", "(u'dave', '01', [ d'1', d'2', d'3' ])" };
        String out = Main.eval(command);
        assertEquals(
                "a5643bf2" +
                "0000000000000000000000000000000000000000000000000000000000000060" +
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "00000000000000000000000000000000000000000000000000000000000000a0" +
                "0000000000000000000000000000000000000000000000000000000000000004" +
                "6461766500000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000003" +
                "0000000000000000000000000000000000000000000000000000000000000001" +
                "0000000000000000000000000000000000000000000000000000000000000002" +
                "0000000000000000000000000000000000000000000000000000000000000003",
                out
        );
    }

}
