import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class JoinGameScreen {
    public static void show(Stage stage) {
        StackPane root = new StackPane(new Label("Join Game Screen"));
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }
}