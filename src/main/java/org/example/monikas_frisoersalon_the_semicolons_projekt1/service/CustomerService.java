package org.example.monikas_frisoersalon_the_semicolons_projekt1.service;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.InvalidPhoneNumberException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Customer;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.CustomerRepositoryMySql;

import java.util.List;

public class CustomerService {

    private final CustomerRepositoryMySql repo;

    public CustomerService(CustomerRepositoryMySql repo) throws DataAccessException {
        this.repo = repo;
    }

    public List<Customer> findAllCustomers() throws DataAccessException {
        return repo.findAllCustomers();
    }

    public boolean createCustomer(String name, String phone) throws DataAccessException, InvalidPhoneNumberException {
        if (verifyPhoneNumber(phone) && isNewCustomer(name, phone)) {
            //repo.createCustomer(name, phone);
            System.out.println("Databasen VILLE have oprettet en ny KUNDE nu!!");
            return true;
        } else {
            System.out.println("true test - result: false");
            return false;
        }
    }

    private boolean isNewCustomer(String name, String phone) throws DataAccessException {
        String noSpacesPhone = phone.replaceAll("\\s+", "");
        return repo.isNewCustomer(name, noSpacesPhone);
    }

    private boolean verifyPhoneNumber(String phone) {
        String noSpacesPhone = phone.replaceAll("\\s+", "");
        try {
            Integer.parseInt(noSpacesPhone);
            return true;
        } catch (NumberFormatException e) {
            throw new InvalidPhoneNumberException("Phone " + phone + " is not a valid phone number");
        }
    }
}
