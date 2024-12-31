package com.example.healthflow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.Optional;

public class PatientRegistrationController {

    @FXML
    public AnchorPane ankrDashboard;
    @FXML
    public MenuButton mnubtnChooseGender;
    @FXML
    public MenuButton mnubtnChooseBloodGroup;
    @FXML
    public TextField txtfPatientID;
    @FXML
    public TextField txtfFirstName;
    @FXML
    public TextField txtfLastName;
    @FXML
    public TextField txtfAddressLine1;
    @FXML
    public TextField txtfCity;
    @FXML
    public TextField txtfPIN;
    @FXML
    public TextField txtfBedno1;
    @FXML
    public TextField txtfAge;
//    @FXML
//    public DatePicker txtfDOB;
    @FXML
    public TextField txtfPhoneNo;
    @FXML
    public TextField txtfEmail;
    @FXML
    public TextField txtfStreet;
    public String selectedGender = null;
    public String selectedBloodGroup = null;
    @FXML
    public AnchorPane ankrMain;
    @FXML
    public DatePicker dtpkrDate;

    // Initialize method
    @FXML
    public void initialize() {
        setNextPatientID();
        restrictFutureDates();

        // Disable future dates in DatePicker for DOB
        dtpkrDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        // Listen to DatePicker changes and calculate age
        dtpkrDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int age = calculateAge(newValue);
                txtfAge.setText(String.valueOf(age));
            }
        });

        // Validate PIN code - only integer input allowed
        txtfPIN.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtfPIN.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validate phone number - only integer input allowed and limited to 10 digits
        txtfPhoneNo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtfPhoneNo.setText(newValue.replaceAll("[^\\d]", ""));
            } else if (newValue.length() > 10) {
                txtfPhoneNo.setText(oldValue); // Limit to 10 digits
            }
        });

        // Add MenuItem action listeners for gender selection
        mnubtnChooseGender.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                selectedGender = menuItem.getText(); // Store selected gender
                mnubtnChooseGender.setText(selectedGender); // Display selection in MenuButton
            });
        });

        // Add MenuItem action listeners for blood group selection
        mnubtnChooseBloodGroup.getItems().forEach(menuItem -> {
            menuItem.setOnAction(event -> {
                selectedBloodGroup = menuItem.getText(); // Store selected blood group
                mnubtnChooseBloodGroup.setText(selectedBloodGroup); // Display selection in MenuButton
            });
        });
    }

    // Method to calculate age based on the selected DOB
    public int calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return (dob != null && currentDate != null) ? Period.between(dob, currentDate).getYears() : 0;
    }

    // Method to save patient details to the database
    @FXML
    public void handleSaveButtonClick() {
        String patientID = txtfPatientID.getText();
        String firstName = txtfFirstName.getText();
        String lastName = txtfLastName.getText();
        String address = txtfAddressLine1.getText();
        String city = txtfCity.getText();
        String pinCode = txtfPIN.getText();
        String bedNumber = txtfBedno1.getText();
        String age = txtfAge.getText();
        String dob = (dtpkrDate.getValue() != null) ? dtpkrDate.getValue().toString() : null;  // Null safety for DatePicker
        String phone = txtfPhoneNo.getText();
        String email = txtfEmail.getText().toLowerCase();  // Convert email to lowercase
        String street = txtfStreet.getText();

        // Check for required fields
        if (patientID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || dob == null || selectedGender == null || selectedBloodGroup == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("Please fill in all required fields.");
            alert.showAndWait();
            return;
        }

        // Validate phone number (10 digits)
        if (!phone.matches("\\d{10}")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("Phone number must be 10 digits.");
            alert.showAndWait();
            return;
        }

        // Validate email (must contain "@" and be in lowercase)
        if (!email.contains("@")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("Email must contain '@','.com' and be in lowercase.");
            alert.showAndWait();
            return;
        }

        // Validate PIN code (must be digits)
        if (!pinCode.matches("\\d+")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setContentText("PIN code must contain only digits.");
            alert.showAndWait();
            return;
        }

        // SQL query for patient table
        String queryPatient = "INSERT INTO patient (patient_id, first_name, last_name, Address, city, pincode, Blood_Group, Age, DOB, Gender, phone_no, email_id, street) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // SQL query for bed table
        String queryBed = "INSERT INTO bed (bed_no, patient_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             PreparedStatement pstmtPatient = conn.prepareStatement(queryPatient)) {

            // Set parameters for patient table
            pstmtPatient.setString(1, patientID);
            pstmtPatient.setString(2, firstName);
            pstmtPatient.setString(3, lastName);
            pstmtPatient.setString(4, address);
            pstmtPatient.setString(5, city);
            pstmtPatient.setString(6, pinCode);
            pstmtPatient.setString(7, selectedBloodGroup); // Use selected blood group
            pstmtPatient.setString(8, age);
            pstmtPatient.setString(9, dob);
            pstmtPatient.setString(10, selectedGender); // Use selected gender
            pstmtPatient.setString(11, phone);
            pstmtPatient.setString(12, email);
            pstmtPatient.setString(13, street);

            // Execute the patient table query
            pstmtPatient.executeUpdate();

            // Insert into bed table only if bed number is provided
            if (bedNumber != null && !bedNumber.trim().isEmpty()) {
                try (PreparedStatement pstmtBed = conn.prepareStatement(queryBed)) {
                    pstmtBed.setString(1, bedNumber);
                    pstmtBed.setString(2, patientID);
                    pstmtBed.executeUpdate();
                }
            }

            // Notify the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Patient details saved successfully!");
            alert.showAndWait();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to save patient details: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Method to clear all form fields
    public void handleClearButtonClick() {
        txtfPatientID.clear();
        txtfFirstName.clear();
        txtfLastName.clear();
        txtfAddressLine1.clear();
        txtfCity.clear();
        txtfPIN.clear();
        txtfBedno1.clear();
        txtfAge.clear();
        dtpkrDate.setValue(null); // Clear DatePicker
        txtfPhoneNo.clear();
        txtfEmail.clear();
        txtfStreet.clear();
        mnubtnChooseGender.setText("Choose option"); // Reset gender selection
        mnubtnChooseBloodGroup.setText("Choose option"); // Reset blood group selection
    }

    // Method to set the next available patient ID
    public void setNextPatientID() {
        String query = "SELECT MAX(patient_id) FROM patient";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                String lastPatientID = rs.getString(1);
                if (lastPatientID != null) {
                    String nextID = String.format("%03d", Integer.parseInt(lastPatientID) + 1);
                    txtfPatientID.setText(nextID);
                } else {
                    txtfPatientID.setText("001"); // Start at 001 if no patient exists
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        // Handler for Home button
    @FXML
    public void handleHomeButtonClick() throws IOException {
        switchScene("HomePage2.fxml");
    }

    // Handler for Appointment button
    @FXML
    public void handleAppointmentButtonClick() throws IOException {
        switchScene("Appointment.fxml");
    }

    // Handler for Billing button
    @FXML
    public void handleBillingButtonClick() throws IOException {
        switchScene("BillingandInvoice.fxml");
    }

    // Handler for Registration MenuItem selection
    @FXML
    public void handlePatientTabClick() throws IOException {
        switchScene("PatientRegistration.fxml");
    }

    @FXML
    public void handleDoctorTabClick() throws IOException {
        switchScene("DoctorRegistration.fxml");
    }

    @FXML
    public void handleStaffTabClick() throws IOException {
        switchScene("StaffRegistration.fxml");
    }

    @FXML
    public void handleClinicalManagementTabClick() throws IOException {
        switchScene("ClinicalManagement.fxml");
    }

    @FXML
    public void handleUserButtonClick() throws IOException {
        // Create a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        // Get the response from the user
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // If user confirms, switch to login page
            switchScene("LoginPage.fxml"); // Replace with your actual FXML file name
        }
    }

    @FXML
    public void handleProfileButtonClick() throws IOException {
        switchScene("Profile.fxml");
    }

    // Method to switch scenes
    public void switchScene(String fxmlFile) throws IOException {
        System.out.println("Switching to scene: " + fxmlFile);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ankrDashboard.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("HealthFlow");
        stage.show();
    }
    public void handleBackButtonClick() throws IOException {
        // Load the homepage scene from FXML (assuming "HomePage2.fxml" is the homepage)
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("HomePage2.fxml")));
        Parent homePageRoot = loader.load();

        // Get the controller for the homepage
        HomeController homePageController = loader.getController();

        // Call the refreshPage method to refresh the homepage data (this step is optional because the initialize method will do this)
        homePageController.refreshPage();

        // Get the current stage
        Stage stage = (Stage) ankrMain.getScene().getWindow();

        // Create a new scene with the homepage root node from the FXML file
        Scene homePageScene = new Scene(homePageRoot);

        // Set the homepage scene to the stage
        stage.setScene(homePageScene);
        stage.show();
    }

    // Prevent the user from selecting a future date
    public void restrictFutureDates() {
        dtpkrDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isAfter(LocalDate.now())) {
                    setDisable(true);
//                    ("-fx-bsetStyleackground-color: #ffc0cb;"); // Optional: style future dates differently
                }
            }
        });
    }

}

