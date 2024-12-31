package com.example.healthflow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class SetCredentialsController {

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public TextField txtfUsername;

    @FXML
    public PasswordField txtfSetPassword;

    @FXML
    public PasswordField txtfConfirmPassword;

    @FXML
    public ComboBox<String> cmbSecurityQuestion; // Combobox for selecting security question

    @FXML
    public TextField txtfSecurityAnswer;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnClearAll;

    // Temporary staff details passed from StaffRegistrationController
    public String firstName, lastName, dob, staffID, phoneNo, email, qualification,gender, department;

    public void setStaffDetails(String firstName, String lastName, String dob, String staffID, String phoneNo,
                                String email, String qualification,String gender ,String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.staffID = staffID;
        this.phoneNo = phoneNo;
        this.email = email;
        this.qualification = qualification;
        this.department = department;
        this.gender = gender;
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
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("HealthFlow");
        stage.show();
    }





    @FXML
    public void initialize() {
        // Add some default security questions to the ComboBox
        cmbSecurityQuestion.getItems().addAll("What was your first pet?", "What is your mother's maiden name?", "What is your favorite book?");
    }

    @FXML
    public void handleSaveButtonClick() {
        String username = txtfUsername.getText();
        String setPassword = txtfSetPassword.getText();
        String confirmPassword = txtfConfirmPassword.getText();
        String securityQuestion = cmbSecurityQuestion.getValue();
        String securityAnswer = txtfSecurityAnswer.getText();

        // Validate password and credentials
        if (areFieldsValid(username, setPassword, confirmPassword, securityQuestion, securityAnswer)) {
            if (setPassword.equals(confirmPassword) && isPasswordValid(setPassword)) {
                try {
                    saveToDatabase(username, setPassword, securityQuestion, securityAnswer);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Credentials saved successfully!");
//                    closeWindow();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save credentials: " + e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match or are invalid!");
            }
        }
    }

    public boolean isPasswordValid(String password) {
        // Ensure password meets complexity requirements
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*");
    }

    @FXML
    public boolean areFieldsValid(String username, String setPassword, String confirmPassword,
                                  String securityQuestion, String securityAnswer) {
        if (username.isEmpty() || setPassword.isEmpty() || confirmPassword.isEmpty() ||
                securityQuestion == null || securityAnswer.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    @FXML
    public void saveToDatabase(String username, String password, String securityQuestion, String securityAnswer) throws SQLException {
        // Database connection setup
        String url = "jdbc:mysql://localhost:3306/healthflow";
        String user = "root"; // Replace with your MySQL username
        String dbPassword = "12345678"; // Replace with your MySQL password

        Connection conn = DriverManager.getConnection(url, user, dbPassword);

        // Save staff details in 'staff' table
        String staffInsertQuery = "INSERT INTO staff (staff_id, first_name, last_name, dob, phone_no, email, qualification, gender, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement staffStmt = conn.prepareStatement(staffInsertQuery);
        staffStmt.setString(1, staffID);  // assuming staffID is passed
        staffStmt.setString(2, firstName);
        staffStmt.setString(3, lastName);
        staffStmt.setString(4, dob);
        staffStmt.setString(5, phoneNo);
        staffStmt.setString(6, email);
        staffStmt.setString(7, qualification);
        staffStmt.setString(8, gender);
        staffStmt.setString(9, department);
        staffStmt.executeUpdate();

        // Save credentials in 'user' table, including staff_id
        String userInsertQuery = "INSERT INTO user (id, username, password, security_answer) VALUES (?, ?, ?, ?)";
        PreparedStatement userStmt = conn.prepareStatement(userInsertQuery);
        userStmt.setString(1, staffID);  // staffID will be stored in the 'id' column of the 'user' table
        userStmt.setString(2, username);
        userStmt.setString(3, password);
        userStmt.setString(4, securityAnswer);
        userStmt.executeUpdate();

        conn.close();
    }


    @FXML
    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

//    @FXML
//    public void closeWindow() {
//        Stage stage = (Stage) btnSave.getScene().getWindow();
//        stage.close();
//    }

    @FXML
    public void clearAll() {
        // Clearing text fields
        txtfUsername.clear();
        txtfSetPassword.clear();
        txtfConfirmPassword.clear();
        txtfSecurityAnswer.clear();

        // Resetting ComboBox to default state
        cmbSecurityQuestion.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleBackButtonClick() throws IOException {
        // Load the staff registration scene from FXML (assuming "StaffRegistration.fxml" is the staff registration page)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("StaffRegistration.fxml"));
        Parent staffRegistrationRoot = loader.load();

        // Get the current stage
        Stage stage = (Stage) ankrMain.getScene().getWindow();

        // Create a new scene with the staff registration root node from the FXML file
        Scene staffRegistrationScene = new Scene(staffRegistrationRoot);

        // Set the staff registration scene to the stage
        stage.setScene(staffRegistrationScene);
        stage.show();
    }

}
