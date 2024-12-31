package com.example.healthflow;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class UpdateAppointmentsController {

    @FXML
    public TableColumn<Appointment, String> ClnDate;

    @FXML
    public TableColumn<Appointment, String> ClnDur;

    @FXML
    public TableColumn<Appointment, String> ClnName;

    @FXML
    public TableColumn<Appointment, String> ClnSp;

    @FXML
    public TableColumn<Appointment, String> ClnTimeSlot;

    @FXML
    public TableColumn<Appointment, String> ClnType;

    @FXML
    public MenuItem DoctorTab;

    @FXML
    public MenuItem PatientTab;

    @FXML
    public MenuItem StaffTab;

    @FXML
    public AnchorPane ankrAppTable;

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
    public Button btnDelete;

    @FXML
    public Button btnHome;

    @FXML
    public Button btnUpdate1;

    @FXML
    public Button btnUser;

    @FXML
    public Button btnUser1;

    @FXML
    public ImageView imgvSearch;

    @FXML
    public ImageView imgvUser;

    @FXML
    public Label lblDashboard;

    @FXML
    public Label lblUpdateAppointment;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public TableView<Appointment> tblvAppTable;

    @FXML
    public TextField txtfSearch;

    @FXML
    public VBox vbxDashboard;

    @FXML
    public VBox vbxUser;

    @FXML
    void handleAppointmentButtonClick(ActionEvent event) {
        // Appointment button logic
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
    public void handleClinicalManagementTabClick() throws IOException {
        switchScene("ClinicalManagement.fxml");
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
        stage.setTitle("HealthFlow");
        stage.show();
    }

    public void handleBackButtonClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("HomePage2.fxml")));
        Parent homePageRoot = loader.load();
        HomeController homePageController = loader.getController();
        homePageController.refreshPage();
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        Scene homePageScene = new Scene(homePageRoot);
        stage.setScene(homePageScene);
        stage.show();
    }

    public class Appointment {
        private String appointmentNo;  // Assuming you have an appointment number
        private String date;
        private String duration;
        private String doctorName;
        private String specialty;
        private String timeSlot;
        private String type;

        public Appointment(String appointmentNo, String date, String duration, String doctorName, String specialty, String timeSlot, String type) {
            this.appointmentNo = appointmentNo;
            this.date = date;
            this.duration = duration;
            this.doctorName = doctorName;
            this.specialty = specialty;
            this.timeSlot = timeSlot;
            this.type = type;
        }

        public String getAppointmentNo() {
            return appointmentNo;
        }

        public String getDate() {
            return date;
        }

        public String getDuration() {
            return duration;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getSpecialty() {
            return specialty;
        }

        public String getTimeSlot() {
            return timeSlot;
        }

        public String getType() {
            return type;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public void setType(String type) {
            this.type = type;
        }
    }


    @FXML
    public void initialize() {
        ClnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        ClnDur.setCellValueFactory(new PropertyValueFactory<>("duration"));
        ClnName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        ClnSp.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        ClnTimeSlot.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        ClnType.setCellValueFactory(new PropertyValueFactory<>("type"));

        makeColumnsEditable();
        loadAppointmentData();
    }

    public void makeColumnsEditable() {
        ClnDate.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnDur.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnName.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnSp.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnTimeSlot.setCellFactory(TextFieldTableCell.forTableColumn());
        ClnType.setCellFactory(TextFieldTableCell.forTableColumn());

        ClnDate.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setDate(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });

        ClnDur.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setDuration(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });

        ClnName.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setDoctorName(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });

        ClnSp.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setSpecialty(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });

        ClnTimeSlot.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setTimeSlot(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });

        ClnType.setOnEditCommit(event -> {
            Appointment appointment = event.getRowValue();
            appointment.setType(event.getNewValue());
            updateAppointmentInDatabase(appointment);
        });
    }


    public void loadAppointmentData() {
        String query = "SELECT appointment_no, appointment_date, appointment_duration, doctor_name, speciality, time_slot, appointment_type FROM appointment";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ObservableList<Appointment> appointments = FXCollections.observableArrayList();

            while (rs.next()) {
                String appointmentNo = rs.getString("appointment_no");
                String date = rs.getString("appointment_date");
                String duration = rs.getString("appointment_duration");
                String doctorName = rs.getString("doctor_name");
                String specialty = rs.getString("speciality");
                String timeSlot = rs.getString("time_slot");
                String type = rs.getString("appointment_type");

                appointments.add(new Appointment(appointmentNo, date, duration, doctorName, specialty, timeSlot, type));
            }

            tblvAppTable.setItems(appointments);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load appointment data: " + e.getMessage());
        }
    }

    public void updateAppointmentInDatabase(Appointment appointment) {
        String query = "UPDATE appointment SET appointment_date = ?, appointment_duration = ?, doctor_name = ?, speciality = ?, time_slot = ?, appointment_type = ? WHERE appointment_no = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, appointment.getDate());
            pstmt.setString(2, appointment.getDuration());
            pstmt.setString(3, appointment.getDoctorName());
            pstmt.setString(4, appointment.getSpecialty());
            pstmt.setString(5, appointment.getTimeSlot());
            pstmt.setString(6, appointment.getType());
            pstmt.setString(7, appointment.getAppointmentNo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update appointment: " + e.getMessage());
        }
    }


    @FXML
    public void handleDeleteButtonClick(ActionEvent event) {
        Appointment selectedAppointment = tblvAppTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            deleteAppointmentFromDatabase(selectedAppointment);
            tblvAppTable.getItems().remove(selectedAppointment);
        } else {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an appointment to delete.");
        }
    }

    public void deleteAppointmentFromDatabase(Appointment appointment) {
        String query = "DELETE FROM appointment WHERE appointment_no = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, appointment.getAppointmentNo());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete appointment: " + e.getMessage());
        }
    }


    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleSearchKeyReleased() {
        String searchText = txtfSearch.getText().toLowerCase();
        ObservableList<Appointment> filteredList = FXCollections.observableArrayList();

        for (Appointment appointment : tblvAppTable.getItems()) {
            if (appointment.getDate().toLowerCase().contains(searchText) ||
                    appointment.getDuration().toLowerCase().contains(searchText) ||
                    appointment.getDoctorName().toLowerCase().contains(searchText) ||
                    appointment.getSpecialty().toLowerCase().contains(searchText) ||
                    appointment.getTimeSlot().toLowerCase().contains(searchText) ||
                    appointment.getType().toLowerCase().contains(searchText)) {
                filteredList.add(appointment);
            }
        }

        tblvAppTable.setItems(filteredList);
    }

}

