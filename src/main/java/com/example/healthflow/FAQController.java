package com.example.healthflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class FAQController {
    @FXML
    public AnchorPane Question;
    @FXML
    public MenuItem DoctorTab;

    @FXML
    public MenuItem PatientTab;

    @FXML
    public MenuItem StaffTab;

    @FXML
    public AnchorPane ankrDashboard;

    @FXML
    public AnchorPane ankrDashboard2;

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public AnchorPane ankrTitle;

    @FXML
    public Button btnAppointment;

    @FXML
    public Button btnBack;

    @FXML
    public Button btnBilling;

    @FXML
    public Button btnClinicalManagement;

    @FXML
    public Button btnHome;

    @FXML
    public Button btnUser;

    @FXML
    public Button btnUser1;

    @FXML
    public ImageView imgvUser;

    @FXML
    public Label lblClinicalManagement;

    @FXML
    public Label lblDashboard;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public VBox vbxDashboard;

    @FXML
    public VBox vbxUser;

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
}