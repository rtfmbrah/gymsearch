package de.gymbro.gymsearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gymbro.gymsearch.domain.Market;
import de.gymbro.gymsearch.domain.Offer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static de.gymbro.gymsearch.configuration.Config.API_HOST;
import static de.gymbro.gymsearch.configuration.Config.CLIENT_HOST;

@Slf4j
@Service
@AllArgsConstructor
public class ReweService {
    private static final String DISCOUNTS_PATH = "stationary-app-offers/";
    private static final String SEARCH_PATH = "v3/market/search?";

    private RestTemplate reweSslRestTemplate;
    private ObjectMapper objectMapper;

    public List<Offer> getReweDiscounts(String marketId) {
        String reweResponse = reweSslRestTemplate.getForObject("https://" + CLIENT_HOST + "/api/" + DISCOUNTS_PATH + marketId, String.class);

        if (reweResponse == null) {
            return null;
        }

        JsonNode responseDom = getResponseDom(reweResponse);

        if (responseDom == null) {
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

    public List<Market> getMarkets(String searchQuery) {
        String query = "search=" + URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        String reweResponse = reweSslRestTemplate.getForObject("https://" + API_HOST + "/api/" + SEARCH_PATH + query, String.class);

        JsonNode responseDom = getResponseDom(reweResponse);

        if (responseDom == null) {
            return null;
        }

        JsonNode markets = responseDom.get("markets");

        if (markets.isArray()) {
            return getMarketsList(markets);
        }

        return null;
    }

    private List<Market> getMarketsList(JsonNode markets) {
        List<Market> marketList = new ArrayList<>();

        markets.forEach(market -> {
            String id = market.get("id").asText();
            String name = market.get("name").asText();
            String addressLine = market.get("addressLine1").asText() + ", " + market.get("addressLine2").asText();
            String latitude = market.get("location").get("latitude").asText();
            String longitude = market.get("location").get("longitude").asText();

            marketList.add(
                    Market.builder()
                            .id(id)
                            .name(name)
                            .addressLine(addressLine)
                            .latitude(latitude)
                            .longitude(longitude)
                            .build()
            );
        });

        return marketList;
    }

    private JsonNode getResponseDom(String reweResponse) {
        try {
            return objectMapper.readTree(reweResponse);
        } catch (JsonProcessingException e) {
            log.error("Could not parse response from REWE API", e);
            return null;
        }
    }
}
