package org.example.monikas_frisoersalon_the_semicolons_projekt1.model;

public class Haircut {
    private int id;
    private String name;
    private double price;
    private int duration;



    public Haircut(int id, String name, double price, int duration) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return  name + " (" + price + ")";
    }

}
