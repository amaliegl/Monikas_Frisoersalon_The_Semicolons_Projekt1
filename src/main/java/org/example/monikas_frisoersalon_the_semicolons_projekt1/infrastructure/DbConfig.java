package org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure;

import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConfig {

    private final String url;
    private final String user;
    private final String password;

    public DbConfig() {
        Properties props = new Properties();

        try (InputStream is = getClass().getResourceAsStream("/db.properties")) {
            if (is == null) {
                throw new DataAccessException("Could not find db.properties in resources");
            }
            props.load(is);
        } catch (Exception e) {
            throw new DataAccessException("Could not read db.properties", e);
        }

        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");
    }

    public Connection getConnection() {
        try {
            // DriverManager håndterer MySQL-driveren via mysql-connector dependency
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new DataAccessException("Could not connect to the database", e);
        }
    }
}