package org.example.monikas_frisoersalon_the_semicolons_projekt1.ui;

//import org.example.monikas_frisoersalon_the_semicolons_projekt1.startPage.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.UsernameOrPasswordIncorrectException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.EmployeeRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.EmployeeService;

import java.io.IOException;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;

    @FXML private Label loginLabel;

    private EmployeeService employeeService;

    @FXML
    public void initialize() {
        DbConfig db = new DbConfig();
        EmployeeRepositoryMySql employeeRepo = new EmployeeRepositoryMySql(db);
        employeeService = new EmployeeService(employeeRepo);
    }

    @FXML
    protected void onLoginButtonClick(ActionEvent event) throws IOException {
        String inputtetUsername = usernameInput.getText().trim();
        String inputtedPassword = passwordInput.getText().trim();
        try {
            if (employeeService.checkLogin(inputtetUsername, inputtedPassword)) { //checking login
                switchToStartPage(event);
            }
        } catch (DataAccessException e){
            showAlert("Database error", e.getMessage());
            e.printStackTrace();
            //TODO - logging error
        } catch (UsernameOrPasswordIncorrectException e) {
            loginLabel.setText("Username or password incorrect");
            passwordInput.clear();
            //TODO - logning af error
        }
    }

    public void switchToStartPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/example/monikas_frisoersalon_the_semicolons_projekt1/startPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}