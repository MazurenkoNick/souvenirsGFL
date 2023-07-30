package org.practice.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Souvenir {

    private long id;
    private String name;
    private LocalDateTime manufacturingDate;
    private double price;
    private long producerId;
}
