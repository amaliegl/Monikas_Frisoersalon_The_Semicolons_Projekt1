package org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions;

public class UsernameOrPasswordIncorrectException extends RuntimeException {
    public UsernameOrPasswordIncorrectException(String message) {
        super(message);
    }
}
