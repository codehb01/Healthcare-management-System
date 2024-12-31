package com.example.healthflow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

import javafx.event.ActionEvent;

public class ClinicalManagementController {

    @FXML
    public TableColumn<Patient, String> ClnAge;

    @FXML
    public TableColumn<Patient, String> ClnBG;

    @FXML
    public TableColumn<Patient, String> ClnFirstName;

    @FXML
    public TableColumn<Patient, String> ClnGender;

    @FXML
    public TableColumn<Patient, String> ClnLastName;

    @FXML
    public TableColumn<Patient, String> ClnPatientId;

    @FXML
    public TableColumn<Patient, String> ClnPhNo;

    @FXML
    public AnchorPane ankrDashboard;

    @FXML
    public AnchorPane ankrMain;

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
    public ImageView imgvSearch;

    @FXML
    public ImageView imgvUser;

    @FXML
    public Label lblClinicalManagement;

    @FXML
    public Label lblDashboard;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public TableView<Patient> tblvTable;

    @FXML
    public TextField txtfSearch;

    @FXML
    public VBox vbxDashboard;

    // ObservableList to hold patient data
    public ObservableList<Patient> patients = FXCollections.observableArrayList();

    @FXML
    public void handleAppointmentButtonClick() throws IOException {
        switchScene("Appointment.fxml");
    }

    // Handler for Home button
    @FXML
    public void handleHomeButtonClick() throws IOException {
        switchScene("HomePage2.fxml");
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

    public void switchScene(String fxmlFile) throws IOException {
        System.out.println("Switching to scene: " + fxmlFile);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ankrDashboard.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleBackButtonClick() throws IOException {
        // Load the homepage scene from FXML (assuming "HomePage2.fxml" is the homepage)
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("HomePage2.fxml")));
        Parent homePageRoot = loader.load();

        // Get the controller for the homepage
        HomeController homePageController = loader.getController();
        homePageController.refreshPage();

        Stage stage = (Stage) ankrMain.getScene().getWindow();
        Scene homePageScene = new Scene(homePageRoot);
        stage.setScene(homePageScene);
        stage.show();
    }

    public static class Patient {
        public String patientID;
        public String firstName;
        public String lastName;
        public String bloodGroup;
        public String phoneNo;
        public String age;
        public String gender;

        public Patient(String patientID, String firstName, String lastName, String bloodGroup, String phoneNo, String age, String gender) {
            this.patientID = patientID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.bloodGroup = bloodGroup;
            this.phoneNo = phoneNo;
            this.age = age;
            this.gender = gender;
        }

        public String getPatientID() {
            return patientID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getBloodGroup() {
            return bloodGroup;
        }

        public String getPhoneNo() {
            return phoneNo;
        }

        public String getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }
    }

    @FXML
    public void initialize() {
        // Set up the columns with the model's properties
        ClnPatientId.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        ClnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        ClnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ClnBG.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        ClnPhNo.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        ClnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        ClnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));

        // Make columns editable
        makeColumnsEditable();

        // Load patient data from the database
        loadPatientData();
    }

    private void makeColumnsEditable() {
        ClnFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnBG.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnPhNo.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnAge.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnGender.setCellFactory(TextFieldTableCell.forTableColumn());

        ClnFirstName.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.firstName = event.getNewValue();
            updatePatientInDatabase(patient);
        });

        ClnLastName.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.lastName = event.getNewValue();
            updatePatientInDatabase(patient);
        });

        ClnBG.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.bloodGroup = event.getNewValue();
            updatePatientInDatabase(patient);
        });

        ClnPhNo.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.phoneNo = event.getNewValue();
            updatePatientInDatabase(patient);
        });

        ClnAge.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.age = event.getNewValue();
            updatePatientInDatabase(patient);
        });

        ClnGender.setOnEditCommit(event -> {
            Patient patient = event.getRowValue();
            patient.gender = event.getNewValue();
            updatePatientInDatabase(patient);
        });
    }

    private void updatePatientInDatabase(Patient patient) {
        String query = "UPDATE patient SET first_name = ?, last_name = ?, Blood_Group = ?, phone_no = ?, Age = ?, Gender = ? WHERE patient_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patient.getFirstName());
            pstmt.setString(2, patient.getLastName());
            pstmt.setString(3, patient.getBloodGroup());
            pstmt.setString(4, patient.getPhoneNo());
            pstmt.setInt(5, Integer.parseInt(patient.getAge()));
            pstmt.setString(6, patient.getGender());
            pstmt.setString(7, patient.getPatientID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update patient data: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteButtonClick(ActionEvent event) {
        Patient selectedPatient = tblvTable.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            deletePatientFromDatabase(selectedPatient);
            patients.remove(selectedPatient);
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a patient to delete.");
        }
    }

    private void deletePatientFromDatabase(Patient patient) {
        String query = "DELETE FROM patient WHERE patient_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patient.getPatientID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete patient data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadPatientData() {
        String query = "SELECT * FROM patient";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String patientID = rs.getString("patient_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String bloodGroup = rs.getString("Blood_Group");
                String phoneNo = rs.getString("phone_no");
                String age = rs.getString("Age");
                String gender = rs.getString("Gender");
                patients.add(new Patient(patientID, firstName, lastName, bloodGroup, phoneNo, age, gender));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load patient data: " + e.getMessage());
        }
        tblvTable.setItems(patients);
    }

    @FXML
    public void handleSearchKeyReleased() {
        String searchText = txtfSearch.getText().toLowerCase();
        ObservableList<Patient> filteredList = FXCollections.observableArrayList();

        for (Patient patient : patients) {
            if (patient.getFirstName().toLowerCase().contains(searchText) ||
                    patient.getLastName().toLowerCase().contains(searchText) ||
                    patient.getBloodGroup().toLowerCase().contains(searchText) ||
                    patient.getPhoneNo().toLowerCase().contains(searchText) ||
                    String.valueOf(patient.getAge()).contains(searchText) ||
                    patient.getGender().toLowerCase().contains(searchText)) {
                filteredList.add(patient);
            }
        }

        tblvTable.setItems(filteredList);

    }

    private void exportToCSV() {
        StringBuilder csvData = new StringBuilder();
        // Add header
        csvData.append("\"Patient ID\",\"First Name\",\"Last Name\",\"Blood Group\",\"Phone No\",\"Age\",\"Gender\"\n");

        // Add patient data
        for (Patient patient : patients) {
            csvData.append("\"").append(patient.getPatientID()).append("\",")
                    .append("\"").append(patient.getFirstName()).append("\",")
                    .append("\"").append(patient.getLastName()).append("\",")
                    .append("\"").append(patient.getBloodGroup()).append("\",")
                    .append("\"").append(patient.getPhoneNo()).append("\",")
                    .append("\"").append(patient.getAge()).append("\",")
                    .append("\"").append(patient.getGender()).append("\"\n");
        }

        // Save to file
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(ankrMain.getScene().getWindow());

            if (file != null) {
                // Write using UTF-8 encoding
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                    writer.write(csvData.toString());
                }
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data exported to CSV successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to export data: " + e.getMessage());
        }
    }

    @FXML
    public void handlePrintButtonClick(ActionEvent event) {
        exportToCSV();

    }
}

