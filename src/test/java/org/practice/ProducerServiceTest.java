package org.practice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.practice.model.Producer;
import org.practice.service.ProducerService;
import org.practice.service.SouvenirService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProducerServiceTest {

    private static ProducerService producerService;
    private static SouvenirService souvenirService;

    @BeforeAll
    public static void setUp() {
        producerService = ProducerService.getInstance();
        souvenirService = SouvenirService.getInstance();
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testReadProducersWithPricesLessThan")
    public void testReadProducersWithPricesLessThan(List<Producer> expected, double price) {
        List<Producer> actual = producerService.readProducersWithPricesLessThan(price);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testReadProducersWithYear")
    public void testReadProducersWithYear(List<Producer> expected, int year) {
        List<Producer> actual = producerService.readProductsWithYear(year);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testDeleteTestDeleteProductAndSouvenirs")
    public void testDeleteTestDeleteProductAndSouvenirs(long id) {
        assertTrue(producerService.deleteProductAndSouvenirs(id));
        assertNull(producerService.read(id));
        assertTrue(souvenirService.readAll(s -> s.getProducerId().equals(id)).isEmpty());
    }
}
