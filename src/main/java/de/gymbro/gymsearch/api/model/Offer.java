package de.gymbro.gymsearch.api.model;

import lombok.Builder;

@Builder
public record Offer(String category, String title, String subtitle, String price) {}
