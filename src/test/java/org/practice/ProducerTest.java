package org.practice;

import org.junit.jupiter.api.Test;
import org.practice.model.Producer;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProducerTest {

    @Test
    public void testFromString() {
        String line = "0, Ivan Production, Ukraine, 444453529002";

        Producer expected = Producer.builder()
                .id(0L)
                .name("Ivan Production")
                .country("Ukraine")
                .details("444453529002")
                .build();
        Producer actual = Producer.builder().build().fromCsvString(line, Producer.class);

        assertEquals(expected, actual, "Producers must be equal");
    }
}
