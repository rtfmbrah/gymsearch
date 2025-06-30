package de.gymbro.gymsearch.api;

import de.gymbro.gymsearch.api.model.Offer;
import de.gymbro.gymsearch.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.gymbro.gymsearch.api.mapping.OfferMapper.mapFromDomain;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PriceController {
    private PriceService priceService;

    @GetMapping("/rewe/discounts/{marketId}")
    public ResponseEntity<List<Offer>> getDiscounts(@PathVariable String marketId) {
        List<Offer> response = mapFromDomain(priceService.getReweDiscounts(marketId));

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
