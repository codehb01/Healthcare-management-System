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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class BillingAndInvoiceController {

    @FXML
    public MenuItem DoctorTab;

    @FXML
    public DatePicker DpAD; // Admission Date

    @FXML
    public DatePicker DpDD; // Discharge Date

    @FXML
    public DatePicker DpDisD; // Due Date

    @FXML
    public DatePicker DpInv; // Invoice Date

    @FXML
    public MenuItem PatientTab;

    @FXML
    public MenuItem StaffTab;

    @FXML
    public TableColumn<?, ?> TcAmt; // Amount Column

    @FXML
    public TableColumn<?, ?> TcP; // Patient Column

    @FXML
    public TableColumn<?, ?> TcSr; // Sr Number Column

    @FXML
    public AnchorPane ankrDashboard;

    @FXML
    public AnchorPane ankrDashboard2;

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public AnchorPane ankrPersonalDetails;

    @FXML
    public AnchorPane ankrTitle;

    @FXML
    public Button btnAppointment;

    @FXML
    public Button btnBack;

    @FXML
    public Button btnBilling;

    @FXML
    public Button btnClearAll;

    @FXML
    public Button btnClinicalManagement;

    @FXML
    public Button btnHome;

    @FXML
    public Button btnPaid;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnUser;

    @FXML
    public Button btnUser1;

    @FXML
    public ImageView imgvUser;

    @FXML
    public Label lblClinicalManagement;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public TextField txtfDoctorId;

    @FXML
    public TextField txtfFirstName;

    @FXML
    public TextField txtfPatientID;

    @FXML
    public TextField txtfinvoiceno;

    @FXML
    public TextField txtfLastName;

    @FXML
    public TextField txtfTotalAmount;

    @FXML
    public VBox vbxUser;

    // JDBC URL, username, and password for MySQL database
    public static final String DB_URL = "jdbc:mysql://localhost:3306/healthflow";
    public static final String USER = "root"; // Your database username
    public static final String PASS = "12345678"; // Your database password

    // Auto-fetch first and last name of the patient based on the entered Patient ID
    @FXML
    void handlePatientIDInput(ActionEvent event) {
        String patientID = txtfPatientID.getText();
        if (patientID != null && !patientID.isEmpty()) {
            fetchPatientDetails(Integer.parseInt(patientID));
        }
    }

    // Method to fetch patient details
    public void fetchPatientDetails(int patientID) {
        String query = "SELECT first_name, last_name FROM patient WHERE patient_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtfFirstName.setText(rs.getString("first_name"));
                txtfLastName.setText(rs.getString("last_name"));
            } else {
                System.out.println("Patient not found with ID: " + patientID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Auto-increment invoice number
    @FXML
    public void initialize() {
        autoIncrementInvoiceNumber();
    }

    public void autoIncrementInvoiceNumber() {
        txtfinvoiceno.setText(String.valueOf(getNextInvoiceNo()));
    }

    public int getNextInvoiceNo() {
        int invoiceNo = 1; // Default starting number
        String query = "SELECT MAX(invoice_no) AS max_invoice_no FROM bill";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                invoiceNo = rs.getInt("max_invoice_no") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceNo;
    }

    @FXML
    void handleSaveButton(ActionEvent event) {
        String firstName = txtfFirstName.getText();
        String lastName = txtfLastName.getText();
        String doctorId = txtfDoctorId.getText();
        String totalAmountText = txtfTotalAmount.getText();

        if (totalAmountText != null && !totalAmountText.isEmpty()) {
            try {
                int totalAmount = Integer.parseInt(totalAmountText);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for total amount. Please enter a valid number.");
            }
        } else {
            System.out.println("Total amount field cannot be empty.");
        }

        String admitDate = (DpAD.getValue() != null) ? DpAD.getValue().toString() : null;
        String dischargeDate = (DpDD.getValue() != null) ? DpDD.getValue().toString() : null;
        String invoiceDate = (DpInv.getValue() != null) ? DpInv.getValue().toString() : null;
        String dueDate = (DpDisD.getValue() != null) ? DpDisD.getValue().toString() : null;

        int patientId = Integer.parseInt(txtfPatientID.getText());
        int invoiceNo = Integer.parseInt(txtfinvoiceno.getText());

        String sql = "INSERT INTO bill (first_name, last_name, due_date, admit_date, discharge_date, invoice_date, patient_id, doctor_id, amount, invoice_no) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, dueDate);
            pstmt.setString(4, admitDate);
            pstmt.setString(5, dischargeDate);
            pstmt.setString(6, invoiceDate);
            pstmt.setInt(7, patientId);
            pstmt.setInt(8, Integer.parseInt(doctorId));
            pstmt.setDouble(9, Double.parseDouble(totalAmountText));
            pstmt.setInt(10, invoiceNo);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Patient billing data saved successfully.");
            } else {
                System.out.println("Failed to save patient billing data.");
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    // Clear all button - clear all input fields
    @FXML
    void handleClearAllActions(ActionEvent event) {
        txtfFirstName.clear();
        txtfLastName.clear();
        txtfPatientID.clear();
        txtfinvoiceno.clear();
        txtfDoctorId.clear();
        txtfTotalAmount.clear();
        DpAD.setValue(null);
        DpDD.setValue(null);
        DpDisD.setValue(null);
        DpInv.setValue(null);
        System.out.println("Cleared all fields");
    }

    @FXML
    public void handleAppointmentButtonClick() throws IOException {
        switchScene("Appointment.fxml");
    }

    @FXML
    public void handleHomeButtonClick() throws IOException {
        switchScene("HomePage2.fxml");
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
    public void handleUserButtonClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            switchScene("LoginPage.fxml");
        }
    }

    @FXML
    public void handleProfileButtonClick() throws IOException {
        switchScene("Profile.fxml");
    }

    @FXML
    public void handleClinicalManagementTabClick() throws IOException {
        switchScene("ClinicalManagement.fxml");
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

    public void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) ankrMain.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void createHtmlReceipt(File file) {
        System.out.println("Generating HTML receipt at: " + file.getAbsolutePath());

        try (FileWriter writer = new FileWriter(file)) {
            // Write the HTML structure
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"en\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Receipt</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: 'Arial', sans-serif; margin: 20px; background-color: #e9f3f3; color: #333; }\n");
            writer.write("        .header, .footer { text-align: center; background-color: #007BFF; color: white; padding: 10px 0; }\n");
            writer.write("        .content { margin: 20px 0; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }\n");
            writer.write("        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }\n");
            writer.write("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("        th { background-color: #0056b3; color: white; }\n");
            writer.write("        .service-list { margin-top: 20px; }\n");
            writer.write("        h3, h4 { color: #87CEEB; }\n");
            writer.write("        ul { padding-left: 20px; }\n");
            writer.write("        li { margin: 5px 0; }\n");
            writer.write("        .important-notes { color: #ff5722; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");

            // Header
            writer.write("<div class=\"header\">\n");
            writer.write("    <h1>🩺 Healthflow 🩺</h1>\n");
            writer.write("    <h2>Your Trusted Healthcare</h2>\n");
            writer.write("</div>\n");

            // Invoice details
            writer.write("<div class=\"content\">\n");
            writer.write("    <h3>🩻 Invoice Details</h3>\n");
            writer.write("    <table>\n");
            writer.write("        <tr><th>Invoice No</th><td>" + txtfinvoiceno.getText() + "</td></tr>\n");
            writer.write("        <tr><th>Patient ID</th><td>" + txtfPatientID.getText() + "</td></tr>\n");
            writer.write("        <tr><th>First Name</th><td>" + txtfFirstName.getText() + "</td></tr>\n");
            writer.write("        <tr><th>Last Name</th><td>" + txtfLastName.getText() + "</td></tr>\n");
            writer.write("        <tr><th>Admission Date</th><td>" + DpAD.getValue() + "</td></tr>\n");
            writer.write("        <tr><th>Discharge Date</th><td>" + DpDD.getValue() + "</td></tr>\n");
            writer.write("        <tr><th>Due Date</th><td>" + DpDisD.getValue() + "</td></tr>\n");
            writer.write("        <tr><th>Invoice Date</th><td>" + DpInv.getValue() + "</td></tr>\n");
            writer.write("        <tr><th>Total Amount</th><td>" + txtfTotalAmount.getText() + "</td></tr>\n");
            writer.write("    </table>\n");

            // Services provided
            writer.write("    <div class=\"service-list\">\n");
            writer.write("        <h4>🩺 Health Services Provided:</h4>\n");
            writer.write("        <ul>\n");
            writer.write("            <li>✔️ Consultation</li>\n");
            writer.write("            <li>✔️ Treatment</li>\n");
            writer.write("            <li>✔️ Medication</li>\n");
            writer.write("            <li>✔️ Follow-up Care</li>\n");
            writer.write("        </ul>\n");
            writer.write("    </div>\n");

            // Important notes
            writer.write("    <h4 class=\"important-notes\">📜 Important Notes:</h4>\n");
            writer.write("    <ul>\n");
            writer.write("        <li>Please keep this receipt for your records.</li>\n");
            writer.write("        <li>Contact us for any discrepancies.</li>\n");
            writer.write("        <li>Our office hours: Mon-Fri, 9 AM - 6 PM</li>\n");
            writer.write("    </ul>\n");
            writer.write("</div>\n");

            // Footer
            writer.write("<div class=\"footer\">\n");
            writer.write("    <h4>🌟 Thank you for choosing us! 🌟</h4>\n");
            writer.write("    <p>Your health is our priority!</p>\n");
            writer.write("</div>\n");

            // Close HTML
            writer.write("</body>\n");
            writer.write("</html>\n");

            System.out.println("HTML receipt created successfully!");
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
    // Handle Paid button click - mark invoice as paid
    @FXML
    void handlePaidButton(ActionEvent event) {
        System.out.println("Invoice marked as paid");

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Receipt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));

        // Show the save dialog and get the selected file
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // Generate the HTML receipt at the selected location
            createHtmlReceipt(file);
        }
    }
}
