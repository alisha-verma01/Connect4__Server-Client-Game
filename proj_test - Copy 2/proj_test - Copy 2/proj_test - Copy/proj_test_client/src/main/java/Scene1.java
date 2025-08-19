import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Random;

// CONNECT 4 game!!
public class Scene1 {

    private Scene scene;
    private String clientID;
    private String selectedClientID;
    private Stage primaryStage;
    private Client clientConnection;
    private Scene mainScene;
    Message message;

    private int playerTurn;

    //////////////////////////////

    GameInfo gameInfo;

    String player1;
    String player2;

    private ImageView[][] boardSpotsImages = new ImageView[7][6];
    private BoardSpot [][] board = new BoardSpot[7][6];

    int secondsPassed = 0;

    int rowIndex;
    int colIndex;

    private GridPane gridPane;

    Boolean endGame = false;

    boolean setDisable = true;

    StackPane finalLayout;

    VBox layout;

    StackPane connect4Box;

    HBox gameOptions;

    int availableRow;

    ImageView player1HouseBackgroundImage;
    ImageView player2HouseBackgroundImage;

    VBox content;

    ScrollPane scrollPane = new ScrollPane();

    String selectedTheme;

    Boolean isSinglePlayer = false;

    String connect4GameMode;
    //////////////////////////////

    // Click on grid and update how it looks on this client
    EventHandler<MouseEvent> z = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {
            //gets the row index of which button you clicked on
            //casting into a button
            rowIndex = GridPane.getRowIndex(((ImageView) t.getSource()));
            //gets the column index of which button you clicked on
            colIndex = GridPane.getColumnIndex(((ImageView) t.getSource()));
            System.out.println(colIndex + " " + rowIndex);
            if(connect4GameMode.equals("Hard")) {
                askQuestionPopUp();
            }
            makeGameBoardAdjustments(colIndex, playerTurn);

        }
    };

    public Scene1(Stage primaryStage, Client clientConnection, Message data,
                  String clientID, String selectedClientID, int turn, String selectedTheme) {
        this.primaryStage = primaryStage;
        this.clientConnection = clientConnection;
        this.message = data;
        this.clientID = clientID;
        this.selectedClientID = selectedClientID;
        this.playerTurn = turn;
        this.selectedTheme = SettingsScreen.getSelectedTheme();
        this.connect4GameMode = SettingsScreen.getSelectedDifficulty();



        //this.connect4GameMode = data.getCreatedGamesGameMode();

        this.player1 = message.getPlayer1();
        this.player2 = message.getPlayer2();

        if(player1.equals(player2)){
            isSinglePlayer = true;
            this.connect4GameMode = "Easy";
        }else{
            isSinglePlayer = false;
        }


        createScene();
    }


    private void createScene() {
        System.out.println("Create Scene");

        connectFourGame();

//        updateScene(1);
    }

    public void connectFourGame(){

        System.out.println(message.getHardModeQuestions());
        System.out.println(message.getHardModeAnswers());

        if(isSinglePlayer) {
            player2 = "AI";
        }

        //gameInfo = new GameInfo("Default", "Easy", player1, player2);

        Label gameTitle = new Label("Connect 4: Awesome Edition");
        gameTitle.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 24px;");

        Label gameMode = new Label(connect4GameMode + " Mode");
        gameMode.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");

        VBox gameTitleText = new VBox(gameTitle,gameMode);
        gameTitleText.setAlignment(Pos.TOP_CENTER); // Align all children to the center
        gameTitleText.setTranslateY(10);

        Image gameBoard = new Image("Images/GameBoard.png");
        ImageView gameBoardImage = new ImageView(gameBoard);

        gameBoardImage.setFitWidth(470);
        gameBoardImage.setFitHeight(400);


        // Default Red
        Image player1HouseBackground = new Image("Images/PlayerHouse.png");
        Image player1HouseHighlightedBackground = new Image("Images/PlayerHouseHighlighted.png");

        player1HouseBackgroundImage = new ImageView(player1HouseBackground);
        if(playerTurn == 1){
            player1HouseBackgroundImage.setImage(player1HouseHighlightedBackground);
        }
        player1HouseBackgroundImage.setFitWidth(120);
        player1HouseBackgroundImage.setFitHeight(170);

        Image player1Token = new Image("Images/RedToken.png");

        if(message.getGameCreatorsTokenColor().equals("Red")){
            player1Token = new Image("Images/RedToken.png");
        }else if(message.getGameCreatorsTokenColor().equals("Yellow")){
            player1Token = new Image("Images/YellowToken.png");
        }

        ImageView player1TokenImage = new ImageView(player1Token);
        player1TokenImage.setTranslateY(-22);

        Label player1Text = new Label("Player 1");
        player1Text.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");
        Label player1Username = new Label(player1);
        player1Username.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px;");

        VBox player1HouseText = new VBox(player1Text, player1Username);
        player1HouseText.setAlignment(Pos.CENTER);
        player1HouseText.setTranslateY(45);

        StackPane player1House = new StackPane(player1HouseBackgroundImage, player1TokenImage, player1HouseText);

        // Default Yellow
        Image player2HouseBackground = new Image("Images/PlayerHouse.png");
        Image player2HouseHighlightedBackground = new Image("Images/PlayerHouseHighlighted.png");

        player2HouseBackgroundImage = new ImageView(player2HouseBackground);
        if(playerTurn == 2){
            player2HouseBackgroundImage.setImage(player2HouseHighlightedBackground);
        }

        player2HouseBackgroundImage.setFitWidth(120);
        player2HouseBackgroundImage.setFitHeight(170);

        Image player2Token = new Image("Images/YellowToken.png");

        if(message.getGameCreatorsTokenColor().equals("Yellow")){
            player2Token = new Image("Images/RedToken.png");
        }

        ImageView player2TokenImage = new ImageView(player2Token);
        player2TokenImage.setTranslateY(-22);

        Label player2Text = new Label("Player 2");
        player2Text.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px;");
        Label player2Username = new Label(player2);
        player2Username.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 18px;");

        VBox player2HouseText = new VBox(player2Text, player2Username);
        player2HouseText.setAlignment(Pos.CENTER);
        player2HouseText.setTranslateY(45);

        StackPane player2House = new StackPane(player2HouseBackgroundImage, player2TokenImage, player2HouseText);


        HBox gamePlayHbox = new HBox(player1House, gameBoardImage, player2House);
        gamePlayHbox.setSpacing(20);  // 30 pixels between elements
        gamePlayHbox.setAlignment(Pos.CENTER);
        gamePlayHbox.setTranslateY(20);

        gameTitleText.setTranslateY(10);
        gamePlayHbox.setTranslateY(10);

        Image rulesLogo = new Image("Images/RulesLogo.png");
        ImageView rulesLogoImage = new ImageView(rulesLogo);
        rulesLogoImage.setFitWidth(80);
        rulesLogoImage.setFitHeight(80);

        rulesLogoImage.setOnMouseClicked(event -> {
            // Rules logo clicked
            showInfoPopup(primaryStage);
        });

        rulesLogoImage.setOnMouseEntered(e -> rulesLogoImage.setTranslateY(-5));
        rulesLogoImage.setOnMouseExited(e -> rulesLogoImage.setTranslateY(5));

        Image messagesLogo = new Image("Images/Messaging.png");
        ImageView messagesLogoImage = new ImageView(messagesLogo);
        messagesLogoImage.setFitWidth(80);
        messagesLogoImage.setFitHeight(80);

        messagesLogoImage.setOnMouseClicked(e -> showPopup(primaryStage));

        messagesLogoImage.setOnMouseEntered(e -> messagesLogoImage.setTranslateY(-5));
        messagesLogoImage.setOnMouseExited(e -> messagesLogoImage.setTranslateY(5));

        Image exitLogo = new Image("Images/ExitLogo.png");
        ImageView exitLogoImage = new ImageView(exitLogo);
        exitLogoImage.setFitWidth(80);
        exitLogoImage.setFitHeight(80);

        exitLogoImage.setOnMouseClicked(event -> {
//             Exit logo clicked

            System.out.println("Exit clicked by: " + GuiClient.getLoggedInUsername());

            // Create exit message with proper sender and recipient info
            String currentUser = GuiClient.getLoggedInUsername();
            String otherPlayer = currentUser.equals(player1) ? player2 : player1;

            Message exitMessage = new Message(MessageType.EXIT_GAME);
            exitMessage.setSenderUsername(currentUser);
            exitMessage.setRecipientUsername(otherPlayer);

            System.out.println("Sending exit message from " + currentUser + " to " + otherPlayer);

            // Send the message
            clientConnection.send(exitMessage);

//            // Return to main scene
            DashboardScreen.show(primaryStage);
        });

        exitLogoImage.setOnMouseEntered(e -> exitLogoImage.setTranslateY(-5));
        exitLogoImage.setOnMouseExited(e -> exitLogoImage.setTranslateY(5));

        gameOptions = new HBox(rulesLogoImage, messagesLogoImage, exitLogoImage);
        gameOptions.setSpacing(30);
        gameOptions.setAlignment(Pos.CENTER);


        // set up default game grid
        gridPane = new GridPane();
        for(int col = 0; col < 7; col++){
            for(int row = 0; row < 6; row++){

                Image image = new Image("Images/EmptySlot.png"); // or use a URL or resource path
                Image highlightedImage = new Image("Images/EmptySlotHighlight.png"); // or use a URL or resource path

                boardSpotsImages[col][row] = new ImageView(image);

                boardSpotsImages[col][row].setFitWidth(50); // resize if needed
                boardSpotsImages[col][row].setFitHeight(50);

                board[col][row] = new BoardSpot(0); // 0 means empty no chips: 1 - player 1 chip, 2 - player 2 chip

                gridPane.add(boardSpotsImages[col][row], col, row);

            }
        }

        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows

        gridPane.setTranslateY(40);
        gridPane.setTranslateX(197);

        connect4Box = new StackPane(gamePlayHbox, gridPane);
        connect4Box.setAlignment(Pos.CENTER);

        content = new VBox(10);
        content.setStyle("-fx-padding: 10;");
        scrollPane.setStyle("-fx-background-color: #630D5F; -fx-border-color: #630D5F");

        System.out.println("ClientID: " + clientID);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                //setting the onMouseClicked property for each of the buttons to call z (the event handler)
                if(this.clientID == this.message.getPlayer1()) {
                    boardSpotsImages[i][j].setOnMouseClicked(z);
                }
            }
        }

        layout = new VBox(gameTitleText, connect4Box, gameOptions);
        layout.setSpacing(20);  // 10 pixels between elements
        layout.setAlignment(Pos.TOP_CENTER);
        System.out.println("Selected theme: " + selectedTheme);

        if(selectedTheme.equals("Default")) {
            layout.setStyle("-fx-background-color: #25004C;");
        }else if (selectedTheme.equals("Space")){
            setBackground("Images/Space.jpg");
        }else if (selectedTheme.equals("Nature")){
            setBackground("Images/Nature.jpg");
        }else if (selectedTheme.equals("Mario")){
            setBackground("Images/Mario.jpg");
        }else if (selectedTheme.equals("Day")){
            setBackground("Images/Day.jpg");
        }else if (selectedTheme.equals("Night")){
            setBackground("Images/Night.jpg");
        }

        finalLayout = new StackPane(layout);

        scene = new Scene(finalLayout, 800, 600);
        primaryStage.setScene(scene);

    }

    public void setBackground(String file){
        Image backgroundImage = new Image(file); // Make sure the path is correct

        // Create BackgroundImage
        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true
                )
        );

        // Set it as the background
        layout.setBackground(new Background(bgImage));
    }

    private void showInfoPopup(Stage owner) {
        Stage popupInfoStage = new Stage();
        popupInfoStage.initModality(Modality.APPLICATION_MODAL);
        popupInfoStage.initOwner(owner);


        Label label = new Label("Lets play Connect 4!");
        label.setStyle("-fx-text-fill: #000000; -fx-font-size: 20px;");

        Label playGameInfo = new Label("Each player takes a turn to drop a coin into a spot on the game board. Click on an empty spot to drop the coin before your turn is up! Get 5 coins in a row to win! Wanna message the other player? Click on the message button. Click on the exit button to leave the game. Good Luck!");
        playGameInfo.setStyle("-fx-text-fill: #000000; -fx-font-size: 16px;");
        playGameInfo.setWrapText(true);
        playGameInfo.setMaxWidth(250);
        playGameInfo.setTextAlignment(TextAlignment.CENTER);

        Button back = new Button("Back");
        back.setMaxWidth(50);
        back.setMaxHeight(30);
        back.setOnMouseClicked(e -> popupInfoStage.close());
        back.setStyle("-fx-background-color: #8D7CF6; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-text-fill: #ffffff");
        back.setTextAlignment(TextAlignment.CENTER);
        back.setAlignment(Pos.CENTER);

        VBox popupLayout = new VBox(10, label, playGameInfo, back);
        popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #ffffff; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: #000000; -fx-border-width: 5px");
        popupLayout.setSpacing(20);
        popupLayout.setAlignment(Pos.CENTER);

        // Remove the title bar and make window undecorated
        popupInfoStage.initStyle(StageStyle.TRANSPARENT);
        // Make the scene background transparent so only the VBox is visible
        Scene popupScene = new Scene(popupLayout, 350, 350);
        popupScene.setFill(Color.TRANSPARENT);

        popupInfoStage.setScene(popupScene);

        // Show the popup first (so it has dimensions), then reposition it
        popupInfoStage.setOnShown(e -> {
            double centerX = owner.getX() + (owner.getWidth() - popupInfoStage.getWidth()) / 2;
            double centerY = owner.getY() + (owner.getHeight() - popupInfoStage.getHeight()) / 2;
            popupInfoStage.setX(centerX);
            popupInfoStage.setY(centerY);
        });

        popupInfoStage.showAndWait();
    }

    private void showPopup(Stage owner) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(owner);
        popupStage.setTitle("Message Opponent");

        int currentPlayer;

        if(player1.equals(GuiClient.getLoggedInUsername())){
            currentPlayer = 1;
        }else{
            currentPlayer = 2;
        }

        Label label = new Label("Player " + currentPlayer + "'s Chat");
        label.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 20px;");

        Image exitLogo = new Image("Images/exitMessage.png");
        ImageView exitLogoImage = new ImageView(exitLogo);
        exitLogoImage.setFitWidth(30);
        exitLogoImage.setFitHeight(30);

        exitLogoImage.setOnMouseClicked(e -> popupStage.close());

        HBox titleOfMessages = new HBox(label, exitLogoImage);
        titleOfMessages.setSpacing(10);
        titleOfMessages.setStyle("-fx-alignment: center; " +
                "-fx-background-color: #360534; -fx-max-width: 500px; -fx-min-width: 500px; -fx-padding: 10px");
        titleOfMessages.setTranslateY(-25);
        titleOfMessages.setMaxWidth(500);
        titleOfMessages.setMinWidth(500);

        content.setStyle("-fx-text-fill: #ffffff;-fx-background-color: #630D5F; -fx-border-color: #630D5F");
        content.setAlignment(Pos.CENTER_RIGHT);  // Positions all children to the right

        // Set the background color for ScrollPane and its viewport
        scrollPane.setStyle("-fx-background: #630D5F; -fx-background-color: #630D5F; -fx-border-color: #630D5F");

        // This is the key fix: setting the background color on the viewport
        scrollPane.setContent(content);

        // Apply style to the viewport
        scrollPane.lookupAll(".viewport").forEach(node -> {
            node.setStyle("-fx-background-color: #630D5F;");
        });

        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);

        // Hide scrollbars
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Use Platform.runLater to ensure scroll happens after layout is complete
        Platform.runLater(() -> {
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });

        VBox container = new VBox(scrollPane);
        container.setStyle("-fx-padding: 5; -fx-background-color: #630D5F; -fx-border-color: #630D5F");
        container.setTranslateY(-20);

        TextField inputTextField = new TextField();
        inputTextField.setPromptText("Enter your name");
        inputTextField.setStyle("-fx-background-color: #360534; -fx-text-fill: #ffffff");
        inputTextField.setMaxWidth(250);
        inputTextField.setMinWidth(250);

        Image sendMessage = new Image("Images/send.png");
        ImageView sendMessageImage = new ImageView(sendMessage);
        sendMessageImage.setFitWidth(30);
        sendMessageImage.setFitHeight(30);

        sendMessageImage.setOnMouseClicked(event -> {
            onSendMessage(inputTextField);
        });

        inputTextField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                onSendMessage(inputTextField);
            }
        });

        HBox messageAndSend = new HBox(inputTextField, sendMessageImage);
        messageAndSend.setSpacing(20);
        messageAndSend.setAlignment(Pos.CENTER);

        VBox popupLayout = new VBox(10, titleOfMessages, container, messageAndSend);
        popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #630D5F");

        Scene popupScene = new Scene(popupLayout, 350, 400);
        popupStage.setScene(popupScene);

        // Show the popup first (so it has dimensions), then reposition it
        popupStage.setOnShown(e -> {
            double centerX = owner.getX() + (owner.getWidth() - popupStage.getWidth()) / 2;
            double centerY = owner.getY() + (owner.getHeight() - popupStage.getHeight()) / 2;
            popupStage.setX(centerX);
            popupStage.setY(centerY);
        });

        popupStage.showAndWait();
    }

    public void onSendMessage(TextField inputTextField){
        Label outputLabel = new Label();
        String input = inputTextField.getText();
        if (!inputTextField.getText().trim().isEmpty()) {
            outputLabel.setText(input);

            Message messageMakeMove;
            System.out.println("Player: " + clientID + " sent a message");

            // Create message with proper sender and recipient info
            String currentUser = GuiClient.getLoggedInUsername();
            String otherPlayer = currentUser.equals(player1) ? player2 : player1;

            messageMakeMove = new Message(currentUser, otherPlayer, input);
            messageMakeMove.setType(MessageType.MESSAGE);
            clientConnection.send(messageMakeMove);

            outputLabel.setAlignment(Pos.CENTER);
            outputLabel.setWrapText(true);
            outputLabel.setMaxWidth(180);
            outputLabel.setTextAlignment(TextAlignment.CENTER);

            outputLabel.setStyle(
                    "-fx-text-fill: #000000; " +
                            "-fx-background-color: #FFC8FE; " +
                            "-fx-background-radius: 30px; " +
                            "-fx-border-radius: 30px; " +
                            "-fx-padding: 10px;"
            );
            content.getChildren().add(outputLabel);
        }
    }

    public void addReceivedMessageToContent(String message){

        Label outputLabel = new Label();
        outputLabel.setText(message);

        outputLabel.setAlignment(Pos.CENTER);
        outputLabel.setWrapText(true);
        outputLabel.setMaxWidth(180);
        outputLabel.setTextAlignment(TextAlignment.CENTER);

        outputLabel.setStyle(
                "-fx-text-fill: #000000; " +
                        "-fx-background-color: #E1BEFF; " +
                        "-fx-background-radius: 30px; " +
                        "-fx-border-radius: 30px; " +
                        "-fx-padding: 10px;"
        );

        outputLabel.setTranslateX(-100);
        content.getChildren().add(outputLabel);
        scrollPane.setContent(null);
        scrollPane.setContent(content);
        // Use Platform.runLater to ensure scroll happens after layout is complete
        Platform.runLater(() -> {
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }

    public void updatePlayerTurnBackgrounds(int playerTurn){
        if(this.playerTurn == 1){
            player1HouseBackgroundImage.setImage(new Image("Images/PlayerHouseHighlighted.png"));
            player2HouseBackgroundImage.setImage(new Image("Images/PlayerHouse.png"));
        }else {
            player2HouseBackgroundImage.setImage(new Image("Images/PlayerHouseHighlighted.png"));
            player1HouseBackgroundImage.setImage(new Image("Images/PlayerHouse.png"));
        }
    }
    public void makeReceivingGameBoardChanges(int colIndex, int playerTurn, Boolean enable){

        if(!isSinglePlayer) {
            setDisable = enable;
            System.out.println("Received request: " + playerTurn + " cllient: " + clientID);
            // Make reciving board clickable for next move
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    //setting the onMouseClicked property for each of the buttons to call z (the event handler)
                    if (setDisable) {
                        boardSpotsImages[i][j].setOnMouseClicked(z);
                    }
                }
            }
        }

        connect4Box.getChildren().remove(gridPane);

        updateBoard(colIndex, playerTurn);

        GridPane gridPaneNew = new GridPane();
        for(int col = 0; col < 7; col++){
            for(int row = 0; row < 6; row++){
                gridPaneNew.add(boardSpotsImages[col][row], col, row);
            }
        }

        gridPaneNew.setHgap(10); // Horizontal gap between columns
        gridPaneNew.setVgap(10); // Vertical gap between rows

        gridPaneNew.setTranslateY(40);
        gridPaneNew.setTranslateX(197);

        connect4Box.getChildren().add(gridPaneNew);
        gridPane = gridPaneNew;
    }

    public void makeGameBoardAdjustments(int colIndex, int playerTurn){
        makeReceivingGameBoardChanges(colIndex, playerTurn, setDisable);

        if(!isSinglePlayer) {
            // Send message to return to main scene for both clients
            Message messageMakeMove;
            this.playerTurn = playerTurn;
            System.out.println("Player: " + clientID + " makes a move");
            String currentUser = GuiClient.getLoggedInUsername();
            String otherPlayer = currentUser.equals(player1) ? player2 : player1;

            messageMakeMove = new Message(currentUser, otherPlayer, this.playerTurn, colIndex, availableRow, true);
            messageMakeMove.setType(MessageType.MAKE_MOVE);
            messageMakeMove.setEnable(true);
            clientConnection.send(messageMakeMove);

            setDisable = false;

            if (this.playerTurn == 1) {
                this.playerTurn = 2;

                player2HouseBackgroundImage.setImage(new Image("Images/PlayerHouseHighlighted.png"));
                player1HouseBackgroundImage.setImage(new Image("Images/PlayerHouse.png"));
            } else {
                this.playerTurn = 1;
                player1HouseBackgroundImage.setImage(new Image("Images/PlayerHouseHighlighted.png"));
                player2HouseBackgroundImage.setImage(new Image("Images/PlayerHouse.png"));
            }

            // Switch turns, send new request to update it
            clientConnection.send(new Message(currentUser, otherPlayer, this.playerTurn, true));

            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    //setting the onMouseClicked property for each of the buttons to call z (the event handler)
                    if (!setDisable) {
                        boardSpotsImages[i][j].setOnMouseClicked(e -> System.out.println("Cant make move"));
                    }
                }
            }
        }


    }

    // CHANGES
    public void askQuestionPopUp(){
        Platform.runLater(() -> {

            Stage popupQuestionStage = new Stage();
            popupQuestionStage.initModality(Modality.APPLICATION_MODAL);
            popupQuestionStage.initOwner(primaryStage);

            Random random = new Random();
            boolean foundQuestion = false;
            int maxAttempts = 50; // Safety limit to prevent infinite loops
            int attempts = 0;

            String displayQuestion = "";
            String displayAnswer = "";

            while (!foundQuestion && attempts < maxAttempts) {
                // Generate random row and column indices
                int row = random.nextInt(6);
                int col = random.nextInt(7);

                // Check if the question at the random position is populated
                if (message.getHardModeQuestions()[row][col] != null && !message.getHardModeAnswers()[row][col].isEmpty()) {
                    // Display the question
                    displayQuestion = message.getHardModeQuestions()[row][col];
                    System.out.println("Question: " + message.getHardModeQuestions()[row][col]);

                    // Optionally, display the answer as well
                    displayAnswer = message.getHardModeAnswers()[row][col];
                    System.out.println("Answer: " + message.getHardModeAnswers()[row][col]);
                    foundQuestion = true;
                } else {
                    // If the spot is not populated, try again
                    attempts++;
                }
            }

            if (!foundQuestion) {
                System.out.println("Could not find a populated question after " + maxAttempts + " attempts.");
            }

            Label showQuestion = new Label(displayQuestion);
            showQuestion.setStyle("-fx-font-size: 18px; -fx-text-fill: #000000");
            showQuestion.setWrapText(true);
            showQuestion.setMaxWidth(250);
            showQuestion.setTextAlignment(TextAlignment.CENTER);

            TextField answerInput = new TextField("Enter your answer here");
            answerInput.setStyle("-fx-font-size: 18px; -fx-text-fill: #000000");
            answerInput.setMaxWidth(250);

            Label correctIncorrectDisplay = new Label();
            showQuestion.setWrapText(true);
            showQuestion.setMaxWidth(250);
            showQuestion.setTextAlignment(TextAlignment.CENTER);

            Button submitButton = new Button("Submit");
            Button GiveUp = new Button("Give up?");
            String finalDisplayAnswer = displayAnswer;

            submitButton.setOnAction(event -> {
                String input = answerInput.getText();

                if (!answerInput.getText().trim().isEmpty()) {
                    if(input.equals(finalDisplayAnswer)){
                        correctIncorrectDisplay.setText("CORRECT!");
                        correctIncorrectDisplay.setStyle("-fx-font-size: 18px; -fx-text-fill: #00ff11");
                        popupQuestionStage.close();
                    }else{
                        correctIncorrectDisplay.setText("Wrong! Try Again!");
                        correctIncorrectDisplay.setStyle("-fx-font-size: 18px; -fx-text-fill: #ff0000");
                    }
                }
            });

            String finalDisplayAnswer1 = displayAnswer;
            GiveUp.setOnAction(event -> {
                correctIncorrectDisplay.setText("ANS: " + finalDisplayAnswer1);
                correctIncorrectDisplay.setStyle("-fx-font-size: 18px; -fx-text-fill: #000000");
                popupQuestionStage.close();
            });

            // Create popup layout with consistent background color
            VBox popupLayout = new VBox(10,showQuestion, answerInput, submitButton, GiveUp, correctIncorrectDisplay);
            popupLayout.setStyle("-fx-alignment: center; -fx-padding: 20; -fx-background-color: #ffffff; -fx-border-radius: 20px; -fx-background-radius: 20px; -fx-border-color: #211038");
            popupLayout.setAlignment(Pos.CENTER);

            // Remove the title bar and make window undecorated
            popupQuestionStage.initStyle(StageStyle.TRANSPARENT);
            // Make the scene background transparent so only the VBox is visible
            Scene popupScene = new Scene(popupLayout, 350, 250);
            popupScene.setFill(Color.TRANSPARENT);
            popupQuestionStage.setScene(popupScene);

            // Show the popup first (so it has dimensions), then reposition it
            popupQuestionStage.setOnShown(e -> {
                double centerX = primaryStage.getX() + (primaryStage.getWidth() - popupQuestionStage.getWidth()) / 2;
                double centerY = primaryStage.getY() + (primaryStage.getHeight() - popupQuestionStage.getHeight()) / 2;
                popupQuestionStage.setX(centerX);
                popupQuestionStage.setY(centerY);
            });

            popupQuestionStage.showAndWait();
        });
    }
    //


    public void triggerNewGame(){

        endGame = false;
        playerTurn = 1;
        updatePlayerTurnBackgrounds(playerTurn);
        finalLayout.getChildren().clear(); // Clear screen and add everything back
        gridPane.getChildren().clear();
        connect4Box.getChildren().remove(gridPane);
        GridPane newGrid = new GridPane();

        for(int col = 0; col < 7; col++){
            for(int row = 0; row < 6; row++){

                Image image = new Image("Images/EmptySlot.png"); // or use a URL or resource path
                Image highlightedImage = new Image("Images/EmptySlotHighlight.png"); // or use a URL or resource path

                boardSpotsImages[col][row].setImage(image);

                boardSpotsImages[col][row].setFitWidth(50); // resize if needed
                boardSpotsImages[col][row].setFitHeight(50);

                board[col][row].setValue(0); // 0 means empty no chips: 1 - player 1 chip, 2 - player 2 chip

                newGrid.add(boardSpotsImages[col][row], col, row);

            }
        }
        gridPane = newGrid;
        gridPane.setHgap(10); // Horizontal gap between columns
        gridPane.setVgap(10); // Vertical gap between rows

        gridPane.setTranslateY(40);
        gridPane.setTranslateX(197);

        connect4Box.getChildren().add(gridPane);


        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if(setDisable) { // setDisable = true means can make a move
                    boardSpotsImages[i][j].setOnMouseClicked(z);
                }
            }
        }

        layout.getChildren().remove(connect4Box);
        layout.getChildren().remove(gameOptions);

        layout.getChildren().add(connect4Box);
        layout.getChildren().add(gameOptions);

        finalLayout.getChildren().add(layout);

    }

    private void updateTurnPlayerDisplays(){

    }

    // Current player drops a coin down
    public void updateBoard(int colIndex, int playerTurn) {
        System.out.println("Update board " + " : " + playerTurn);
        if(endGame){
            return;
        }

        availableRow = getRowOfDroppedCoin(colIndex);

        // If no empty spot, ignore the click
        if (availableRow == -1) {
            System.out.println("Column full!");
            return;
        }

        // Set token image based on current player
        Image tokenImage = null;
        if (playerTurn == 1) {
            if(message.getGameCreatorsTokenColor().equals("Red")) {
                tokenImage = new Image("Images/RedToken.png");
            }else if(message.getGameCreatorsTokenColor().equals("Yellow")){
                tokenImage = new Image("Images/YellowToken.png");
            }
        } else {
            tokenImage = new Image("Images/YellowToken.png");
            if(message.getGameCreatorsTokenColor().equals("Yellow")) {
                tokenImage = new Image("Images/RedToken.png");
            }
        }

        // Update UI and board state
        boardSpotsImages[colIndex][availableRow].setImage(tokenImage);
        board[colIndex][availableRow].setValue(playerTurn);



        // Print board (for debugging)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                System.out.print(board[col][row].getValue());
            }
            System.out.println();
        }

        if(checkWinCondition()){
            System.out.println("Player: " + playerTurn + " WINS!");
            String winner = "0";

            if(playerTurn == 1){
                winner = player1;
            }else{
                winner = player2;
            }
            if(!isSinglePlayer) {
                String currentUser = GuiClient.getLoggedInUsername();
                String otherPlayer = currentUser.equals(player1) ? player2 : player1;

                Message messageGameStatus = new Message(currentUser, otherPlayer, winner, true, true);
                messageGameStatus.setType(MessageType.GAME_STATUS);
                clientConnection.send(messageGameStatus);
            }
            VBox wonGameMessage = finalMessageDisplay("Win");
            endGame = true;
            finalLayout.getChildren().add(wonGameMessage);

        }

        if (checkFullBoard()){
            VBox drawGameMessage = finalMessageDisplay("Draw");
            endGame = true;
            finalLayout.getChildren().add(drawGameMessage);
        }

        if(isSinglePlayer) {
            playerTurn = 2;
            aiMakesMove();
        }
    }

    public void aiMakesMove(){
        playerTurn = 2;
        ArrayList<Integer> possibleColumns = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int checkCol = i;
            int checkRow = getRowOfDroppedCoin(i); //gets the row
            if(checkRow != -1 && board[checkCol][checkRow].getValue() != 1 && board[checkCol][checkRow].getValue() == 0){
                possibleColumns.add(checkCol);
            }
        }

        // Set token image based on current player
        Image tokenImage = new Image("Images/YellowToken.png");
        if(message.getGameCreatorsTokenColor().equals("Yellow")) {
            tokenImage = new Image("Images/RedToken.png");
        }
        Random rand = new Random();

        // Pick a random index
        int randomIndex = rand.nextInt(possibleColumns.size());

        // Get the random element
        int randomValue = possibleColumns.get(randomIndex);
        availableRow = getRowOfDroppedCoin(randomValue);

        //find the next available row
        while (availableRow == -1){
            randomIndex = rand.nextInt(possibleColumns.size());
            // Get the random element
            randomValue = possibleColumns.get(randomIndex);
            availableRow = getRowOfDroppedCoin(randomValue);
        }

        System.out.println("col: " + randomValue + " row: " + availableRow);

        // Update UI and board state
        boardSpotsImages[randomValue][availableRow].setImage(tokenImage);
        board[randomValue][availableRow].setValue(2);

        // Print board (for debugging)
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                System.out.print(board[col][row].getValue());
            }
            System.out.println();
        }

        if(checkWinCondition()){
            System.out.println("Player: " + playerTurn + " WINS!");
            String winner = "";

            if (playerTurn == 1) {
                winner = player1;
            } else {
                winner = player2;
            }

//            if(!isSinglePlayer) {
//                if (playerTurn == 1) {
//                    winner = player1;
//                } else {
//                    winner = player2;
//                }
//            }else{
//
//                if (playerTurn == 1) {
//                    winner = 1;
//                } else {
//                    winner = 2;
//                }
//            }
            if(!isSinglePlayer) {
                String currentUser = GuiClient.getLoggedInUsername();
                String otherPlayer = currentUser.equals(player1) ? player2 : player1;
                Message messageGameStatus = new Message(currentUser, otherPlayer, winner, true, true);
                messageGameStatus.setType(MessageType.GAME_STATUS);
                clientConnection.send(messageGameStatus);
            }
            VBox wonGameMessage = finalMessageDisplay("Win");
            endGame = true;
            finalLayout.getChildren().add(wonGameMessage);

        }

        if (checkFullBoard()){
            VBox drawGameMessage = finalMessageDisplay("Draw");
            endGame = true;
            finalLayout.getChildren().add(drawGameMessage);
        }

        playerTurn = 1;
    }

    //Check current columns directions
    public void checkDirections(int checkCol, int checkRow){
        //up and down

        //check for existing chips below
        for(int i = checkRow; i >= 0; i--){
            if(board[checkRow][i].getValue() != 1 && board[checkRow][i].getValue() == 0){//can drop

            }
        }

        //check up
        for(int i = checkRow; i >= 0; i--){
            if(board[checkRow][i].getValue() != 1 && board[checkRow][i].getValue() == 0){//can drop

            }
        }
    }

    public int getRowOfDroppedCoin(int colIndex){
        for (int row = 5; row >= 0; row--) {
            if (board[colIndex][row].getValue() == 0) {
                availableRow = row;
                return availableRow;
            }
        }
        return -1;
    }


    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    private VBox finalMessageDisplay(String type){
        Label wonGameText = new Label("PLAYER " + playerTurn + " WINS!!");
        if(type.equals("Draw")) {
            wonGameText.setText("DRAW!!");
        }
        wonGameText.setStyle("-fx-font-size: 20px; -fx-text-fill: #46068A");

        Label gameOverText = new Label("GAME OVER!!!");
        gameOverText.setStyle("-fx-font-size: 18px; -fx-text-fill: #46068A");

        Button newGame = new Button("New Game");
        newGame.setStyle("-fx-font-size: 14px; -fx-background-color: #8D7CF6; -fx-text-fill: #ffffff; -fx-border-radius: 30px");
        newGame.setOnMouseClicked(event -> {
            String currentUser = GuiClient.getLoggedInUsername();
            String otherPlayer = currentUser.equals(player1) ? player2 : player1;

            Message message1 = new Message(currentUser, otherPlayer,1);
            System.out.println("Player1: " + player1 + " p2: " + player2);
            if(clientID != player1){
                setDisable = false; // if its false it doesn't allow move
            }else{
                setDisable = true;// if its true it allows move
                // setDisable = true means can make a move
            }
            message1.setType(MessageType.RESET_GAME);
            clientConnection.send(message1);
            triggerNewGame();
        });


        Button logOut = new Button("Log Out");
        logOut.setStyle("-fx-font-size: 14px; -fx-background-color: rgba(141,124,246,0); " +
                "-fx-text-fill: #8D7CF6; -fx-border-color: #8D7CF6; -fx-border-radius: 20px");

        logOut.setOnMouseClicked(event -> {
//             Exit logo clicked

            System.out.println("Exit clicked by: " + GuiClient.getLoggedInUsername());

            // Create exit message with proper sender and recipient info
            String currentUser = GuiClient.getLoggedInUsername();
            String otherPlayer = currentUser.equals(player1) ? player2 : player1;

            Message exitMessage = new Message(MessageType.EXIT_GAME);
            exitMessage.setSenderUsername(currentUser);
            exitMessage.setRecipientUsername(otherPlayer);

            System.out.println("Sending exit message from " + currentUser + " to " + otherPlayer);
            if(!isSinglePlayer){
                // Send the message
                clientConnection.send(exitMessage);
            }
//            // Return to main scene
            DashboardScreen.show(primaryStage);
        });

        HBox buttonLayout = new HBox(newGame, logOut);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.setSpacing(10);

        VBox wonGameMessage = new VBox(wonGameText, gameOverText, buttonLayout);
        wonGameMessage.setStyle("-fx-background-color: rgb(255,255,255); -fx-border-color: #211038;-fx-background-radius: 50px; -fx-border-radius: 50px");
        wonGameMessage.setAlignment(Pos.CENTER);
        wonGameMessage.setSpacing(10);
        wonGameMessage.setMaxHeight(150); // Set max height to 200 pixels
        wonGameMessage.setMaxWidth(300);  // Set max width to 300 pixels

        return wonGameMessage;
    }

    private Boolean checkFullBoard(){
        // Iterate through board and check if it is full
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if(board[col][row].getValue() == 0){
                    return false;
                }
            }
        }

        return true;
    }


    // Function checks for win conditions by checking all 4 directions
    private Boolean checkWinCondition(){
        //Check up-down win
        int consecutiveMatchesUpDown = 0;

        for(int currentCol = 0; currentCol < 7; currentCol++) {
            for (int row = 5; row >= 1; row--) {

                if (board[currentCol][row].getValue() != 0 &&
                        row - 1 >= 0 && row - 2 >= 0 && row - 3 >= 0 &&
                        board[currentCol][row].getValue() == board[currentCol][row - 1].getValue()
                        && board[currentCol][row].getValue() == board[currentCol][row - 2].getValue()
                        && board[currentCol][row].getValue() == board[currentCol][row - 3].getValue()) {
                    System.out.println("Win Up down for: " + currentCol + " r: " + row);
                    return true;
                }
            }
        }


        // Check right-left win
        int consecutiveMatchesRightLeft = 0;
        for(int currentRow = 0; currentRow < 6; currentRow++) {

            for (int col = 0; col < 7; col++) {
                if (board[col][currentRow].getValue() != 0 &&
                        col + 1 < 7 && col + 2 < 7 && col + 3 < 7 &&
                        board[col][currentRow].getValue() == board[col + 1][currentRow].getValue()
                        && board[col][currentRow].getValue() == board[col + 2][currentRow].getValue()
                        && board[col][currentRow].getValue() == board[col + 3][currentRow].getValue()) {
                    System.out.println("Win left right for: " + col + " r: " + currentRow);
                    return true;
                }
            }

        }

        //Check diagonal left to right win
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int current = board[col][row].getValue();
                if (current != 0 &&
                        current == board[col + 1][row + 1].getValue() &&
                        current == board[col + 2][row + 2].getValue() &&
                        current == board[col + 3][row + 3].getValue()) {
                    System.out.println("Win left to right win for: " + col + " r: " + row);
                    return true;
                }
            }
        }

        // Check right to left win
        for (int row = 3; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                int current = board[col][row].getValue();
                if (current != 0 &&
                        current == board[col + 1][row - 1].getValue() &&
                        current == board[col + 2][row - 2].getValue() &&
                        current == board[col + 3][row - 3].getValue()) {
                    System.out.println("Win right to left for: " + col + " r: " + row);
                    return true;
                }
            }
        }

        return false;
    }

    public void updateScene(int playerTurn){
        this.playerTurn = playerTurn;

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: lightgreen;" +
                "-fx-font-family: 'serif';");

        Label titleLabel = new Label("Client Detail View");
        titleLabel.setFont(Font.font("Serif", FontWeight.BOLD, 24));

        Label clientLabel = new Label("You are Client #" + clientID);
        clientLabel.setFont(Font.font("Serif", FontWeight.NORMAL, 16));

        Label selectedLabel = new Label("Viewing Client #" + selectedClientID);
        selectedLabel.setFont(Font.font("Serif", FontWeight.BOLD, 18));

        Button changeTurn = new Button("Change turn");
        changeTurn.setOnAction(e -> {
            if(this.playerTurn == 1){
                this.playerTurn = 2;
            }else {
                this.playerTurn = 1;
            }
            // Send message to return to main scene for both clients
//            clientConnection.send(new Message(clientID, selectedClientID,  this.playerTurn, true));
//            updateScene(this.playerTurn);

        });
        Label turn = new Label("It's player: " + playerTurn + "'s turn");

        Button backButton = new Button("Back to Main");
        backButton.setOnAction(e -> {
            System.out.println("ClientID: " + clientID + " selectedClientID: " + selectedClientID);

            // Send message to return to main scene for both clients
//            Message message1 = new Message(clientID, selectedClientID,true);
//            message1.setType(Message.MessageType.EXIT_GAME);


//            clientConnection.send(message1);

            // Return to main scene
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Client #" + clientID);
        });

        root.getChildren().addAll(titleLabel, clientLabel, selectedLabel, changeTurn, turn, backButton);

        scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
    }


    public Scene getScene() {
        return scene;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}