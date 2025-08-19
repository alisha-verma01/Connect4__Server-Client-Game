import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.Cursor;

public class InboxScreen {

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2e005f;");

        VBox requestsBox = new VBox(20);
        requestsBox.setPadding(new Insets(30));
        requestsBox.setAlignment(Pos.CENTER);

        ArrayList<String> requests = GuiClient.getInboxRequests();

        if (requests.isEmpty()) {
            Label emptyLabel = new Label("No friend requests at this time.");
            emptyLabel.setTextFill(Color.WHITE);
            emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            requestsBox.getChildren().add(emptyLabel);
        } else {
            for (String sender : requests) {
                HBox requestRow = new HBox(20);
                requestRow.setAlignment(Pos.CENTER);

                Label requestLabel = new Label(sender + " sent you a friend request");
                requestLabel.setTextFill(Color.WHITE);
                requestLabel.setFont(Font.font("Arial", 18));

                Button acceptButton = new Button("Accept");
                acceptButton.setCursor(Cursor.HAND);
                acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                acceptButton.setOnAction(e -> {
                    GuiClient.acceptFriendRequest(sender);
                    requestsBox.getChildren().remove(requestRow); // remove request from view
                    DashboardScreen.updateNotificationBadge(); // update inbox badge
                });

                Button declineButton = new Button("Decline");
                declineButton.setCursor(Cursor.HAND);
                declineButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                declineButton.setOnAction(e -> {
                    GuiClient.deleteFriendRequest(sender);
                    requestsBox.getChildren().remove(requestRow);
                    DashboardScreen.updateNotificationBadge(); // update inbox badge
                });

                requestRow.getChildren().addAll(requestLabel, acceptButton, declineButton);
                requestsBox.getChildren().add(requestRow);
            }
        }

        root.setCenter(requestsBox);

        // Back Button to return to Dashboard
        Button backButton = new Button("← Back to Dashboard");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: #a879e7; -fx-background-radius: 20;");
        backButton.setCursor(Cursor.HAND);
        backButton.setOnAction(e -> DashboardScreen.show(stage));

        VBox bottomBox = new VBox(backButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Inbox - Friend Requests");
        stage.show();
    }
}
