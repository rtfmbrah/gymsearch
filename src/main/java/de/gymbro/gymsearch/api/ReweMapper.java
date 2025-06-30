package de.gymbro.gymsearch.api;

import de.gymbro.gymsearch.api.model.Market;
import de.gymbro.gymsearch.api.model.Offer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReweMapper {
    private ReweMapper() {}

    public static List<Offer> mapOffers(List<de.gymbro.gymsearch.domain.Offer> offers) {
        if (offers == null) {
            return null;
        }

        return offers.stream()
                .map(ReweMapper::mapOffer)
                .toList();
    }

    private static Offer mapOffer(de.gymbro.gymsearch.domain.Offer offer) {
        return Offer.builder()
                .category(offer.getCategory())
                .title(offer.getTitle())
                .subtitle(offer.getSubtitle())
                .price(offer.getPrice())
                .build();
    }

    public static List<Market> mapMarkets(List<de.gymbro.gymsearch.domain.Market> markets) {
        if (markets == null) {
            return null;
        }

        return markets.stream()
                .map(ReweMapper::mapMarket)
                .toList();
    }

     private static Market mapMarket(de.gymbro.gymsearch.domain.Market market) {
        return Market.builder()
                .id(market.getId())
                .name(market.getName())
                .addressLine(market.getAddressLine())
                .latitude(market.getLatitude())
                .longitude(market.getLongitude())
                .build();
     }
}
