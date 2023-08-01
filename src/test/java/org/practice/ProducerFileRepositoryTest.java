package org.practice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.practice.filerepository.ProducerFileRepository;
import org.practice.model.Producer;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerFileRepositoryTest {

    private static ProducerFileRepository producerFileRepository;

    @BeforeAll
    public static void setUp() {
        producerFileRepository = ProducerFileRepository.getInstance();
    }

    @Test
    public void addTest() {
        Producer expected = Producer.builder()
                .name("Volkswagen")
                .details("44477737333")
                .country("Germany")
                .build();

        Producer actual = producerFileRepository.add(expected);

        assertNotEquals(0L, actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDetails(), actual.getDetails());
        assertEquals(expected.getCountry(), actual.getCountry());
    }

    @Test
    public void readTest() {
        Producer expected = Producer.builder()
                .id(0L)
                .name("Ivan Production")
                .details("444453529002")
                .country("Ukraine")
                .build();

        Producer actual = producerFileRepository.read(0);

        assertEquals(expected, actual, "Producers must be equal. Make sure that expected producer exists in the file");
        assertNull(producerFileRepository.read(-10));
    }

    @Test
    public void updateTest() {
        // 0, Ivan Production, Ukraine, 444453529002
        Producer expected = Producer.builder()
                .id(0L)
                .name("Ivan Production")
                .details("444453529002")
                .country("Ukraine")
                .build();

        assertTrue(producerFileRepository.update(expected));

        Producer actual = producerFileRepository.read(0);

        assertEquals(expected, actual, "Producers must be equal. Make sure that expected producer exists in the file");
        assertFalse(producerFileRepository.update(new Producer()));
    }

    @Test
    public void addAndDeleteTest() throws ParseException {
        Producer expected = Producer.builder()
                .name("Volkswagen")
                .details("44477737333")
                .country("Germany")
                .build();

        Producer actual = producerFileRepository.add(expected);

        assertTrue(producerFileRepository.delete(actual.getId()));
        assertFalse(producerFileRepository.delete(-10));
    }
}
