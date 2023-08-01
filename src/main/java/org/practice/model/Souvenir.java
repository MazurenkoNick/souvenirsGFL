package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Souvenir extends CsvModel<Souvenir> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private Long id;
    private String name;
    private Date manufacturingDate;
    private double price;
    private Long producerId;

    @Override
    int propertiesLength() {
        return Souvenir.class.getDeclaredFields().length - 1;
    }

    @Override
    public Souvenir buildFromList(List<String> formattedFields) {
        Date manufacturingDate;
        try {
            manufacturingDate = new SimpleDateFormat(DATE_FORMAT).parse(formattedFields.get(2));
        } catch (ParseException e) {
            throw new IllegalArgumentException(
                    "Can't parse date into format yyyy-MM-dd: " + formattedFields.get(2)
            );
        }
        this.id = Long.parseLong(formattedFields.get(0));
        this.name = formattedFields.get(1);
        this.manufacturingDate = manufacturingDate;
        this.price = Double.parseDouble(formattedFields.get(3));
        this.producerId = Long.parseLong(formattedFields.get(4));
        return this;
    }

    @Override
    public String format() {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return String.format("%d, %s, %s, %f, %d",
                id, name, formatter.format(manufacturingDate), price, producerId
        );
    }
}
