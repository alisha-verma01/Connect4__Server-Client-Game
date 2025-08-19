import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InviteFriendsScreen {

    private static final String FILE_NAME = "users.txt";

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2e005f;");

        Label title = new Label("Invite Friends");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(40, 0, 10, 0));
        root.setTop(title);

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        Label instructions = new Label("Select a friend to invite:");
        instructions.setTextFill(Color.WHITE);
        instructions.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        ComboBox<String> friendsDropdown = new ComboBox<>();
        friendsDropdown.setPromptText("Choose a username");
        friendsDropdown.setMaxWidth(300);

        ArrayList<String> allUsers = loadAllUsernamesFromFile();
        String currentUsername = GuiClient.getLoggedInUsername();

        for (String username : allUsers) {
            if (!username.equals(currentUsername)) {
                friendsDropdown.getItems().add(username);
            }
        }

        Button inviteButton = new Button("Send Invite");
        inviteButton.setCursor(Cursor.HAND);
        inviteButton.setStyle("-fx-background-color: #a879e7; -fx-text-fill: white; -fx-font-weight: bold;");
        inviteButton.setOnAction(e -> {
            String selectedFriend = friendsDropdown.getValue();
            if (selectedFriend != null && !selectedFriend.isEmpty()) {
                sendFriendRequest(selectedFriend);
                centerBox.getChildren().clear();
                Label sentLabel = new Label("Invite Sent!");
                sentLabel.setTextFill(Color.WHITE);
                sentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
                centerBox.getChildren().add(sentLabel);
            } else {
                Label errorLabel = new Label("Please select a friend!");
                errorLabel.setTextFill(Color.RED);
                errorLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                if (!centerBox.getChildren().contains(errorLabel)) {
                    centerBox.getChildren().add(errorLabel);
                }
            }
        });

        centerBox.getChildren().addAll(instructions, friendsDropdown, inviteButton);
        root.setCenter(centerBox);

        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        nav.setPadding(new Insets(30));

        Button backButton = new Button("← back");
        backButton.setPrefSize(120, 30);
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: #a879e7; -fx-background-radius: 20;");
        backButton.setCursor(Cursor.HAND);
        backButton.setOnAction(e -> DashboardScreen.show(stage));

        nav.getChildren().add(backButton);
        root.setBottom(nav);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Invite Friends");
        stage.show();
    }

    private static ArrayList<String> loadAllUsernamesFromFile() {
        ArrayList<String> usernames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length == 2) {
                    usernames.add(parts[0].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users.txt: " + e.getMessage());
        }
        return usernames;
    }

    private static void sendFriendRequest(String selectedFriend) {
        Message friendRequest = new Message(MessageType.FRIEND_REQUEST);
        friendRequest.setSenderUsername(GuiClient.getLoggedInUsername());
        friendRequest.setRecipientUsername(selectedFriend);
        GuiClient.sendMessage(friendRequest);
        System.out.println("Friend request sent to " + selectedFriend);
    }
}
