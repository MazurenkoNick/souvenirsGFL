package org.practice.model;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.Builder;
import lombok.Data;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class Souvenir {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private long id;
    private String name;
    private Date manufacturingDate;
    private double price;
    private long producerId;

    public static Souvenir fromString(String line) {
        CSVParser parser = new CSVParserBuilder().withSeparator(',').build();

        try (CSVReader csvReader = new CSVReaderBuilder(
                new StringReader(line)).withCSVParser(parser).build()) {

            String[] fields = csvReader.readNext();
            if (fields == null || fields.length != 5) {
                throw new IllegalArgumentException("Invalid CSV line: " + line);
            }

            List<String> formattedFields = Arrays.stream(fields).map(String::trim).toList();
            return buildFromList(formattedFields);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error parsing CSV line: " + line, e);
        }
    }

    private static Souvenir buildFromList(List<String> formattedFields) throws ParseException {
        long id = Long.parseLong(formattedFields.get(0));
        String name = formattedFields.get(1);
        Date manufacturingDate = new SimpleDateFormat(DATE_FORMAT).parse(formattedFields.get(2));
        double price = Double.parseDouble(formattedFields.get(3));
        long producerId = Long.parseLong(formattedFields.get(4));

        return Souvenir.builder()
                .id(id)
                .name(name)
                .manufacturingDate(manufacturingDate)
                .price(price)
                .producerId(producerId)
                .build();
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return String.format("%d, %s, %s, %f, %d",
                id, name, formatter.format(manufacturingDate), price, producerId
        );
    }
}
