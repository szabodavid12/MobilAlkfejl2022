package com.example.theaterapp;

public class Play {
    private String id;
    private String name;
    private String info;
    private String price;
    private String date;
    private float ratedInfo;
    private int imageResource;

    public Play(){}

    public Play(String name, String info, String price, float ratedInfo, int imageResource, String date) {
        this.name = name;
        this.info = info;
        this.price = price;
        this.date = date;
        this.ratedInfo = ratedInfo;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getRatedInfo() {
        return ratedInfo;
    }

    public void setRatedInfo(float ratedInfo) {
        this.ratedInfo = ratedInfo;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String _getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }
}
