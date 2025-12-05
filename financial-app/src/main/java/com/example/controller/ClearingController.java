package com.example.controller;

import com.example.model.TradeTransaction;
import com.example.service.ClearingService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clearing")
public class ClearingController {

    private final Counter clearRequests; // Prometheus Metric
    private final ClearingService clearingService;

    public ClearingController(MeterRegistry registry, ClearingService clearingService) {
        // This hits the "Prometheus" requirement
        this.clearRequests = registry.counter("clearing_trades_total");
        this.clearingService = clearingService;
    }

    @PostMapping("/submit")
    public ResponseEntity<TradeTransaction> clearTrade(@RequestBody TradeTransaction trade) {
        // Delegate to service layer
        TradeTransaction clearedTrade = clearingService.clearTrade(trade);
        clearRequests.increment(); // Count the trade for monitoring
        return ResponseEntity.ok(clearedTrade);
    }
}

