package com.example.truckapp;

import java.io.Serializable;

public class OwnerModel implements Serializable {
    private String owner, name, date, time, address, type, weight, length, width, height, truck, id, destination;

    public OwnerModel(String owner, String name, String date, String time, String address, String destination, String type, String weight, String length, String width, String height, String truck, String id) {
        this.owner = owner;
        this.name = name;
        this.date = date;
        this.time = time;
        this.address = address;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.truck = truck;
        this.id = id;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public OwnerModel() {
        this.owner = "";
        this.name = "";
        this.date = "";
        this.time = "";
        this.address = "";
        this.type = "";
        this.weight = "";
        this.length = "";
        this.width = "";
        this.height = "";
        this.truck = "";
        this.id = "";
        this.destination = "";
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
