package org.practice.model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public abstract class CsvModel<T> implements Entity {

    /**
     * @param formattedProperties properties that come from the string (file string).
     *                            Order of the properties must be equal to the order of the {@link T} properties
     * @return {@link T} object, which should be built using {@code List<String> formattedProperties}
     */
    abstract T buildFromList(List<String> formattedProperties);
    abstract int propertiesLength();

    public T fromString(String line) {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();

        try (CSVReader csvReader = new CSVReaderBuilder(
                new StringReader(line)).withCSVParser(parser).build()) {

            String[] properties = csvReader.readNext();
            int actualLength = propertiesLength();

            if (properties == null || properties.length != actualLength) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }

            List<String> formattedProperties = Arrays.stream(properties).map(String::trim).toList();
            return buildFromList(formattedProperties);
        }
        catch (CsvValidationException | IOException e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + line, e);
        }
    }
}
