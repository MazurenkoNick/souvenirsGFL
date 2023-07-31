package org.practice;

import org.junit.Test;
import org.practice.model.Souvenir;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

public class SouvenirTest {

    @Test
    public void testFromString() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String line = "0,Toy,2022-02-02,200.00,0";

        Souvenir expected = Souvenir.builder()
                .id(0)
                .name("Toy")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(200.00)
                .producerId(0)
                .build();
        Souvenir actual = Souvenir.fromString(line);

        assertEquals("Souvenirs must be equal", expected, actual);
    }
}
