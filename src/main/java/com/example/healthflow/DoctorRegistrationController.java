package com.example.healthflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class DoctorRegistrationController {

    @FXML
    public AnchorPane ankrMain;
    @FXML
    public TextField txtfFirstName;
    @FXML
    public TextField txtfLastName;
    @FXML
    public TextField txtfDoctorID;
    @FXML
    public TextField txtfPhoneNo;
    @FXML
    public TextField txtfEmail;
    @FXML
    public TextField txtfRegNo;
    @FXML
    public ComboBox<String> mnubtnChooseSpeciality;
    @FXML
    public MenuButton mnubtnChooseGender;
    @FXML
    public DatePicker dtpkrDate;
    @FXML
    public Label lblPasswordError;

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
        stage.show();
    }

    @FXML
    public void initialize() {
        setAutoIncrementDoctorId();
        restrictFutureDates();
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


    public void setAutoIncrementDoctorId() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT COALESCE(MAX(DoctorId), 0) + 1 AS NextDoctorId FROM doctor";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int nextDoctorId = rs.getInt("NextDoctorId");
                txtfDoctorID.setText(String.valueOf(nextDoctorId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lblPasswordError.setTextFill(Color.RED);
            lblPasswordError.setText("Error fetching Doctor ID.");
        }
    }

    // Handler for Set Credentials button
    @FXML
    public void handleSetCredentialsButtonClick() throws IOException {
        // Validate inputs
        if (!isInputValid()) {
            return; // Don't proceed if validation fails
        }

        // Create a Doctor object and populate it with form data
        Doctor doctor = new Doctor();
        doctor.setDoctorID(Integer.parseInt(txtfDoctorID.getText()));
        doctor.setFirstName(txtfFirstName.getText());
        doctor.setLastName(txtfLastName.getText());
        doctor.setGender(mnubtnChooseGender.getText());
        doctor.setDob(dtpkrDate.getValue());
        doctor.setPhoneNo(txtfPhoneNo.getText());
        doctor.setEmail(txtfEmail.getText());
        doctor.setRegNo(txtfRegNo.getText());
        doctor.setSpeciality(mnubtnChooseSpeciality.getValue());

        // Load SetCredentials.fxml and pass the doctor object
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SetCredentials2.fxml"));
        Parent root = loader.load();

        // Pass the doctor object to the SetCredentialsController
        SetCredentialsController2 credentialsController = loader.getController();
        credentialsController.setDoctorDetails(doctor);

        Stage stage = (Stage) ankrMain.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isInputValid() {
        // Check phone number length
        String phoneNo = txtfPhoneNo.getText();
        if (phoneNo.length() != 10 || !phoneNo.matches("\\d+")) {
            showAlert("Phone Number Error", "Phone number must be exactly 10 digits.");
            return false;
        }

        // Check email format
        String email = txtfEmail.getText();
        if (!email.contains("@")) {
            showAlert("Email Error", "Email must contain '@'.");
            return false;
        }

        // Check registration number format
        String regNo = txtfRegNo.getText();
        if (!regNo.matches("\\d+")) {
            showAlert("Registration Number Error", "Registration number must be an integer.");
            return false;
        }

        return true; // All checks passed
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class Doctor {
        public int doctorID;
        public String firstName;
        public String lastName;
        public String gender;
        public LocalDate dob;
        public String phoneNo;
        public String email;
        public String regNo;
        public String speciality;

        // Getters and Setters
        public int getDoctorID() {
            return doctorID;
        }

        public void setDoctorID(int doctorID) {
            this.doctorID = doctorID;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public LocalDate getDob() {
            return dob;
        }

        public void setDob(LocalDate dob) {
            this.dob = dob;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRegNo() {
            return regNo;
        }

        public void setRegNo(String regNo) {
            this.regNo = regNo;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }
    }

    @FXML
    public void handleGenderSelect(ActionEvent event) {
        // Get the source of the event, which is the MenuItem that was clicked
        MenuItem selectedMenuItem = (MenuItem) event.getSource();
        String selectedGender = selectedMenuItem.getText();

        // Update the text of the MenuButton to show the selected gender
        mnubtnChooseGender.setText(selectedGender);

        // Optional: Do something with the selected gender (e.g., store it in a variable, print it)
        System.out.println("Selected Gender: " + selectedGender);
    }

    @FXML
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

        // Create a new scene with the homepage root node from the FXML
        Scene homeScene = new Scene(homePageRoot);

        // Set the scene to the current stage and show it
        stage.setScene(homeScene);
        stage.show();
    }
    @FXML
    public void clearAll() {
        // Clear text fields
        txtfFirstName.clear();
        txtfLastName.clear();
        txtfDoctorID.clear();
        txtfPhoneNo.clear();
        txtfEmail.clear();
        txtfRegNo.clear();

        // Reset the ComboBox (Speciality)
        mnubtnChooseSpeciality.setValue(null);

        // Reset the MenuButton (Gender)
        mnubtnChooseGender.setText("Select Gender"); // Set it to the default label or placeholder

        // Reset DatePicker (Date of Birth)
        dtpkrDate.setValue(null);

        // Optionally, clear any error labels
        lblPasswordError.setText("");
    }
}





