package com.example.theaterapp;

public class Ticket {
    private String id;
    private String name;
    private String seatNumber;
    private String price;

    public Ticket(){}

    public Ticket(String name, String seatNumber, String price) {
        this.name = name;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
