package org.practice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.practice.annotation.Property;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producer extends CsvModel<Producer> {

    @Property
    private Long id;
    @Property
    private String name;
    @Property
    private String country;
    @Property
    private String details;
}
