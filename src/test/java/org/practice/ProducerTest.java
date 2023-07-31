package org.practice;

import org.junit.Test;
import org.practice.model.Producer;

import static org.junit.Assert.assertEquals;

public class ProducerTest {

    @Test
    public void testFromString() {
        String line = "0, Ivan Production, Ukraine, 444453529002";

        Producer expected = Producer.builder()
                .id(0)
                .name("Ivan Production")
                .country("Ukraine")
                .details("444453529002")
                .build();
        Producer actual = Producer.builder().build().fromString(line);

        assertEquals("Producers must be equal", expected, actual);
    }
}
