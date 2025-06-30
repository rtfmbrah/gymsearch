package de.gymbro.gymsearch.api.model;

import lombok.Builder;

@Builder
public record Market(String id, String name, String addressLine, String latitude, String longitude) {
}
