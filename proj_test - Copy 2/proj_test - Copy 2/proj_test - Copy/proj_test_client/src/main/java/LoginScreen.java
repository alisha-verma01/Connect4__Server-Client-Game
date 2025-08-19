import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

import java.net.URL;
import java.net.Socket;

public class LoginScreen extends Application {

    private final Connect4Class connect4 = new Connect4Class();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Connect Four - Login");


        // Load and verify image
        URL imageUrl = getClass().getResource("/welcome_bg.png");
        if (imageUrl == null) {
            System.out.println("❌ Image not found. Check path.");
        } else {
            System.out.println("✅ Image loaded: " + imageUrl);
        }

        // Background image setup
        Image bgImage = new Image(imageUrl.toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(800);
        bgView.setFitHeight(610);
        bgView.setPreserveRatio(false);

        // Welcome text
        Text welcomeText = new Text("Welcome");
        welcomeText.setFill(Color.WHITE);
        welcomeText.setFont(Font.font("Arial", FontWeight.BOLD, 36));

        // Username field
        Label usernameLabel = new Label("User Name");
        usernameLabel.setTextFill(Color.web("#444"));
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(360);
        usernameField.setPrefHeight(35);

        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setTextFill(Color.web("#444"));
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(360);
        passwordField.setPrefHeight(35);

        // Status label
        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);

        Region spacer = new Region();
        spacer.setPrefHeight(20); // controls vertical spacing

        // Log In Button
        Button loginButton = new Button("Log In");
        loginButton.setCursor(Cursor.HAND);

        loginButton.setPrefWidth(360);
        loginButton.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 0;");

        loginButton.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            if (connect4.validateUser(user, pass)) {
                statusLabel.setText("Login successful!");
                statusLabel.setTextFill(Color.GREEN);

                GuiClient.startConnection();
                GuiClient.setLoggedInUsername(user);


                new Thread(() -> {
                    try {
                        Thread.sleep(200);  // wait 200 ms
                        Message loginMessage = new Message(MessageType.LOGIN);
                        loginMessage.setSenderUsername(user);
                        GuiClient.sendMessage(loginMessage);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();

                SettingsScreen.show(primaryStage);



            } else {
                statusLabel.setText("Invalid credentials.");
                statusLabel.setTextFill(Color.RED);
            }
        });

        // Create Account Button
        Button createButton = new Button("Create Account");
        createButton.setCursor(Cursor.HAND);
        createButton.setPrefWidth(360);
        createButton.setStyle("-fx-background-color: #4B0082; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 0;");

        createButton.setOnAction(e -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                statusLabel.setText("Fields cannot be empty.");
                statusLabel.setTextFill(Color.RED);
                return;
            }
            if (connect4.createUser(user, pass)) {
                statusLabel.setText("Account created! You can now log in.");
                statusLabel.setTextFill(Color.GREEN);
            } else {
                statusLabel.setText("Username already exists.");
                statusLabel.setTextFill(Color.RED);
            }
        });

        // Login card (white box)
        VBox loginCard = new VBox(15);
        loginCard.setAlignment(Pos.CENTER_LEFT);
        loginCard.setPadding(new Insets(30));
        loginCard.setMaxWidth(420);
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        loginCard.getChildren().addAll(
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                spacer,
                loginButton, createButton,
                statusLabel
        );

        // Wrapper VBox for header + card
        VBox layoutWrapper = new VBox(40);
        layoutWrapper.setAlignment(Pos.TOP_CENTER);
        layoutWrapper.setPadding(new Insets(55, 0, 0, 18)); // top, right, bottom, left
        layoutWrapper.getChildren().addAll(welcomeText, loginCard);

        // Final root layout with layered background
        StackPane root = new StackPane();
        root.getChildren().addAll(bgView, layoutWrapper);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

//    private void goToSettingsScene(Stage stage, String username) {
//        Label welcome = new Label("Welcome, " + username + "! This is the settings screen.");
//        VBox layout = new VBox(10, welcome);
//        layout.setAlignment(Pos.CENTER);
//        layout.setStyle("-fx-background-color: white;");
//        Scene settingsScene = new Scene(layout, 800, 600);
//        stage.setScene(settingsScene);
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
