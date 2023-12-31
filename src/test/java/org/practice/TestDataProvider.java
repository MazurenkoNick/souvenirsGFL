package org.practice;

import org.junit.jupiter.params.provider.Arguments;
import org.practice.model.Producer;
import org.practice.model.Souvenir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Stream;

public class TestDataProvider {

    public static Stream<Arguments> testReadProducersWithPricesLessThan() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Producer(0L, "Ivan Production",
                                        "Ukraine", "444453529002")
                        ),
                        351
                )
        );
    }

    public static Stream<Arguments> testReadProducersWithYear() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Producer(0L, "Ivan Production",
                                        "Ukraine", "444453529002")
                        ),
                        2019
                )
        );
    }

    public static Stream<Arguments> testDeleteTestDeleteProductAndSouvenirs() {
        return Stream.of(
                Arguments.of(
                        2
                )
        );
    }

    public static Stream<Arguments> testReadAllByYear() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Stream.of(
                Arguments.of(
                        List.of(
                            new Souvenir(2202499513421820009L, "Porsche",
                                    dateFormat.parse("2029-02-02"), 10350, 1L),
                            new Souvenir(2202499513421820010L, "Porsche",
                                    dateFormat.parse("2029-02-02"), 10350, 1L)
                        ),
                        2029
                )
        );
    }

    public static Stream<Arguments> testReadAllByCountry() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Souvenir(2202499513421820013L, "Porsche",
                                        dateFormat.parse("2011-02-02"), 10350, 3L),
                                new Souvenir(2202499513421820014L, "Porsche",
                                        dateFormat.parse("2011-02-02"), 10350, 3L)
                        ),
                        "Poland"
                )
        );
    }

    public static Stream<Arguments> testReadAllByProducerId() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Souvenir(2202499513421820013L, "Porsche",
                                        dateFormat.parse("2011-02-02"), 10350, 3L),
                                new Souvenir(2202499513421820014L, "Porsche",
                                        dateFormat.parse("2011-02-02"), 10350, 3L)
                        ),
                        3L
                )
        );
    }
}
