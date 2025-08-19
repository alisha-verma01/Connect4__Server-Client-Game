//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.scene.Scene;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.BorderPane;
//import javafx.stage.Stage;
//
//public class GuiServer extends Application {
//
//    private Server server;
//    private ListView<String> serverLog;
//
//    @Override
//    public void start(Stage primaryStage) {
//        serverLog = new ListView<>();
//        server = new Server(message -> {
//            Platform.runLater(() -> {
//                serverLog.getItems().add("Received: " + message.getType());
//            });
//        });
//
//        BorderPane root = new BorderPane(serverLog);
//        Scene scene = new Scene(root, 400, 400);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Server GUI");
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
