//30-09
package com.example.healthflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class ProfileController {

    @FXML
    public MenuItem DoctorTab;

    @FXML
    public MenuItem PatientTab;

    @FXML
    public MenuItem StaffTab;

    @FXML
    public AnchorPane ankrDashboard;

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public AnchorPane ankrTitle1;

    @FXML
    public AnchorPane ankrTitle11;

    @FXML
    public AnchorPane ankrTitle111;

    @FXML
    public Button btnAppointment;

    @FXML
    public Button btnBack;

    @FXML
    public Button btnBilling;

    @FXML
    public Button btnChangePassword;

    @FXML
    public Button btnClinicalManagement;

    @FXML
    public Button btnHome;

    @FXML
    public Button btnProfile;

    @FXML
    public Button btnUpdatePassword;

    @FXML
    public Button btnUser;

    @FXML
    public Hyperlink hyprFAQ;

    @FXML
    public ImageView imgvUser;

    @FXML
    public Label lblAppointment;

    @FXML
    public Label lblAvailability;

    @FXML
    public Label lblAvailability1;

    @FXML
    public Label lblDoctorID;

    @FXML
    public Label lblEmail;

    @FXML
    public Label lblName;

    @FXML
    public Label lblPhoneNo;

    @FXML
    public MenuButton mnuBtnRegistration;

    @FXML
    public TextField txtfEmail;

    @FXML
    public TextField txtfFirstName;

    @FXML
    public TextField txtfID;

    @FXML
    public TextField txtfID2;

    @FXML
    public TextField txtfLastName;

    @FXML
    public TextField txtfPhoneNo;

    @FXML
    public TextField txtfUsername;

    @FXML
    public VBox vbxUser;

    private String loggedInUsername; // Store the username of the logged-in user
    private Connection conn; // JDBC connection object

    // Method to set the logged-in username (called from LoginController)
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
        loadUserProfile();  // Fetch and load user profile details
    }

    // Fetch and display user details based on the logged-in username
    public void loadUserProfile() {
        String url = "jdbc:mysql://localhost:3306/healthflow"; // Change to your database URL
        String user = "root"; // Your database user
        String password = "12345678"; // Your database password
        try {
            // Get the user ID from the 'user' table based on the username
            String userIdQuery = "SELECT id FROM user WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userIdQuery);
            userStmt.setString(1, loggedInUsername);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("id");
                txtfID.setText(String.valueOf(userId));  // Display the user ID

                // Check if the user is a doctor
                String doctorQuery = "SELECT * FROM doctor WHERE doctor_id = ?";
                PreparedStatement doctorStmt = conn.prepareStatement(doctorQuery);
                doctorStmt.setInt(1, userId);
                ResultSet doctorRs = doctorStmt.executeQuery();

                if (doctorRs.next()) {
                    displayUserDetails(doctorRs);  // Display doctor details
                } else {
                    // Check if the user is a staff member
                    String staffQuery = "SELECT * FROM staff WHERE staff_id = ?";
                    PreparedStatement staffStmt = conn.prepareStatement(staffQuery);
                    staffStmt.setInt(1, userId);
                    ResultSet staffRs = staffStmt.executeQuery();

                    if (staffRs.next()) {
                        displayUserDetails(staffRs);  // Display staff details
                    } else {
                        System.out.println("No matching doctor or staff found for user.");
                    }
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display user details in text fields
    private void displayUserDetails(ResultSet rs) throws SQLException {
        txtfFirstName.setText(rs.getString("first_name"));
        txtfLastName.setText(rs.getString("last_name"));
        txtfPhoneNo.setText(rs.getString("phone_no"));
        txtfEmail.setText(rs.getString("email"));
        txtfUsername.setText(loggedInUsername);  // Display the username
    }

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
    public void handleResetButtonClick() throws IOException {
        switchScene("Forgotpassword.fxml");
    }
    @FXML
    void FAQPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/healthflow/FAQspage.fxml"));

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FAQ");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Forgot Password page.");
        }
    }
    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    // Method to handle Change Password button click and redirect to Forgot Password page
    @FXML
    public void handleChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Forgotpassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnChangePassword.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleUpdateUsername() {
        // Get ID from the first text field
        String idString = txtfID2.getText(); // Assuming txtfID is the TextField for ID
        // Get username from the second text field
        String username = txtfUsername.getText(); // Assuming txtfUsername is the TextField for username

        // Validate ID
        if (idString != null && !idString.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idString); // Parse the ID only if it's not empty
                // Validate Username
                if (username != null && !username.trim().isEmpty()) {
                    // Proceed with updating the username using the ID and Username
                    updateUsernameInDatabase(id, username); // Example method to update in DB
                } else {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Username field is empty. Please enter a valid username.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid ID format. Please enter a valid number.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID field is empty. Please enter a valid ID.");
        }
    }

    @FXML
    public void handleLogoutButtonClick() throws IOException {
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

    private void updateUsernameInDatabase(int id, String username) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/healthflow"; // Change to your database URL
        String user = "root"; // Your database user
        String password = "12345678"; // Your database password

        String updateQuery = "UPDATE user SET username = ? WHERE id = ?";

        // Use try-with-resources to ensure resources are closed after use
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            // Set the parameters for the query
            stmt.setString(1, username);
            stmt.setInt(2, id);

            // Execute the update query
            int rowsAffected = stmt.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Update Successful", "Username updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "No user found with the given ID.");
            }

        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the username.");
        }
    }



}





