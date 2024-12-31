package com.example.healthflow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Logincontroller {

    @FXML
    private CheckBox CBoxShowPass;

    @FXML
    private Hyperlink HypLinkForgPass;

    @FXML
    private TextField tfuser;  // Username TextField

    @FXML
    private PasswordField tfpass;  // Password PasswordField

    @FXML
    private Button btnlog;  // Login Button

    @FXML
    private Label lblError;  // Error Label for login failure

    @FXML
    private TextField tfpassVisible = new TextField();  // TextField for showing plain password

    private final String DB_URL = "jdbc:mysql://localhost:3306/healthflow";
    private final String DB_USERNAME = "root";  // Replace with your MySQL username
    private final String DB_PASSWORD = "12345678";  // Replace with your MySQL password

    @FXML
    void initialize() {
        tfpassVisible.setManaged(false);
        tfpassVisible.setVisible(false);
        tfpassVisible.textProperty().bindBidirectional(tfpass.textProperty());

        CBoxShowPass.setOnAction(event -> togglePasswordVisibility());
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = tfuser.getText();
        String password = tfpass.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please enter both username and password.");
        } else {
            int validationResult = validateCredentials(username, password);

            switch (validationResult) {
                case 1:  // Incorrect username
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect username.");
                    break;
                case 2:  // Incorrect password
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect password.");
                    break;
                case 3:  // Both username and password are incorrect
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
                    break;
                case 0:  // Successful login
                    loadHomePage(event);  // Navigate to home page
                    break;
                default:
                    showAlert(Alert.AlertType.ERROR, "Error", "An unknown error occurred.");
            }
        }
    }

//    private void showAlert(Alert.AlertType alertType, String title, String message) {
//        Alert alert = new Alert(alertType);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }


    private int validateCredentials(String username, String password) {
        int result = 3;  // Assume both are incorrect
        String queryUsername = "SELECT * FROM user WHERE username = ?";
        String queryPassword = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Check if the username exists
            try (PreparedStatement usernameStmt = connection.prepareStatement(queryUsername)) {
                usernameStmt.setString(1, username);
                ResultSet rs = usernameStmt.executeQuery();
                if (rs.next()) {
                    result = 2;  // Username exists, but password might be wrong
                }
            }

            // Check if both username and password are correct
            if (result == 2) {
                try (PreparedStatement passwordStmt = connection.prepareStatement(queryPassword)) {
                    passwordStmt.setString(1, username);
                    passwordStmt.setString(2, password);
                    ResultSet rs = passwordStmt.executeQuery();
                    if (rs.next()) {
                        result = 0;  // Both username and password are correct
                    }
                }
            } else {
                result = 1;  // Username is incorrect
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }

        return result;
    }

    private void loadHomePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/healthflow/HomePage2.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("HealthFlow");
            stage.show();

            com.example.healthflow.HomeController homeController = loader.getController();
            homeController.loadDashboardData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        if (CBoxShowPass.isSelected()) {
            tfpassVisible.setText(tfpass.getText());
            tfpassVisible.setVisible(true);
            tfpassVisible.setManaged(true);
            tfpass.setVisible(false);
            tfpass.setManaged(false);
        } else {
            tfpass.setText(tfpassVisible.getText());
            tfpass.setVisible(true);
            tfpass.setManaged(true);
            tfpassVisible.setVisible(false);
            tfpassVisible.setManaged(false);
        }
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/healthflow/ForgotPassword.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Forgot Password");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Forgot Password page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
