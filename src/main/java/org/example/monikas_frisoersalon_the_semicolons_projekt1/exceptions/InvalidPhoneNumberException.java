package org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
