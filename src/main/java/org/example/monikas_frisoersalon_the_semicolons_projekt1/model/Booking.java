package org.example.monikas_frisoersalon_the_semicolons_projekt1.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Booking {
    private int id;
    private Employee employee;
    private Customer customer;
    private Haircut haircut;
    private List<Treatment> treatmentList;
    private int duration;
    private double totalPrice;
    private boolean paid;
    private Status status; //TODO - sørg for, at enum passer til enum i booking tabellen
    private LocalDate date;
    private LocalTime time;

}
