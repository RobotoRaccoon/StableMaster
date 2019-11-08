package me.robotoraccoon.stablemaster;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LangStringTest {

    @Test
    void testFormatting() {
        LangString msg = new LangString().append("%s = %d");
        msg.format("&b4", 2 + 2);

        assertAll(
            () -> assertEquals(msg.getMessage(), "&b4 = 4"),
            () -> assertEquals(msg.toString(), "§b4 = 4")
        );
    }
}
