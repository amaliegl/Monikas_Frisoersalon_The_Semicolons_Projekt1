package org.example.monikas_frisoersalon_the_semicolons_projekt1.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.EmployeeRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.EmployeeService;

import java.io.IOException;
import java.net.URL;

public class StartPageController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label currentEmployee;

    private EmployeeService employeeService;

    @FXML
    public void initialize() {
        DbConfig db = new DbConfig();
        EmployeeRepositoryMySql employeeRepo = new EmployeeRepositoryMySql(db);
        employeeService = new EmployeeService(employeeRepo);
        currentEmployee.setText(employeeService.getCurrentUser().getName());
    }

    @FXML
    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        switchToLoginPage(event);
    }

    @FXML
    public void onNewBookingButtonClick(ActionEvent event) throws IOException {
        switchToNewBookingPage(event);
    }

    private void switchToLoginPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/example/monikas_frisoersalon_the_semicolons_projekt1/loginPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void switchToNewBookingPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/example/monikas_frisoersalon_the_semicolons_projekt1/newBookingPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
