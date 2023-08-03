package org.practice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.practice.model.Souvenir;
import org.practice.service.SouvenirService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SouvenirServiceTest {

    private static SouvenirService souvenirService;

    @BeforeAll
    public static void setUp() {
        souvenirService = SouvenirService.getInstance();
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testReadAllByYear")
    public void testReadAllByYear(List<Souvenir> expected, int year) {
        List<Souvenir> actual = souvenirService.readAllByYear(year);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testReadAllByCountry")
    public void testReadAllByCountry(List<Souvenir> expected, String country) {
        List<Souvenir> actual = souvenirService.readAllByCountry(country);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("org.practice.TestDataProvider#testReadAllByProducerId")
    public void testReadAllByProducerId(List<Souvenir> expected, long producerId) {
        List<Souvenir> actual = souvenirService.readAllByProducerId(producerId);

        assertEquals(expected, actual);
    }
}
