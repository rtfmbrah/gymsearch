package de.gymbro.gymsearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gymbro.gymsearch.domain.Offer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PriceService {
    private RestTemplate reweSslRestTemplate;
    private ObjectMapper objectMapper;

    public List<Offer> getReweDiscounts(String marketId) {
        String reweResponse = reweSslRestTemplate.getForObject("stationary-app-offers/" + marketId, String.class);

        if (reweResponse == null) {
            return Collections.emptyList();
        }

        JsonNode responseDom;

        try {
            responseDom = objectMapper.readTree(reweResponse);
        } catch (JsonProcessingException e) {
            log.error("Could not parse response from REWE API", e);
            return null;
        }

        JsonNode offersCategories = responseDom
                .get("data")
                .get("offers")
                .get("categories");

        if (offersCategories.isArray()) {
            return getOffersList(offersCategories);
        }

        return null;
    }

    private List<Offer> getOffersList(JsonNode offersCategories) {
        List<Offer> offersList = new ArrayList<>();

        offersCategories.forEach(category -> {
            String id = category.get("id").asText();

            JsonNode offers = category.get("offers");
            offers.forEach(offer -> {
                String title = offer.get("title").asText();
                String subtitle = offer.get("subtitle").asText();
                String price = offer.get("priceData").get("price").asText();

                offersList.add(
                        Offer.builder()
                                .category(id)
                                .title(title)
                                .subtitle(subtitle)
                                .price(price)
                                .build()
                );
            });
        });

        return offersList;
    }
}
