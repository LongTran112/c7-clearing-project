package com.example.model;

public class TradeTransaction {

    private String id;

    private String instrument; // e.g., "DE000ABC123"

    private double quantity;

    private double price;

    private String marketType; // "CASH" or "REPO" (Keywords from JD!)

    private String status;

    // Constructors
    public TradeTransaction() {
    }

    public TradeTransaction(String id, String instrument, double quantity, double price, String marketType, String status) {
        this.id = id;
        this.instrument = instrument;
        this.quantity = quantity;
        this.price = price;
        this.marketType = marketType;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

