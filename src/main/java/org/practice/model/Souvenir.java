package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.practice.annotation.Property;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Souvenir extends CsvModel<Souvenir> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    @Property
    private Long id;
    @Property
    private String name;
    @Property
    private Date manufacturingDate;
    @Property
    private double price;
    @Property
    private Long producerId;

    public int getManufacturingYear() {
        return getManufacturingDate().getYear() + 1900;
    }
}
