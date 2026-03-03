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
    private Status status;
    private LocalDate date;
    private LocalTime time;

    public Booking(int id, Employee employee, Customer customer, Haircut haircut, int duration, double totalPrice, boolean paid, Status status, LocalDate date, LocalTime time){
    this.id = id;
    this.employee = employee;
    this.customer = customer;
    this.haircut = haircut;
    this.duration = duration;
    this.totalPrice = totalPrice;
    this.paid = paid;
    this.status = status;
    this.date = date;
    this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", employee=" + employee +
                ", customer=" + customer +
                ", haircut=" + haircut +
                ", treatmentList=" + treatmentList +
                ", duration=" + duration +
                ", totalPrice=" + totalPrice +
                ", paid=" + paid +
                ", status=" + status +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
