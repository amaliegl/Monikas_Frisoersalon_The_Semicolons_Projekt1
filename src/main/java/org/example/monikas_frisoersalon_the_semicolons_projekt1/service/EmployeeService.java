package org.example.monikas_frisoersalon_the_semicolons_projekt1.service;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.UsernameOrPasswordIncorrectException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Employee;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.EmployeeRepositoryMySql;

import java.util.List;

public class EmployeeService {
    private final EmployeeRepositoryMySql repo;
    private static Employee currentUser;

    public EmployeeService(EmployeeRepositoryMySql repo) {
        this.repo = repo;
    }

    public List<Employee> getAll() throws DataAccessException {
        return repo.findAll();
    }

    public boolean checkLogin(String username, String password) throws DataAccessException, UsernameOrPasswordIncorrectException {
        currentUser = repo.checkUsernameAndPasswordMatch(username, password);

        return true;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }
}
