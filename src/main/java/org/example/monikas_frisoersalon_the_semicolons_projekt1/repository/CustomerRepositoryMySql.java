package org.example.monikas_frisoersalon_the_semicolons_projekt1.repository;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Customer;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryMySql {

    private final DbConfig db;

    public CustomerRepositoryMySql(DbConfig db){
        this.db = db;
    }

    public List<Customer> findAllCustomers(){
        String sql = "SELECT * FROM customer_info";
        List<Customer> customers = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while (rs.next()){
                customers.add(mapRow(rs));
            }

        } catch (Exception e) {
            throw new DataAccessException("Error in findAllCustomers()", e);
        }

        return customers;
    }

    public void createCustomer(String name, String phone) {
        String sql = "INSERT INTO customer_info (customer_name, customer_phone) VALUES" +
                "(?, ?)";

        try (Connection con = db.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new DataAccessException("Error in createCustomer()", e);
        }
    }

    public boolean isNewCustomer(String name, String phone) {
        String sql = "SELECT * FROM customer_info WHERE customer_name = ? AND REPLACE(customer_phone, ' ', '') = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);

            ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    System.out.println("Vi er nu i 'der er en næste'");
                    return false;
                }

            /*try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Vi er nu i 'der er en næste'");
                    return false;
                }
            }*/
        } catch (Exception e) {
            throw new DataAccessException("Error in createCustomer()", e);
        }
        System.out.println("Vi er hoppet over 'der er en næste'");
        return true;
    }

        private Customer mapRow (ResultSet rs) throws SQLException {
            return new Customer(rs.getInt("customer_id"), rs.getString("customer_name"), rs.getString("customer_phone"));
        }


}
