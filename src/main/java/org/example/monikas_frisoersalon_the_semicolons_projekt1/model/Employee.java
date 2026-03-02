package org.example.monikas_frisoersalon_the_semicolons_projekt1.model;

public class Employee {
    private int id;
    private String name;
    private String email;

    public Employee(int id, String name, String email ){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
