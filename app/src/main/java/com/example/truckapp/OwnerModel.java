package com.example.truckapp;

import java.io.Serializable;

public class OwnerModel implements Serializable {
    private String owner, name, date, time, pickupAddress, pickupLat, pickupLong, deliveryLat, deliverLong, type, weight, length, width, height, truck, id, deliveryAddress;

    public OwnerModel(String owner, String name, String date, String time, String pickupAddress, String deliveryAddress, String deliveryLong, String deliverLat, String pickupLat, String pickupLong, String type, String weight, String length, String width, String height, String truck, String id) {
        this.owner = owner;
        this.name = name;
        this.date = date;
        this.time = time;
        this.pickupAddress = pickupAddress;
        this.pickupLat = getPickupLat();
        this.pickupLong = pickupLong;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.truck = truck;
        this.id = id;
        this.deliveryAddress = deliveryAddress;
        this.deliveryLat = deliverLat;
        this.deliverLong = deliveryLong;
    }

    public String getDestination() {
        return deliveryAddress;
    }

    public void setDestination(String destination) {
        this.deliverLong = destination;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(String pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(String deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public String getDeliverLong() {
        return deliverLong;
    }

    public void setDeliverLong(String deliverLong) {
        this.deliverLong = deliverLong;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OwnerModel() {
        this.owner = "";
        this.name = "";
        this.date = "";
        this.time = "";
        this.pickupAddress = "";
        this.type = "";
        this.weight = "";
        this.length = "";
        this.width = "";
        this.height = "";
        this.truck = "";
        this.id = "";
        this.deliveryAddress = "";
        this.pickupLat = "";
        this.pickupLong = "";
        this.deliveryLat = "";
        this.deliverLong = "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }
}
