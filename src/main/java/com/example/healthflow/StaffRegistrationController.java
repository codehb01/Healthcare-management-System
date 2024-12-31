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
import java.util.Optional;

import static com.example.healthflow.AppointmentController.*;

public class StaffRegistrationController {

    @FXML
    public AnchorPane ankrContact;

    @FXML
    public DatePicker dtpkrDate;

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnSetCredentials; // New button to go to credentials page

    @FXML
    public TextField txtfFirstName;

    @FXML
    public TextField txtfLastName;

    @FXML
    public TextField txtfPhoneNo;

    @FXML
    public TextField txtfEmail;

    @FXML
    public TextField txtfStaffID;

    @FXML
    public TextField txtfDepartment;

    @FXML
    public MenuButton mnubtnChooseQualification;

    @FXML
    public MenuButton mnubtnChooseGender;

    // Store staff details temporarily before saving credentials
    public String tempFirstName, tempLastName, tempDob, tempStaffID, tempPhoneNo, tempEmail, tempQualification, tempGender, tempDepartment;

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

    @FXML
    public void handleBackButtonClick() throws IOException {
        // Load the homepage scene from FXML (assuming "HomePage2.fxml" is the homepage)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage2.fxml"));
        Parent homePageRoot = loader.load();

        // Get the controller for the homepage
        HomeController homePageController = loader.getController();
        homePageController.refreshPage(); // Refresh the homepage data

        // Get the current stage
        Stage stage = (Stage) ankrMain.getScene().getWindow();

        // Create a new scene with the homepage root node from the FXML file
        Scene homePageScene = new Scene(homePageRoot);
        // Set the homepage scene to the stage
        stage.setScene(homePageScene);
        stage.show();
    }

    // Update switchScene method to use loader once
    @FXML
    public void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();  // Load once
        Scene scene = new Scene(root);
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        for (MenuItem item : mnubtnChooseQualification.getItems()) {
            item.setOnAction(event -> mnubtnChooseQualification.setText(item.getText()));
        }
        for (MenuItem item : mnubtnChooseGender.getItems()) {
            item.setOnAction(event -> mnubtnChooseGender.setText(item.getText()));
        }
        // Call the method to display next staff ID
        displayNextStaffID();
        restrictFutureDates();
    }
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

    @FXML
    public void handleSaveButtonClick() {
        // Save staff details (without credentials)
        if (areFieldsValid()) {
            tempFirstName = txtfFirstName.getText();
            tempLastName = txtfLastName.getText();
            tempDob = (dtpkrDate.getValue() != null) ? dtpkrDate.getValue().toString() : "";
            tempStaffID = txtfStaffID.getText();
            tempPhoneNo = txtfPhoneNo.getText();
            tempEmail = txtfEmail.getText();
            tempQualification = mnubtnChooseQualification.getText();
            tempGender = mnubtnChooseGender.getText();
            tempDepartment = txtfDepartment.getText();
            // Proceed with further processing...
        }
    }

    // Redirect to SetCredentials page
    @FXML
    public void handleSetCredentialsClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SetCredentials.fxml"));
        Parent root = loader.load();

        // Pass temporary staff details to the SetCredentialsController
        SetCredentialsController credentialsController = loader.getController();
        credentialsController.setStaffDetails(tempFirstName, tempLastName, tempDob, tempStaffID, tempPhoneNo, tempEmail, tempQualification, tempGender, tempDepartment);

        Stage stage = (Stage) btnSetCredentials.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Method to check if staff details are valid
    @FXML
    public boolean areFieldsValid() {
        // Check if any field is empty
        if (txtfFirstName.getText().isEmpty() || txtfLastName.getText().isEmpty() || (dtpkrDate.getValue() == null)
                || txtfStaffID.getText().isEmpty() || txtfPhoneNo.getText().isEmpty() || txtfEmail.getText().isEmpty()
                || mnubtnChooseQualification.getText().equals("Choose option") || mnubtnChooseGender.getText().equals("Choose option")
                || txtfDepartment.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }

        // Validate phone number
        String phoneNumber = txtfPhoneNo.getText();
        if (!phoneNumber.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone number must be exactly 10 digits.");
            return false;
        }

        // Validate email
        String email = txtfEmail.getText();
        if (!email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Email must contain '@' symbol.");
            return false;
        }

        return true; // All validations passed
    }

    // Show alert dialog
    @FXML
    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void clearAll() {
        // Clearing all text fields
        txtfFirstName.clear();
        txtfLastName.clear();
        txtfPhoneNo.clear();
        txtfEmail.clear();
        txtfStaffID.clear();
        txtfDepartment.clear();

        // Resetting MenuButtons to default state
        mnubtnChooseQualification.setText("Choose option");
        mnubtnChooseGender.setText("Choose option");

        // Resetting DatePicker to null
        dtpkrDate.setValue(null);
    }

    public void displayNextStaffID() {
        String query = "SELECT MAX(staff_id) AS last_staff_id FROM staff";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                int lastStaffId = resultSet.getInt("last_staff_id");
                int nextStaffId = lastStaffId + 1;
                txtfStaffID.setText(String.valueOf(nextStaffId));
            } else {
                // If there are no staff in the database, start from 1
                txtfStaffID.setText("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching the last staff ID: " + e.getMessage());
        }
    }
}
