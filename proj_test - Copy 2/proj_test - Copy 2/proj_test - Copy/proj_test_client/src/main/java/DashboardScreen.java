import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class DashboardScreen {

    private static Label notificationBadge = new Label();  // 🆕

    // CHANGES
    public static ScrollPane allAvailablePlayers = new ScrollPane();
    public static ScrollPane allLeaderboardPlayers = new ScrollPane();

    public static VBox content = new VBox();
    static Label loading, loadingLeaderboard;
    public static ArrayList<String> allPlayers = new ArrayList<>();

    public static Stage popupStage1;
    public static Scene1 scene1;
    public static Stage dashboardStage;

    public static String createdGamesGameMode = "Hard";

    public static String currentScreen = "Dashboard";


    public static void show(Stage stage) {
        dashboardStage = stage;
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2e005f;");

        Message getAllUsers = new Message(MessageType.GET_ALL_PLAYERS,GuiClient.getLoggedInUsername());
        GuiClient.sendMessage(getAllUsers);

        // Top title and inbox
        StackPane topBar = new StackPane();
        topBar.setPadding(new Insets(50, 30, 0, 30)); // pushed slightly down

        Label title = new Label("Dashboard");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        StackPane.setAlignment(title, Pos.CENTER);

        ImageView inboxIcon = new ImageView(new Image(DashboardScreen.class.getResourceAsStream("/inbox_icon.png")));
        inboxIcon.setFitWidth(40);
        inboxIcon.setPreserveRatio(true);
        inboxIcon.setCursor(Cursor.HAND);
        StackPane.setAlignment(inboxIcon, Pos.TOP_RIGHT);

        notificationBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        notificationBadge.setTextFill(Color.WHITE);
        notificationBadge.setBackground(new Background(new BackgroundFill(Color.web("#a879e7"), new CornerRadii(50), Insets.EMPTY)));
        notificationBadge.setMinSize(20, 20);
        notificationBadge.setMaxSize(20, 20);
        notificationBadge.setAlignment(Pos.CENTER);
        notificationBadge.setVisible(false); // hidden when 0 requests
        StackPane.setAlignment(notificationBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(notificationBadge, new Insets(5, 10, 0, 0)); // adjust position over icon

        // Click to open Inbox
        inboxIcon.setOnMouseClicked(e -> {
            InboxScreen.show(stage);
        });

        topBar.getChildren().addAll(title, inboxIcon, notificationBadge);
        root.setTop(topBar);

        // Grid for dashboard buttons
        GridPane grid = new GridPane();
        grid.setHgap(60);
        grid.setVgap(50);
        grid.setPadding(new Insets(80, 60, 40, 60));
        grid.setAlignment(Pos.TOP_CENTER);

        VBox inviteBtn = buildButton("invite friends to play", stage);
        VBox friendsBtn = buildButton("my friends", stage);
        // CHANGES
        VBox leaderboardBtn = buildButton("score leaderboard", stage);
        loadingLeaderboard = new Label("loading info...");
        loadingLeaderboard.setStyle("-fx-text-fill: #ffffff");
        loadingLeaderboard.setVisible(false);
        //

        VBox leaderboardInfo = new VBox(leaderboardBtn, loadingLeaderboard);
        leaderboardInfo.setSpacing(10);

        VBox joinBtn = buildButton("join an open game", stage);
        loading = new Label("loading info...");
        loading.setStyle("-fx-text-fill: #ffffff");
        loading.setVisible(false);

        VBox joinInfo = new VBox(joinBtn, loading);
        joinInfo.setSpacing(10);


        grid.add(inviteBtn, 0, 0);
        grid.add(joinInfo, 1, 0);
        grid.add(friendsBtn, 0, 1);
        grid.add(leaderboardBtn, 1, 1);
        root.setCenter(grid);

        // Back button
        HBox nav = new HBox();
        nav.setPadding(new Insets(30));
        nav.setAlignment(Pos.BOTTOM_LEFT);

        StackPane back = createNavButton("← back", e -> {
            SettingsScreen.show(stage);
        });

        nav.getChildren().add(back);
        root.setBottom(nav);

        updateNotificationBadge();

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Connect Four - Dashboard");
        stage.show();
    }

    private static VBox buildButton(String text, Stage stage) {
        Button button = new Button(text);
        button.setCursor(Cursor.HAND);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 19));
        button.setTextFill(Color.WHITE);
        button.setPrefSize(550, 100);
        button.setStyle("""
        -fx-background-color: #a879e7;
        -fx-background-radius: 20;
    """);

        if (text.equalsIgnoreCase("my friends")) {
            button.setOnAction(e -> MyFriendsScreen.show(stage));
        }
        if (text.equalsIgnoreCase("invite friends to play")) {
            button.setOnAction(e -> InviteFriendsScreen.show(stage));
        }

        // CHANGES
        if (text.equalsIgnoreCase("score leaderboard")) {
            button.setOnAction(event -> {
                System.out.println("Clicked on show leaderboard");
                System.out.println(GuiClient.getLoggedInUsername());

                System.out.println(GuiClient.getLoggedInUsername());

                new Thread(() -> {
                    try {
                        Thread.sleep(200);  // wait 200 ms
                        Message message = new Message(GuiClient.getLoggedInUsername());
                        message.setType(MessageType.VIEW_LEADERBOARD);
                        GuiClient.clientConnection.send(message);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();

                loadingLeaderboard.setVisible(true);

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event1 -> {
                    // Your task here
                    loadingLeaderboard.setVisible(false);
                    showPopupLeaderboard(stage);
                });
                pause.play();

                System.out.println("Here: " + allPlayers);
            });
        }
        //


        if (text.equalsIgnoreCase("join an open game")) {
            button.setOnAction(event -> {
                System.out.println("Clicked");
                System.out.println(GuiClient.getLoggedInUsername());


                new Thread(() -> {
                    try {
                        Thread.sleep(200);  // wait 200 ms
                        Message getAllUsers = new Message(MessageType.GET_ALL_PLAYERS,GuiClient.getLoggedInUsername());
                        GuiClient.sendMessage(getAllUsers);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();

                allPlayers = GuiClient.getAllUsernames();
                content.getChildren().add(new Label(""));
                content.getChildren().clear();
                loading.setVisible(true);


                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event1 -> {
                    // Your task here
                    loading.setVisible(false);
                    showPopup(stage, allPlayers);
                });
                pause.play();

                System.out.println("Here: " + allPlayers);
//                System.out.println("Here: " + GuiClient.getAllUsernames());

            });
        }

        HBox wrapper = new HBox(button);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setPadding(new Insets(6));
        wrapper.setStyle("""
        -fx-background-color: #7037bb;
        -fx-background-radius: 28;
    """);

        VBox container = new VBox(wrapper);
        container.setAlignment(Pos.CENTER_LEFT);
        return container;
    }
    // CHANGES
    public static void showPopupLeaderboard(Stage owner) {
        Platform.runLater(() -> {

            Stage popupLeaderboardStage = new Stage();
            popupLeaderboardStage.initModality(Modality.APPLICATION_MODAL);
            popupLeaderboardStage.initOwner(owner);
            popupLeaderboardStage.setTitle("All Available Players");

            Button refreshList = new Button("Back");
            refreshList.setOnAction(event -> {
                popupLeaderboardStage.close();
            });

            // Create popup layout with consistent background color
            VBox popupLayout = new VBox(10, allLeaderboardPlayers, refreshList);
            popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #630D5F;");

            // Create scene with background color set immediately
            Scene popupScene = new Scene(popupLayout, 350, 400);
            popupScene.setFill(Color.web("#630D5F"));
            popupLeaderboardStage.setScene(popupScene);

            // Show the popup first (so it has dimensions), then reposition it
            popupLeaderboardStage.setOnShown(e -> {
                double centerX = owner.getX() + (owner.getWidth() - popupLeaderboardStage.getWidth()) / 2;
                double centerY = owner.getY() + (owner.getHeight() - popupLeaderboardStage.getHeight()) / 2;
                popupLeaderboardStage.setX(centerX);
                popupLeaderboardStage.setY(centerY);
            });

            popupLeaderboardStage.showAndWait();
        });
    }

    //

    public static void showPopup(Stage owner, ArrayList<String> allUsers) {
        Platform.runLater(() -> {

            popupStage1 = new Stage();
            popupStage1.initModality(Modality.APPLICATION_MODAL);
            popupStage1.initOwner(owner);
            popupStage1.setTitle("All Available Players");

            // Clear previous content
            content.getChildren().clear();
            content.setStyle("-fx-background-color: #630D5F;");

            for(String user: allUsers){

                    Label outputLabel = new Label(user);
                    outputLabel.setAlignment(Pos.CENTER);
                    outputLabel.setWrapText(true);
                    outputLabel.setMaxWidth(300);
                    outputLabel.setTextAlignment(TextAlignment.CENTER);
                    outputLabel.setStyle(
                            "-fx-text-fill: #000000; " +
                                    "-fx-background-color: #FFC8FE; " +
                                    "-fx-background-radius: 30px; " +
                                    "-fx-border-radius: 30px; " +
                                    "-fx-padding: 10px;"
                    );

                    outputLabel.setOnMouseClicked(event -> {
                        areYouSureQuestion(owner, outputLabel.getText());
                        System.out.println(outputLabel.getText());
                    });

                    content.getChildren().add(outputLabel);
                    content.setAlignment(Pos.CENTER);
                    content.setSpacing(10);

            }

            // Set the background color for the ScrollPane and its content
            allAvailablePlayers.setStyle("-fx-background-color: #630D5F; -fx-border-color: #630D5F;");
            allAvailablePlayers.setContent(null);  // Clear previous content
            allAvailablePlayers.setContent(content);

            // Set the viewport background color explicitly
            allAvailablePlayers.lookupAll(".viewport").forEach(node -> {
                node.setStyle("-fx-background-color: #630D5F;");
            });

            // Apply ScrollPane settings
            allAvailablePlayers.setFitToWidth(true);
            allAvailablePlayers.setPrefHeight(400);
            allAvailablePlayers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            allAvailablePlayers.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            Button refreshList = new Button("Back");
            refreshList.setOnAction(event -> {
                popupStage1.close();
            });

            // Create popup layout with consistent background color
            VBox popupLayout = new VBox(10, allAvailablePlayers, refreshList);
            popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #630D5F;");

            // Create scene with background color set immediately
            Scene popupScene = new Scene(popupLayout, 350, 400);
            popupScene.setFill(Color.web("#630D5F"));
            popupStage1.setScene(popupScene);

            // Show the popup first (so it has dimensions), then reposition it
            popupStage1.setOnShown(e -> {
                double centerX = owner.getX() + (owner.getWidth() - popupStage1.getWidth()) / 2;
                double centerY = owner.getY() + (owner.getHeight() - popupStage1.getHeight()) / 2;
                popupStage1.setX(centerX);
                popupStage1.setY(centerY);
            });

            popupStage1.showAndWait();
        });
    }
    // CHANGES
    public static void areYouSureQuestion(Stage owner, String opponent) {
        Platform.runLater(() -> {

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(owner);
            popupStage.setTitle("Are you sure?");

            Label areYouSure = new Label("Play a game with: " + opponent + "?");
            areYouSure.setStyle("-fx-text-fill: #ffffff");

            Button playGame = new Button("Yes");
            playGame.setOnAction(event -> {
                System.out.println("Lets play!");

                // Send scene change message to server targeting the selected client and myself
                if (QuestionBank.hardModeQuestions != null && QuestionBank.hardModeAnswers != null) {
                    Message message = new Message(GuiClient.getLoggedInUsername(), opponent, opponent, 1, true, "Space",
                            QuestionBank.hardModeQuestions, QuestionBank.hardModeAnswers, createdGamesGameMode, "Red");
                    // if game play person has token color - red then their opp - yellow vise versa

                    if(opponent.equals(GuiClient.getLoggedInUsername())) {
                        scene1 = new Scene1(DashboardScreen.getDashboardStage(), GuiClient.clientConnection,
                                message, GuiClient.getLoggedInUsername(), opponent, 1, "Default");
                        DashboardScreen.getDashboardStage().setScene(scene1.getScene());
                    }else{
                        GuiClient.sendMessage(message);
                    }
                } else {
                    System.err.println("Error: Question bank arrays are null!");
                }
                popupStage.close();
                popupStage1.close();

            });

            Button refreshList = new Button("No");
            refreshList.setOnAction(event -> {
                popupStage.close();
            });

            HBox options = new HBox(playGame, refreshList);
            options.setSpacing(10);
            options.setAlignment(Pos.CENTER);

            VBox popupLayout = new VBox(10, areYouSure, options);
            popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #630D5F;");

            Scene popupScene = new Scene(popupLayout, 250, 200);
            popupScene.setFill(Color.web("#630D5F"));
            popupStage.setScene(popupScene);

            popupStage.setOnShown(e -> {
                double centerX = owner.getX() + (owner.getWidth() - popupStage.getWidth()) / 2;
                double centerY = owner.getY() + (owner.getHeight() - popupStage.getHeight()) / 2;
                popupStage.setX(centerX);
                popupStage.setY(centerY);
            });

            popupStage.showAndWait();
        });
    }
    //

    private static StackPane createNavButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        button.setPrefWidth(120);
        button.setPrefHeight(30);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        button.setCursor(Cursor.HAND);
        button.setTextFill(Color.WHITE);
        button.setStyle("""
            -fx-background-color: #a879e7;
            -fx-background-radius: 20;
        """);

        StackPane buttonWrapper = new StackPane(button);
        buttonWrapper.setPadding(new Insets(4));
        buttonWrapper.setStyle("""
            -fx-background-color: #7037bb;
            -fx-background-radius: 20;
        """);

        return buttonWrapper;
    }

    public static void updateNotificationBadge() {
        int pendingRequests = GuiClient.getInboxRequests().size();
        if (pendingRequests > 0) {
            notificationBadge.setText(String.valueOf(pendingRequests));
            notificationBadge.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
        }
    }

    public static Stage getDashboardStage() {
        return dashboardStage;
    }

    public static String getCurrentScreen() {
        return currentScreen;
    }

    public static ScrollPane getAllLeaderboardPlayers() {
        return allLeaderboardPlayers;
    }

    public static void setAllLeaderboardPlayers(ScrollPane allLeaderboardPlayers) {
        DashboardScreen.allLeaderboardPlayers = allLeaderboardPlayers;
    }

}
