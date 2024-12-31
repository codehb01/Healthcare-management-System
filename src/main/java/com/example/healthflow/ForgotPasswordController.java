package com.example.healthflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ForgotPasswordController {

    @FXML
    public AnchorPane ankrMain;

    @FXML
    public Button btnBack;

    @FXML
    public Button btnClearAll;

    @FXML
    public Button btnResetPass;

    @FXML
    public PasswordField passfConfirmNewPass;

    @FXML
    public PasswordField passfNewPass;

    @FXML
    public TextField txtUsername;

    @FXML
    public TextField txtfSecurityAnswer;

    // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/healthflow";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "12345678";

    // Verify security answer by querying the database
    public boolean verifySecurityAnswer(String username, String answer) {
        String query = "SELECT security_answer FROM user WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String correctAnswer = rs.getString("security_answer");
                return correctAnswer.equals(answer); // Compare the provided answer with the stored answer
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Username not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
        }
        return false;
    }

    @FXML
    public void handleBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Loginpage.fxml"));  // Load the correct FXML
            Parent root = loader.load();

            Logincontroller loginController = loader.getController();  // Cast to LoginController
            // Now you can call methods on loginController if needed

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("HealthFlow");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update password in the database
    public void updatePassword(String username, String newPassword) {
        String query = "UPDATE user SET password = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Password reset successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Password reset failed. Try again.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
        }
    }

    @FXML
    public void handleResetPasswordAction() {
        String username = txtUsername.getText();
        String securityAnswer = txtfSecurityAnswer.getText();
        String newPassword = passfNewPass.getText();
        String confirmNewPassword = passfConfirmNewPass.getText();

        // Check if any field is empty
        if (username.isEmpty() || securityAnswer.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all the fields.");
            return;
        }

        // Check if passwords match
        if (!newPassword.equals(confirmNewPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "New password and confirmation do not match.");
            return;
        }

        // Check if newPassword is alphanumeric and contains a special character
        if (!isValidPassword(newPassword)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password must be alphanumeric and contain at least one special character.");
            return;
        }

        // Verify security answer
        if (verifySecurityAnswer(username, securityAnswer)) {
            // If correct, update the password
            updatePassword(username, newPassword);
            clearAllFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Incorrect security answer. Please try again.");
        }
    }

    // Method to validate password
    private boolean isValidPassword(String password) {
        // Check for at least one special character and alphanumeric
        boolean hasSpecialChar = !password.matches("[A-Za-z0-9 ]*");
        boolean isAlphanumeric = password.matches(".*[A-Za-z].*") && password.matches(".*[0-9].*");

        return hasSpecialChar && isAlphanumeric;
    }
    @FXML
    public void handleClearAllAction() {
        clearAllFields();
    }

    @FXML
    public void handleBackAction() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close(); // Close the current window
    }

    public void clearAllFields() {
        txtUsername.clear();
        txtfSecurityAnswer.clear();
        passfNewPass.clear();
        passfConfirmNewPass.clear();
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
