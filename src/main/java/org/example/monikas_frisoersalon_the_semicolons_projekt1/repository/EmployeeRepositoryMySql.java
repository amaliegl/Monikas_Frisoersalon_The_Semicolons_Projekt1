package org.example.monikas_frisoersalon_the_semicolons_projekt1.repository;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.UsernameOrPasswordIncorrectException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryMySql {

    private final DbConfig db;

    public EmployeeRepositoryMySql(DbConfig db) {
        this.db = db;
    }

    public List<Employee> findAll(){
        String sql = "SELECT * FROM employee_info";

        List<Employee> employees = new ArrayList<>();

        try (Connection con = db.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                employees.add(mapRow(rs));
            }
        } catch (Exception e) {
            throw new DataAccessException("Error in findAll() for employees", e);
        }
        return employees;
    }

    public Employee checkUsernameAndPasswordMatch(String username, String password) {
        String sql = "select employee_info.employee_id, employee_info.employee_name, employee_info.employee_email from employee_info\n" +
                "right join employee_login\n" +
                "on employee_info.employee_id = employee_login.employee_id\n" +
                "where employee_username = ? AND employee_password = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                   return new Employee(
                           rs.getInt("employee_id"),
                           rs.getString("employee_name"),
                           rs.getString("employee_email"));
                } else {
                    throw new UsernameOrPasswordIncorrectException("Username or password does not exist");
                    //TODO - lav exception-logning
                }
            }
        } catch (UsernameOrPasswordIncorrectException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error in checkUsernameAndPasswordMatch()", e);
            //TODO - exceptionlog
        }
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt("employee_id"), rs.getString("employee_name"), rs.getString("employee_email"));
    }
}