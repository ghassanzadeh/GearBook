package com.example.ghassanz_gearbook;
/*
    This class is to contain all the attributes for each gear and MainActivity class and GearListAdapter
    to create an instance of a new gear and modify the information for each instance. The attributes are declared as private
    to ensure encapsulation and the class contains getters and setters to retrieve information about each attribute.
 */
public class Gear {

    // declaration of private attributes of Gear class
    private String date;
    private String maker; // up to 20 character
    private String description; // up to 40 character
    private String price; // two decimal numbers
    private String comment; // up to 20 character

    // constructor 1
    public Gear( String date, String maker, String description, String price, String comment) {
        this.date = date;
        this.maker = maker;
        this.description = description;
        this.price = price;
        this.comment = comment;
    }

    // constructor 2 without comment attribute
    public Gear( String date, String maker, String description, String price) {
        this.date = date;
        this.maker = maker;
        this.description = description;
        this.price = price;
    }

    // getters and setters for each attribute
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // toString method for all attributes
    @Override
    public String toString() {
        return "Gear{" +
                ", date=" + date +
                ", maker='" + maker + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", comment='" + comment + '\'' +
                '}';
    }
}
