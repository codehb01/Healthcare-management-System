package com.example.healthflow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SetCredentialsController2 {
    @FXML
    public AnchorPane ankrMain;
    @FXML
    public TextField txtfUsername;
    @FXML
    public PasswordField txtfSetPassword;
    @FXML
    public PasswordField txtfConfirmPassword;
    @FXML
    public TextField txtfSecurityAnswer;
    @FXML
    public Label lblError;

    @FXML
    public Button btnProfile;
    public MenuItem DoctorTab, PatientTab, StaffTab;
    @FXML
    public AnchorPane ankrCredentials, ankrCredentials1, ankrTitle;
    @FXML
    public Button btnAppointment, btnBack, btnBilling, btnClearAll, btnClinicalManagement, btnHome, btnSave, btnUser;
    @FXML
    public ComboBox<?> cmbSecurityQuestion;
    @FXML
    public ImageView imgvUser;
    @FXML
    public Label lblCredentials, lblCredentials1, lblDoctorRegistration, lblPassword, lblUsername, lblUsername1, lblUsername11;
    @FXML
    public MenuButton mnuBtnRegistration;
    @FXML
    public VBox vbxUser;

    public DoctorRegistrationController.Doctor doctor;

    // Database credentials
    public final String DB_URL = "jdbc:mysql://localhost:3306/healthflow";
    public final String DB_USER = "root";
    public final String DB_PASS = "12345678";

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
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("HealthFlow");
        stage.show();
    }

    public void setDoctorDetails(DoctorRegistrationController.Doctor doctor) {
        this.doctor = doctor;
    }

    @FXML
    public void handleSaveCredentials() {
        String username = txtfUsername.getText();
        String password = txtfSetPassword.getText();
        String confirmPassword = txtfConfirmPassword.getText();
        String securityAnswer = txtfSecurityAnswer.getText();

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            lblError.setTextFill(Color.RED);
            lblError.setText("Passwords do not match!");
            return;
        }

        // Check if password is alphanumeric and contains both uppercase and lowercase letters
        if (!isValidPassword(password)) {
            lblError.setTextFill(Color.RED);
            lblError.setText("Password must be alphanumeric and contain both upper and lower case letters.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Check if username or password already exists
            if (isUsernameOrPasswordExists(conn, username, password)) {
                lblError.setTextFill(Color.RED);
                lblError.setText("Username or Password already exists!");
                return;
            }

            // Check if security answer matches with other users
            if (isSecurityAnswerExists(conn, securityAnswer)) {
                lblError.setTextFill(Color.RED);
                lblError.setText("Security answer matches with another user. Please choose a different question.");
                return;
            }

            conn.setAutoCommit(false); // Start transaction

            // Insert doctor details into doctor table
            String doctorQuery = "INSERT INTO doctor (DoctorId, first_name, last_name, Gender, DOB, phone_no, email, Regno, Speciality) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement doctorStmt = conn.prepareStatement(doctorQuery);
            doctorStmt.setInt(1, doctor.getDoctorID());
            doctorStmt.setString(2, doctor.getFirstName());
            doctorStmt.setString(3, doctor.getLastName());
            doctorStmt.setString(4, doctor.getGender());
            doctorStmt.setDate(5, java.sql.Date.valueOf(doctor.getDob()));
            doctorStmt.setString(6, doctor.getPhoneNo());
            doctorStmt.setString(7, doctor.getEmail());
            doctorStmt.setString(8, doctor.getRegNo());
            doctorStmt.setString(9, doctor.getSpeciality());
            doctorStmt.executeUpdate();

            // Insert credentials into user table with user_id as doctor_id
            String credentialsQuery = "INSERT INTO user (id, username, password, security_answer) VALUES (?, ?, ?, ?)";
            PreparedStatement credentialsStmt = conn.prepareStatement(credentialsQuery);
            credentialsStmt.setInt(1, doctor.getDoctorID());  // user_id is the same as doctor_id
            credentialsStmt.setString(2, username);
            credentialsStmt.setString(3, password);
            credentialsStmt.setString(4, securityAnswer);
            credentialsStmt.executeUpdate();

            conn.commit(); // Commit transaction

            lblError.setTextFill(Color.GREEN);
            lblError.setText("Doctor and credentials saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            lblError.setTextFill(Color.RED);
            lblError.setText("Error saving data.");
        }
    }

    // Check if username or password already exists in the database
    private boolean isUsernameOrPasswordExists(Connection conn, String username, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE username = ? OR password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Check if the security answer matches with any existing user
    private boolean isSecurityAnswerExists(Connection conn, String securityAnswer) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE security_answer = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, securityAnswer);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Validate password for alphanumeric and mixed case
    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false; // Ensure minimum length
        boolean hasUpperCase = false, hasLowerCase = false, hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            else if (Character.isLowerCase(c)) hasLowerCase = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpperCase && hasLowerCase && hasDigit;
    }

    @FXML
    public void handleBackButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DoctorRegistration.fxml"));
        Parent staffRegistrationRoot = loader.load();
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        Scene staffRegistrationScene = new Scene(staffRegistrationRoot);
        stage.setScene(staffRegistrationScene);
        stage.show();
    }

    @FXML
    public void clearAll() {
        // Clearing text fields
        txtfUsername.clear();
        txtfSetPassword.clear();
        txtfConfirmPassword.clear();
        txtfSecurityAnswer.clear();
        lblError.setText("");
    }
}

