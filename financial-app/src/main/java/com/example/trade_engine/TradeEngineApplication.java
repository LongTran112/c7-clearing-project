package com.example.trade_engine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@RestController
public class TradeEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeEngineApplication.class, args);
    }

    private final Counter tradeCounter;
    private final Counter failedTradeCounter;

    // Constructor Injection for the Metrics Registry
    public TradeEngineApplication(MeterRegistry registry) {
        this.tradeCounter = registry.counter("clearing_trades_success_total");
        this.failedTradeCounter = registry.counter("clearing_trades_failed_total");
    }

    @PostMapping("/api/clear")
    public Map<String, Object> clearTrade(@RequestBody Map<String, Object> tradeData) {
        // Simulate "Business Logic"
        // If the trade amount is negative, reject it.
        if (tradeData.containsKey("amount") && (int) tradeData.get("amount") < 0) {
            failedTradeCounter.increment();
            return Map.of("status", "REJECTED", "reason", "Negative Amount");
        }

        // Otherwise, clear it
        tradeCounter.increment();
        return Map.of(
            "status", "CLEARED",
            "id", new Random().nextInt(100000),
            "timestamp", java.time.Instant.now().toString()
        );
    }
}