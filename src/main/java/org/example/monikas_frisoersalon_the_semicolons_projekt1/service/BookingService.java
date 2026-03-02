package org.example.monikas_frisoersalon_the_semicolons_projekt1.service;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.*;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.BookingRepositoryMySql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingService {
    private final BookingRepositoryMySql repo;

    public BookingService(BookingRepositoryMySql repo){
        this.repo = repo;
    }

    public void addBooking(Employee employee, Customer customer, Haircut haircut, List<Treatment> treatments, int duration, double price,
                           boolean paid, Status status, LocalDate date, LocalTime time) throws DataAccessException {
        int bookingId = repo.createBooking(employee, customer, haircut, duration, price, paid, status, date, time);
        if (!(bookingId == -1)) {
            logTreatmentsInBooking(bookingId, treatments);
        }
        //TODO - udkommenteret, så vi ikke tilføjer en masse nye "forkerte" bookinger under test


    }

    public void logTreatmentsInBooking(int bookingId, List<Treatment> treatments){
        if (treatments == null) {
            return;
        }
        for (int i = 0; i < treatments.size(); i++) {
            repo.addTreatmentsToBooking(bookingId, treatments.get(i).getId());
        }
    }
}
