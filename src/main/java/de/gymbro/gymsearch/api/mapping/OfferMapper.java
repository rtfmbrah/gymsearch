package de.gymbro.gymsearch.api.mapping;

import de.gymbro.gymsearch.api.model.Offer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OfferMapper {
    private OfferMapper() {}

    public static List<Offer> mapFromDomain(List<de.gymbro.gymsearch.domain.Offer> offers) {
        if (offers == null) {
            return null;
        }

        return offers.stream()
                .map(OfferMapper::mapFromDomain)
                .toList();
    }

    public static Offer mapFromDomain(de.gymbro.gymsearch.domain.Offer offer) {
        return Offer.builder()
                .category(offer.getCategory())
                .title(offer.getTitle())
                .subtitle(offer.getSubtitle())
                .price(offer.getPrice())
                .build();
    }
}
