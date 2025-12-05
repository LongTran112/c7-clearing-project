package com.example.service;

import com.example.model.TradeTransaction;
import org.springframework.stereotype.Service;

@Service
public class ClearingService {

    public TradeTransaction clearTrade(TradeTransaction trade) {
        // Simulate business logic
        trade.setStatus("CLEARED");
        return trade;
    }
}

