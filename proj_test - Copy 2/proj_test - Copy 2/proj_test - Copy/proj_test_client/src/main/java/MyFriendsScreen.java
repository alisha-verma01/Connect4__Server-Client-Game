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

public class MyFriendsScreen {

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2e005f;");

        VBox friendsBox = new VBox(20);
        friendsBox.setPadding(new Insets(30));
        friendsBox.setAlignment(Pos.CENTER);

        ArrayList<String> friends = GuiClient.getFriendsList();

        if (friends.isEmpty()) {
            Label emptyLabel = new Label("You don't have any friends yet.");
            emptyLabel.setTextFill(Color.WHITE);
            emptyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            friendsBox.getChildren().add(emptyLabel);
        } else {
            for (String friend : friends) {
                HBox friendRow = new HBox(20);
                friendRow.setAlignment(Pos.CENTER);

                Label friendLabel = new Label(friend);
                friendLabel.setTextFill(Color.WHITE);
                friendLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

                Button removeButton = new Button("X");
                removeButton.setCursor(Cursor.HAND);
                removeButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 20;");
                removeButton.setOnAction(e -> {
                    GuiClient.removeFriend(friend);
                    friendsBox.getChildren().remove(friendRow);
                });

                friendRow.getChildren().addAll(friendLabel, removeButton);
                friendsBox.getChildren().add(friendRow);
            }
        }

        root.setCenter(friendsBox);

        Button backButton = new Button("← Back to Dashboard");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: #a879e7; -fx-background-radius: 20;");
        backButton.setCursor(Cursor.HAND);
        backButton.setOnAction(e -> {
            DashboardScreen.show(stage);
        });

        VBox bottomBox = new VBox(backButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("My Friends");
        stage.show();
    }
}
