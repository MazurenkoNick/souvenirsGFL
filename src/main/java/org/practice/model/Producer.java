package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producer extends CsvModel<Producer> {

    private Long id;
    private String name;
    private String country;
    private String details;

    @Override
    Producer buildFromList(List<String> formattedFields) {
        this.id = Long.parseLong(formattedFields.get(0));
        this.name = formattedFields.get(1);
        this.country = formattedFields.get(2);
        this.details = formattedFields.get(3);
        return this;
    }

    @Override
    int propertiesLength() {
        return Producer.class.getDeclaredFields().length;
    }

    @Override
    public String format() {
        return String.format("%d, %s, %s, %s",
                id, name, country, details
        );
    }
}
