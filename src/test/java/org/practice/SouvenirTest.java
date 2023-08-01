package org.practice;

import org.junit.jupiter.api.Test;
import org.practice.model.Souvenir;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SouvenirTest {

    @Test
    public void testFromString() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String line = "0,Toy,2022-02-02,200.00,0";

        Souvenir expected = Souvenir.builder()
                .id(0L)
                .name("Toy")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(200.00)
                .producerId(0L)
                .build();
        Souvenir actual = Souvenir.builder().build().fromString(line);

        assertEquals(expected, actual, "Souvenirs must be equal");
    }
}
