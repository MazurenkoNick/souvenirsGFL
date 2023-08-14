package org.practice;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.practice.filerepository.SouvenirFileRepository;
import org.practice.model.Souvenir;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

public class SouvenirFileRepositoryTest {

    private static SouvenirFileRepository souvenirFileRepository;
    private double price = 350;

    @BeforeAll
    public static void setUp() {
        souvenirFileRepository = SouvenirFileRepository.getInstance();
    }

    @Test
    public void addTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Souvenir expected = Souvenir.builder()
                .id(0L)
                .name("Porsche")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(price + 10000)
                .producerId(0L)
                .build();
        Souvenir actual = souvenirFileRepository.add(expected);

        assertNotEquals(0, actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getManufacturingDate(), actual.getManufacturingDate());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    public void readTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Souvenir expected = Souvenir.builder()
                .id(3954570612593142035L)
                .name("Big Toy")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(350)
                .producerId(0L)
                .build();
        Souvenir actual = souvenirFileRepository.read(3954570612593142035L);

        assertEquals(expected, actual, "Souvenirs must be equal. Make sure that expected souvenir exists in the file");
        assertNull(souvenirFileRepository.read(-10));
    }

    @Test
    public void updateTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Souvenir expected = Souvenir.builder()
                .id(3954570612593142035L)
                .name("Big Toy")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(price)
                .producerId(0L)
                .build();

        souvenirFileRepository.update(expected);
        Souvenir actual = souvenirFileRepository.read(3954570612593142035L);

        assertEquals(expected, actual, "Souvenirs must be equal. Make sure that expected souvenir exists in the file");
        assertFalse(souvenirFileRepository.update(new Souvenir()));
    }

    @Test
    public void addAndDeleteTest() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Souvenir expected = Souvenir.builder()
                .id(0L)
                .name("Porsche")
                .manufacturingDate(dateFormat.parse("2022-02-02"))
                .price(price + 10000)
                .producerId(0L)
                .build();
        Souvenir actual = souvenirFileRepository.add(expected);

        assertTrue(souvenirFileRepository.delete(actual.getId()));
        assertNull(souvenirFileRepository.read(actual.getId()));
        assertFalse(souvenirFileRepository.delete(-10L));
    }
}
