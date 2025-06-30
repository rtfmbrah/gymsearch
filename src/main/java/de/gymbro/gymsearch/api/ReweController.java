package de.gymbro.gymsearch.api;

import de.gymbro.gymsearch.api.model.Market;
import de.gymbro.gymsearch.api.model.Offer;
import de.gymbro.gymsearch.service.ReweService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static de.gymbro.gymsearch.api.ReweMapper.mapMarkets;

@RestController
@RequestMapping("/api/rewe")
@AllArgsConstructor
public class ReweController {
    private ReweService reweService;

    @GetMapping("/discounts/{marketId}")
    public ResponseEntity<List<Offer>> getDiscounts(@PathVariable String marketId) {
        List<Offer> response = ReweMapper.mapOffers(reweService.getReweDiscounts(marketId));

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/markets/{searchQuery}")
    public ResponseEntity<List<Market>> getMarkets(@PathVariable String searchQuery) {
        List<Market> response = mapMarkets(reweService.getMarkets(searchQuery));

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
