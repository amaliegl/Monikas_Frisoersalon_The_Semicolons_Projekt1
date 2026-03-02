package org.example.monikas_frisoersalon_the_semicolons_projekt1.repository;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalTime.now;

public class BookingRepositoryMySql {
    private final DbConfig db;

    public BookingRepositoryMySql(DbConfig db) {
        this.db = db;
    }

    //return id (int) of created booking
    public int createBooking(Employee employee, Customer customer, Haircut haircut, int duration, double price,
                             boolean paid, Status status, LocalDate date, LocalTime time)  {
        String sql = "insert into bookings (booking_employee, booking_customer, booking_haircut, booking_duration, booking_price, booking_paid, booking_status, booking_date, booking_time, booking_creation_date)\n" +
                "values (?,?,?,?,?,?,?,?,?,?);";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, employee.getId());
            ps.setInt(2, customer.getId());
            ps.setInt(3, haircut.getId());
            ps.setInt(4, duration);
            ps.setDouble(5, price);

            if (paid) {
                ps.setInt(6, 1);
            } else {
                ps.setInt(6, 0);
            }

            ps.setString(7, status.name());
            ps.setDate(8, Date.valueOf(date));
            ps.setTime(9, Time.valueOf(time));

            LocalDateTime localDateTime = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            ps.setTimestamp(10, timestamp);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            return -1; //
        } catch (Exception e) {
            throw new DataAccessException("Error in createBooking()", e);
        }
    }

    public void addTreatmentsToBooking(int bookingId, int treatmentId) {
        String sql = "INSERT INTO booking_treatment (booking_id, treatment_id) VALUES (?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ps.setInt(2, treatmentId);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Error in addTreatmentsToBooking()", e);
        }

    }

}

