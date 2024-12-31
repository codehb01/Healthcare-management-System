package com.example.healthflow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

public class HomeController {

    @FXML
    public AnchorPane ankrDashboard;

    @FXML
    public Button btnHome, btnAppointment, btnBilling, btnClinicalManagement, btnUser;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public MenuItem PatientTab, DoctorTab, StaffTab;

    @FXML
    public VBox vbxUser;

    @FXML
    public Label lblNoOfTotalPatients, lblTotalStaff, lblTotalnoDoctors, lblTotalnoStaff;

    @FXML
    public PieChart pieChart;

    @FXML
    public XYChart<String, Number> barGraph;

    // Scene switching methods
    @FXML
    public void handleHomeButtonClick() throws IOException {
        switchScene("HomePage2.fxml");
    }

    @FXML
    public void handleAppointmentButtonClick() throws IOException {
        switchScene("Appointment.fxml");
    }

    @FXML
    public void handleBillingButtonClick() throws IOException {
        switchScene("BillingandInvoice.fxml");
    }

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

    // Utility method to switch scenes
    public void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ankrDashboard.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("HealthFlow");
        stage.show();
    }


    // Load dashboard data
    public void loadDashboardData() {
        String DB_URL = "jdbc:mysql://localhost:3306/healthflow";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "12345678";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            // Query to get total patients
            int totalPatients = getTotalPatients(statement);
            lblNoOfTotalPatients.setText(String.valueOf(totalPatients));

            int totalDoctors = getTotalDoctors(statement);
            lblTotalnoDoctors.setText(String.valueOf(totalDoctors));

            int totalStaffs = getTotalStaffs(statement);
            lblTotalnoStaff.setText(String.valueOf(totalStaffs));

            // Update the PieChart with Age Groups
            updatePieChart(statement, totalPatients);

            // Update the BarChart with Blood Group data
            updateBarChart(statement);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateBarChart(Statement statement) {
        // Implement logic to update bar chart here
    }

    // Helper methods to get data from the database
    public int getTotalPatients(Statement statement) throws Exception {
        String queryTotalPatients = "SELECT COUNT(*) AS totalPatients FROM patient";
        try (ResultSet resultSet = statement.executeQuery(queryTotalPatients)) {
            if (resultSet.next()) {
                return resultSet.getInt("totalPatients");
            }
        }
        return 0;
    }

    public int getTotalDoctors(Statement statement) throws Exception {
        String queryTotalDoctors = "SELECT COUNT(*) AS totalDoctors FROM doctor";
        try (ResultSet resultSet = statement.executeQuery(queryTotalDoctors)) {
            if (resultSet.next()) {
                return resultSet.getInt("totalDoctors");
            }
        }
        return 0;
    }

    public int getTotalStaffs(Statement statement) throws Exception {
        String queryTotalStaffs = "SELECT COUNT(*) AS totalStaff FROM staff";
        try (ResultSet resultSet = statement.executeQuery(queryTotalStaffs)) {
            if (resultSet.next()) {
                return resultSet.getInt("totalStaff");
            }
        }
        return 0;
    }
    // Helper methods to update charts
    public void updatePieChart(Statement statement, int totalPatients) throws Exception {
        pieChart.getData().clear();
        String ageGroupQuery = "SELECT ageGroup, COUNT(*) AS count FROM patient GROUP BY ageGroup";
        try (ResultSet resultSet = statement.executeQuery(ageGroupQuery)) {
            while (resultSet.next()) {
                String ageGroup = resultSet.getString("ageGroup");
                int count = resultSet.getInt("count");

                double percentage = (count / (double) totalPatients) * 100;
                pieChart.getData().add(new PieChart.Data(ageGroup + " (" + String.format("%.1f", percentage) + "%)", count));
            }
        }

        // Update the BarChart with Blood Group data
        barGraph.getData().clear();
        String bloodGroupQuery = "SELECT Blood_Group, COUNT(*) AS count FROM patient GROUP BY Blood_Group";
        XYChart.Series<String, Number> seriesBloodGroup = new XYChart.Series<>();
        seriesBloodGroup.setName("Blood Group Distribution");

        try (ResultSet resultSet = statement.executeQuery(bloodGroupQuery)) {
            while (resultSet.next()) {
                String bloodGroup = resultSet.getString("Blood_Group");
                int count = resultSet.getInt("count");
                seriesBloodGroup.getData().add(new XYChart.Data<>(bloodGroup, count));
            }
        }

        barGraph.getData().add(seriesBloodGroup);

        // Update the PieChart with age groups
        pieChart.getData().clear();
        String ageQuery = "SELECT Age FROM patient";
        int ageGroup1 = 0, ageGroup2 = 0, ageGroup3 = 0, ageGroup4 = 0;

        try (ResultSet resultSet = statement.executeQuery(ageQuery)) {
            while (resultSet.next()) {
                int age = resultSet.getInt("age");
                if (age <= 12) {
                    ageGroup1++;
                } else if (age <= 19) {
                    ageGroup2++;
                } else if (age <= 60) {
                    ageGroup3++;
                } else {
                    ageGroup4++;
                }
            }
        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("0-12", (ageGroup1 / (double) totalPatients) * 100),
                new PieChart.Data("13-19", (ageGroup2 / (double) totalPatients) * 100),
                new PieChart.Data("20-60", (ageGroup3 / (double) totalPatients) * 100),
                new PieChart.Data("60+", (ageGroup4 / (double) totalPatients) * 100)
        );

        pieChart.setData(pieChartData);
    }



    @FXML
    public void refreshPage() {
        loadDashboardData();
    }

    @FXML
    public void initialize() {
        barGraph.getYAxis().setLabel("Number of Patients"); // Add this line
        refreshPage();
    }
}