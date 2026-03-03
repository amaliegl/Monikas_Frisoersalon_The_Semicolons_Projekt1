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
import org.example.monikas_frisoersalon_the_semicolons_projekt1.model.*;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.repository.*;
import org.example.monikas_frisoersalon_the_semicolons_projekt1.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> timeChoiceBox;

    private final ObservableList<Haircut> haircuts = FXCollections.observableArrayList();
    private HaircutService haircutService;

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();
    private EmployeeService employeeService;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private CustomerService customerService;

    private TreatmentService treatmentService;

    private BookingService bookingService;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

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

        BookingRepositoryMySql bookingRepo = new BookingRepositoryMySql(db);
        bookingService = new BookingService(bookingRepo);

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


        //TODO - denne er til test lige nu
        refreshTimeSlots();
    }


    @FXML
    public void onGoBackButtonClick(ActionEvent event) throws IOException {
        switchToStartPage(event);
    }

    private void switchToStartPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/org/example/monikas_frisoersalon_the_semicolons_projekt1/startPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        priceLabel.setText(calculatePrice() + " kr.");
    }

    private void updateDurationLabel() {
        int sum = calculateDuration();

        if (sum > 59) {
            int hours = ((int) sum / 60);
            int minutes = ((int) sum % 60);
            if (minutes < 10) {
                durationLabel.setText(hours + ":0" + minutes + " (h:mm)");
            } else {
                durationLabel.setText(hours + ":" + minutes + " (h:mm)");
            }
        } else {
            durationLabel.setText(sum + " min");
        }
    }

    private double calculatePrice() {
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
        return sum;
    }

    private int calculateDuration() {
        int sum = 0;
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
        return sum;
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

    private void loadCustomers() {
        try {
            List<Customer> allCustomers = customerService.findAllCustomers();
            customers.setAll(allCustomers);
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHaircuts() {
        try {
            List<Haircut> allHaircuts = haircutService.getAll();
            haircuts.setAll(allHaircuts);
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onChooseTimeClick() {
        refreshTimeSlots();
    }

    private void refreshTimeSlots() {
        LocalDate date = datePicker.getValue();
        if (date == null) {
            System.out.println("Er inde i date == null");
            timeChoiceBox.setItems(FXCollections.observableArrayList());
            return;
        }

        //Nedenstående er bare for at prøve at fylde i boxen
        List<String> slots = new ArrayList<>();
        LocalTime t = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(17, 0);

        while (!t.isAfter(end.minusMinutes(30))) {
            String slot = t.format(TIME_FMT);
            slots.add(slot);
            t = t.plusMinutes(15);
        }
        timeChoiceBox.setItems(FXCollections.observableArrayList(slots));
        System.out.println("Skulle nu have sat tiden");


        /*List<LocalTime> slots = new ArrayList<>();
        LocalTime t = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(17, 0);

        while (!t.isAfter(end.minusMinutes(30))) {
            LocalTime slot = t;
            slots.add(slot);
            t = t.plusMinutes(15);
        }
        timeChoiceBox.setItems(FXCollections.observableArrayList(slots));*/
        
        /*
        Tjek ift. om tid er ledig
        - Er der min 1 frisør ledig i hele tiden
        
        
        
        Hvis der er valgt frisør på forhånd:
        - Er frisør ledig i hele perioden
         */
        
        /*
        Ift. at tjekke næste tid:
        if (duration % 15 == 0) {+ tid med duration}
        else {
        divisor = duration / 15
        + med (divisor + 1) * 15 */
    }

    @FXML
    private void onAddBookingClick() {
        //Create list of selected treatments
        List<Treatment> treatments = new ArrayList<>();
        if (washCheck.isSelected()) {
            treatments.add((Treatment) washCheck.getUserData());
        }
        if (dyeingCheck.isSelected()) {
            treatments.add((Treatment) dyeingCheck.getUserData());
        }
        if (maskCheck.isSelected()) {
            treatments.add((Treatment) maskCheck.getUserData());
        }
        if (bleachingCheck.isSelected()) {
            treatments.add((Treatment) bleachingCheck.getUserData());
        }

        //All fields that are not allowed to be null
        Haircut chosenHaircut = haircutChoice.getSelectionModel().getSelectedItem();
        Employee chosenEmployee = hairdresserChoice.getSelectionModel().getSelectedItem();
        LocalDate chosenDate = datePicker.getValue();
        String chosenTime = timeChoiceBox.getValue();
        Customer chosenCustomer = customerTable.getSelectionModel().getSelectedItem();


        if (chosenHaircut == null || chosenEmployee == null || chosenDate == null || chosenTime == null || chosenCustomer == null) {
            addBookingLabel.setText("Missing information, cannot create booking");
            return;
        }


        //Converting selected time (as String) to LocalTime
        LocalTime convertedTime = null;
        try {
            convertedTime = LocalTime.parse(chosenTime, TIME_FMT);
        } catch (DateTimeParseException e) {
            System.out.println("Problem med konvertering"); //TODO - change this!
        }


        try {
            bookingService.addBooking(chosenEmployee, chosenCustomer, chosenHaircut, treatments, calculateDuration(), calculatePrice(), paidCheck.isSelected(), Status.awaiting, chosenDate, convertedTime);
            addBookingLabel.setText("Booking created");
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO - til test
        try {
            List<Booking> bookings = bookingService.findAwaitingBookingsForEmployeeOnDate(chosenEmployee, chosenDate);
            if (bookings.size() > 0) {
                for (int i = 0; i < bookings.size(); i++) {
                    System.out.println(bookings.get(i));
                }
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    /*
    getAllByEmployeeAndDate
     */


            /*

            Date
            Time

            Paid
             */
}

