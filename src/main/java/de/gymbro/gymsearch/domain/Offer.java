package de.gymbro.gymsearch.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Offer {
    private String category;
    private String title;
    private String subtitle;
    private String price;
}
