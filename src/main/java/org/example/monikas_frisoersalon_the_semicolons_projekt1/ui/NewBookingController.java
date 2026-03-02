package org.example.monikas_frisoersalon_the_semicolons_projekt1.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.DataAccessException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.exceptions.InvalidPhoneNumberException;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.infrastructure.DbConfig;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Customer;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Employee;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Haircut;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.Treatment;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.CustomerRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.EmployeeRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.HaircutRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.TreatmentRepositoryMySql;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.CustomerService;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.EmployeeService;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.HaircutService;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.TreatmentService;

import java.io.IOException;
import java.util.List;

public class NewBookingController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private ChoiceBox<Haircut> haircutChoice;
    @FXML private ChoiceBox<Employee> hairdresserChoice;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> phoneColumn;

    @FXML private CheckBox washCheck;
    @FXML private CheckBox dyeingCheck;
    @FXML private CheckBox maskCheck;
    @FXML private CheckBox bleachingCheck;
    @FXML private CheckBox paidCheck;

    @FXML private Label priceLabel;
    @FXML private Label durationLabel;
    @FXML private Label newCustomerLabel;
    @FXML private Label addBookingLabel;

    @FXML private TextField nameTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField phoneSearchTextField;

    private final ObservableList<Haircut> haircuts = FXCollections.observableArrayList();
    private HaircutService haircutService;

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();
    private EmployeeService employeeService;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private CustomerService customerService;

    private TreatmentService treatmentService;



    @FXML
    public void initialize() {
        DbConfig db = new DbConfig();
        HaircutRepositoryMySql haircutRepo = new HaircutRepositoryMySql(db);
        EmployeeRepositoryMySql employeeRepo = new EmployeeRepositoryMySql(db);
        haircutService = new HaircutService(haircutRepo);
        employeeService = new EmployeeService(employeeRepo);

        CustomerRepositoryMySql customerRepo = new CustomerRepositoryMySql(db);
        customerService = new CustomerService(customerRepo);

        TreatmentRepositoryMySql treatmentRepo = new TreatmentRepositoryMySql(db);
        treatmentService = new TreatmentService(treatmentRepo);

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNumber()));

        haircutChoice.setItems(haircuts);
        loadHaircuts();

        hairdresserChoice.setItems(employees);
        loadEmployees();


        customerTable.setItems(customers);
        loadCustomers();


        washCheck.setUserData(treatmentService.findByName("wash"));
        dyeingCheck.setUserData(treatmentService.findByName("dyeing"));
        maskCheck.setUserData(treatmentService.findByName("mask + wash"));
        bleachingCheck.setUserData(treatmentService.findByName("bleaching"));

    }


    @FXML
    public void onGoBackButtonClick(ActionEvent event) throws IOException {
        switchToStartPage(event);
    }

    private void switchToStartPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/example/monikas_frisoersalon_the_semicolons_projekt1/startPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void onAddCustomerClick() {
        String nameInput = nameTextField.getText().trim();
        String phoneInput = phoneTextField.getText().trim();

        try {
            if (customerService.createCustomer(nameInput, phoneInput)) {
                newCustomerLabel.setText("New customer added");
            } else {
                newCustomerLabel.setText("Customer already exist");
            }
            phoneSearchTextField.setText(phoneInput);
            loadCustomers();
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (InvalidPhoneNumberException e) {
            newCustomerLabel.setText("Invalid phone number");
        }
    }

    @FXML
    public void onSearchPhoneAction() {
        System.out.println("test");
    }


    @FXML
    public void onHaircutClick() { //onAction event inserted directly in fxml file as scenebuilder does not have this for comboboxes
        updatePriceAndDuration();

    }

    @FXML
    public void onWashClick() {
        updatePriceAndDuration();
    }

    @FXML
    public void onMaskClick() {
        updatePriceAndDuration();
    }

    @FXML
    public void onBleachingClick() {
        updatePriceAndDuration();
    }

    @FXML
    public void onDyeingClick() {
        updatePriceAndDuration();
    }

    private void updatePriceAndDuration() {
        updatePriceLabel();
        updateDurationLabel();
    }


    private void updatePriceLabel() {
        double sum = 0;
        if (haircutChoice.getValue() != null) {
            sum += haircutChoice.getValue().getPrice();
        }
        if (washCheck.isSelected()) {
            sum += ((Treatment) washCheck.getUserData()).getPrice();
        }
        if (dyeingCheck.isSelected()) {
            sum += ((Treatment) dyeingCheck.getUserData()).getPrice();
        }
        if (maskCheck.isSelected()) {
            sum += ((Treatment) maskCheck.getUserData()).getPrice();
        }
        if (bleachingCheck.isSelected()) {
            sum += ((Treatment) bleachingCheck.getUserData()).getPrice();
        }
        priceLabel.setText(sum + " kr.");
    }

    private void updateDurationLabel() {
        double sum = 0;
        if (haircutChoice.getValue() != null) {
            sum += haircutChoice.getValue().getDuration();
        }
        if (washCheck.isSelected()) {
            sum += ((Treatment) washCheck.getUserData()).getDuration();
        }
        if (dyeingCheck.isSelected()) {
            sum += ((Treatment) dyeingCheck.getUserData()).getDuration();
        }
        if (maskCheck.isSelected()) {
            sum += ((Treatment) maskCheck.getUserData()).getDuration();
        }
        if (bleachingCheck.isSelected()) {
            sum += ((Treatment) bleachingCheck.getUserData()).getDuration();
        }

        if (sum > 59) {
            int hours = ((int)sum / 60);
            int minutes = ((int)sum % 60);
            if (minutes < 10) {
                durationLabel.setText(hours + ":0" + minutes + " (h:mm)");
            } else {
                durationLabel.setText(hours + ":" + minutes + " (h:mm)");
            }
        } else {
            durationLabel.setText(sum + " min");
        }
    }

    private void loadEmployees() {
        try {
            List<Employee> allEmployees = employeeService.getAll();
            employees.setAll(allEmployees);
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomers(){
        try {
            List<Customer> allCustomers = customerService.findAllCustomers();
            customers.setAll(allCustomers);
        } catch (DataAccessException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHaircuts(){
        try {
            List<Haircut> allHaircuts = haircutService.getAll();
            haircuts.setAll(allHaircuts);
        } catch (DataAccessException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
