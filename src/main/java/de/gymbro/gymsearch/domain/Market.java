package de.gymbro.gymsearch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Market {
    private String id;
    private String name;
    private String addressLine;
    private String latitude;
    private String longitude;
}
