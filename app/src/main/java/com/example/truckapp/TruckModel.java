package com.example.truckapp;

public class TruckModel {
    private String truckOwnerName;
    private String truckCapacity;
    private String truckCost;
    private String truckUrl;

    public TruckModel(String truckOwnerName, String truckCapacity, String truckCost, String truckUrl) {
        this.truckOwnerName = truckOwnerName;
        this.truckCapacity = truckCapacity;
        this.truckCost = truckCost;
        this.truckUrl = truckUrl;
    }
    public TruckModel() {
        this.truckOwnerName = "";
        this.truckCapacity = "";
        this.truckCost = "";
        this.truckUrl = "";
    }


    public String getTruckOwnerName() {
        return truckOwnerName;
    }

    public void setTruckOwnerName(String truckOwnerName) {
        this.truckOwnerName = truckOwnerName;
    }

    public String getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(String truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public String getTruckCost() {
        return truckCost;
    }

    public void setTruckCost(String truckCost) {
        this.truckCost = truckCost;
    }

    public String getTruckImage() {
        return truckUrl;
    }

    public void setTruckImage(String truckUrl) {
        this.truckUrl = truckUrl;
    }
}
