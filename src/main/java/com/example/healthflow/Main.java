package com.example.healthflow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class
Main extends Application {
    @Override

    //method -that is entry point of application
    public void start(Stage primaryStage) throws Exception {
        //loads fxml file
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Loginpage.fxml")));
        primaryStage.setTitle("HealthFlow");
        // Set the application logo
        Image appLogo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("HealthFlowLogo_x16_drawing.png")));
        primaryStage.getIcons().add(appLogo);
        //first scene
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Attempt to establish a connection to the MySQL database
        try {
            // Optionally load the MySQL JDBC driver (not mandatory for newer versions of JDBC)
            //changes ithe kele
            Class.forName("com.mysql.cj.jdbc.Driver");  // Optional for JDBC 4.0 and later

            // Create the connection object using correct syntax
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthflow", "root", "12345678");
            System.out.println("Connected to the database successfully!");

            // You can perform database operations here or pass the connection object to your controllers

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        }

        // Launch the JavaFX application
        launch(args);
    }
}
