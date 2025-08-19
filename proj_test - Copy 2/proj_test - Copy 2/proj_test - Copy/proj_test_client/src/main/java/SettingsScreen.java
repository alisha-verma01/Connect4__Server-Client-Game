import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsScreen {

    private static String selectedTheme = "Default"; // Default if none selected
    private static String selectedDifficulty = "Easy"; // Default is Easy

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2e005f;");

        Label title = new Label("Settings");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(40, 0, 10, 0));
        root.setTop(title);

        GridPane grid = new GridPane();
        grid.setHgap(60);
        grid.setVgap(50);
        grid.setPadding(new Insets(30, 60, 40, 60));
        grid.setAlignment(Pos.TOP_CENTER);

        VBox[] themeContainer = new VBox[1];
        VBox[] avatarContainer = new VBox[1];
        VBox[] tokenContainer = new VBox[1];
        VBox[] gameContainer = new VBox[1];

        themeContainer[0] = buildOption("Choose theme", "select_theme.png", e -> showThemePopup(stage, themeContainer[0]));
        avatarContainer[0] = buildOption("Choose avatar", "select_avatar.png", e -> showAvatarPopup(stage, avatarContainer[0]));
        tokenContainer[0] = buildOption("Choose token color", "select_token_color.png", e -> showTokenPopup(stage, tokenContainer[0]));
        gameContainer[0] = buildOption("Create or join new game", "select_game.png", e -> showGameModePopup(stage, gameContainer[0]));

        grid.add(themeContainer[0], 0, 0);
        grid.add(avatarContainer[0], 1, 0);
        grid.add(tokenContainer[0], 0, 1);
        grid.add(gameContainer[0], 1, 1);
        root.setCenter(grid);

        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        nav.setPadding(new Insets(30));

        StackPane back = createNavButton("← back", e -> {
            try {
                new LoginScreen().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        StackPane next = createNavButton("next →", e -> {
            DashboardScreen.show(stage);
        });


        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);
        nav.getChildren().addAll(back, space, next);

        root.setBottom(nav);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Connect Four - Settings");
        stage.show();
    }

    private static VBox buildOption(String labelText, String iconFileName, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Label label = new Label(labelText);
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 21));

        ImageView icon = new ImageView(new Image(SettingsScreen.class.getResourceAsStream("/" + iconFileName)));
        icon.setFitWidth(35);
        icon.setFitHeight(35);

        Button button = new Button("select one");
        button.setCursor(Cursor.HAND);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 19));
        button.setTextFill(Color.WHITE);
        button.setPrefSize(550, 100);
        button.setStyle("""
            -fx-background-color: #a879e7;
            -fx-background-radius: 20;
        """);

        if (handler != null) {
            button.setOnAction(handler);
        }

        HBox hBox = new HBox(10, icon, button);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(6));
        hBox.setStyle("""
            -fx-background-color: #7037bb;
            -fx-background-radius: 28;
        """);

        VBox container = new VBox(10, label, hBox);
        container.setAlignment(Pos.CENTER_LEFT);
        return container;
    }

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

    private static void showThemePopup(Stage owner, VBox optionBox) {
        showPopup(owner, optionBox, "Select Theme", new String[]{
                "space_theme.png", "mountain_theme.png", "mario_theme.png",
                "light_theme.png", "dark_theme.png", "purple_theme.png"
        });
    }

    private static void showAvatarPopup(Stage owner, VBox optionBox) {
        showPopup(owner, optionBox, "Select Avatar", new String[]{
                "avatar1.png", "avatar2.png", "avatar3.png",
                "avatar4.png", "avatar5.png", "avatar6.png"
        });
    }

    private static void showTokenPopup(Stage owner, VBox optionBox) {
        showPopup(owner, optionBox, "Select Token Color", new String[]{
                "red_token.png", "yellow_token.png", "blue_token.png",
                "green_token.png", "orange_token.png", "purple_token.png"
        });
    }


    private static void showPopup(Stage owner, VBox optionBox, String title, String[] imageFiles) {
        Label label = (Label) optionBox.getChildren().get(0);
        HBox buttonContainer = (HBox) optionBox.getChildren().get(1);

        // Dim the label and button
        label.setOpacity(0.4);
        buttonContainer.setOpacity(0.4);

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle(title);

        GridPane popupGrid = new GridPane();
        popupGrid.setHgap(20);
        popupGrid.setVgap(20);
        popupGrid.setPadding(new Insets(10, 20, 0, 20));
        popupGrid.setAlignment(Pos.CENTER);
        popupGrid.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        int index = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                ImageView icon = new ImageView(new Image(SettingsScreen.class.getResourceAsStream("/" + imageFiles[index])));

                if (title.equals("Select Avatar")) {
                    icon.setFitWidth(100);
                    icon.setFitHeight(100);
                } else if (imageFiles[index].equals("mountain_theme.png")) {
                    icon.setFitWidth(120);
                    icon.setFitHeight(120);
                } else if (imageFiles[index].equals("mario_theme.png")) {
                    icon.setFitWidth(95);
                    icon.setFitHeight(95);
                } else {
                    icon.setFitWidth(70);
                    icon.setFitHeight(70);
                }

                StackPane iconWrapper = new StackPane(icon);
                iconWrapper.setPrefSize(150, 150);
                iconWrapper.setAlignment(Pos.CENTER);

                Button imageBtn = new Button();
                imageBtn.setGraphic(iconWrapper);
                imageBtn.setStyle("-fx-background-color: transparent;");
                imageBtn.setCursor(Cursor.HAND);

                popupGrid.add(imageBtn, col, row);
                index++;

                if (title.equals("Select Theme")) {
                    final String selectedImage = imageFiles[index - 1]; // Because you incremented index already

                    imageBtn.setOnAction(e -> {
                        popup.close();

                        if (selectedImage.equals("space_theme.png")) {
                            selectedTheme = "Space";
                        } else if (selectedImage.equals("mountain_theme.png")) {
                            selectedTheme = "Nature";
                        } else if (selectedImage.equals("mario_theme.png")) {
                            selectedTheme = "Mario";
                        } else if (selectedImage.equals("light_theme.png")) {
                            selectedTheme = "Day";
                        } else if (selectedImage.equals("dark_theme.png")) {
                            selectedTheme = "Night";
                        } else if (selectedImage.equals("purple_theme.png")) {
                            selectedTheme = "Default";
                        }

                        System.out.println("Selected theme: " + selectedTheme);
                    });
                }

            }
        }

        StackPane popupRoot = new StackPane(popupGrid);
        popupRoot.setPadding(new Insets(20));
        Scene scene = new Scene(popupRoot, 440, 320);
        popup.setScene(scene);
        popup.show();

        // Center the popup after showing
        popup.setX(owner.getX() + owner.getWidth() / 2 - popup.getWidth() / 2);
        popup.setY(owner.getY() + owner.getHeight() / 2 - popup.getHeight() / 2);

        // When popup closes, restore opacity
        popup.setOnHidden(e -> {
            label.setOpacity(1.0);
            buttonContainer.setOpacity(1.0);
        });
    }

    private static void showGameModePopup(Stage owner, VBox optionBox) {
        Label label = (Label) optionBox.getChildren().get(0);
        HBox buttonContainer = (HBox) optionBox.getChildren().get(1);

        label.setOpacity(0.4);
        buttonContainer.setOpacity(0.4);

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Create or Join Game");



        Button createBtn = new Button("create");
        createBtn.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        createBtn.setTextFill(Color.web("#2e005f"));
        createBtn.setPrefSize(220, 180);
        createBtn.setCursor(Cursor.HAND);
        createBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """);
        createBtn.setOnMouseEntered(e -> createBtn.setStyle("""
            -fx-background-color: rgba(112,55,187,0.1);
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 3);
        """));
        createBtn.setOnMouseExited(e -> createBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """));

        createBtn.setOnAction(e -> {
            popup.close();
            showDifficultyPopup(owner); // This method will open the easy/hard selection
        });

        Button joinBtn = new Button("join");
        joinBtn.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        joinBtn.setTextFill(Color.web("#2e005f"));
        joinBtn.setPrefSize(220, 180);
        joinBtn.setCursor(Cursor.HAND);
        joinBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """);
        joinBtn.setOnMouseEntered(e -> joinBtn.setStyle("""
            -fx-background-color: rgba(112,55,187,0.1);
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 3);
        """));
        joinBtn.setOnMouseExited(e -> joinBtn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """));


        joinBtn.setOnAction(e -> {
            popup.close();
            DashboardScreen.show(owner);
        });






        for (Button btn : new Button[]{createBtn, joinBtn}) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            btn.setTextFill(Color.web("#2e005f"));
            btn.setPrefSize(220, 180);
            btn.setCursor(Cursor.HAND);
            btn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """);

            btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: rgba(112,55,187,0.1);
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.3, 0, 3);
        """));
            btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-background-color: transparent;
            -fx-border-color: transparent;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.2, 0, 2);
        """));
        }

        Region divider = new Region();
        divider.setPrefWidth(1);
        divider.setStyle("-fx-background-color: #7037bb;");

        HBox popupBox = new HBox(createBtn, divider, joinBtn);
        popupBox.setAlignment(Pos.CENTER);
        popupBox.setPadding(new Insets(30));
        popupBox.setSpacing(0);
        popupBox.setStyle("""
        -fx-background-color: linear-gradient(to bottom right, #f3eaff, #ffffff);
        -fx-background-radius: 25;
        -fx-border-radius: 25;
        -fx-effect: dropshadow(gaussian, rgba(112, 55, 187, 0.3), 20, 0.5, 0, 5);
    """);

        StackPane popupRoot = new StackPane(popupBox);
        popupRoot.setPadding(new Insets(20));
        Scene scene = new Scene(popupRoot, 480, 280);
        popup.setScene(scene);
        popup.show();

        popup.setX(owner.getX() + owner.getWidth() / 2 - popup.getWidth() / 2);
        popup.setY(owner.getY() + owner.getHeight() / 2 - popup.getHeight() / 2);

        popup.setOnHidden(e -> {
            label.setOpacity(1.0);
            buttonContainer.setOpacity(1.0);
        });
    }


    private static void showDifficultyPopup(Stage owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Select Difficulty");

        Button easyBtn = new Button("easy");
        Button hardBtn = new Button("hard");

        for (Button btn : new Button[]{easyBtn, hardBtn}) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            btn.setTextFill(Color.web("#2e005f"));
            btn.setPrefSize(220, 180);
            btn.setCursor(Cursor.HAND);
            btn.setStyle("""
                -fx-background-color: #ede5ff;
                -fx-border-color: transparent;
                -fx-background-radius: 25;
            """);

            btn.setOnMouseEntered(e -> btn.setStyle("""
                -fx-background-color: #d6c6ff;
                -fx-border-color: transparent;
                -fx-background-radius: 25;
            """));

            btn.setOnMouseExited(e -> btn.setStyle("""
                -fx-background-color: #ede5ff;
                -fx-border-color: transparent;
                -fx-background-radius: 25;
            """));
        }

        // === LOGIC BINDING ===
        easyBtn.setOnAction(e -> {
            selectedDifficulty = "Easy"; // record choice
            popup.close();
            DashboardScreen.show(owner);
        });


        hardBtn.setOnAction(e -> {
            selectedDifficulty = "Hard"; // choice
            popup.close();
            showHardModeGridPopup(owner);
        });



        // Divider
        Region divider = new Region();
        divider.setPrefWidth(1);
        divider.setStyle("-fx-background-color: #7037bb;");

        HBox popupBox = new HBox(easyBtn, divider, hardBtn);
        popupBox.setAlignment(Pos.CENTER);
        popupBox.setPadding(new Insets(30));
        popupBox.setSpacing(0);
        popupBox.setStyle("""
            -fx-background-color: linear-gradient(to bottom right, #fff4ff, #e5d1ff);
            -fx-background-radius: 25;
            -fx-border-radius: 25;
            -fx-effect: dropshadow(gaussian, rgba(112, 55, 187, 0.3), 20, 0.5, 0, 5);
        """);

        StackPane popupRoot = new StackPane(popupBox);
        popupRoot.setPadding(new Insets(20));
        Scene scene = new Scene(popupRoot, 480, 280);
        popup.setScene(scene);
        popup.show();

        popup.setX(owner.getX() + owner.getWidth() / 2 - popup.getWidth() / 2);
        popup.setY(owner.getY() + owner.getHeight() / 2 - popup.getHeight() / 2);
    }



    private static void showHardModeGridPopup(Stage owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Enter Questions for Level Hard");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.CENTER);

        // Store the questions
        String[][] questions = QuestionBank.hardModeQuestions;


        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                int r = row, c = col;
                Button box = new Button();
                box.setMinSize(50, 50);
                box.setStyle("""
                    -fx-background-color: #fdf7ff;
                    -fx-border-color: #7037bb;
                """);
                box.setOnMouseEntered(e -> box.setStyle("""
                    -fx-background-color: #e5d1ff;
                    -fx-border-color: #7037bb;
                """));
                box.setOnMouseExited(e -> {
                    if (box.getText().equals("✔")) {
                        box.setStyle("-fx-background-color: #7037bb; -fx-text-fill: white;");
                    } else {
                        box.setStyle("-fx-background-color: #fdf7ff; -fx-border-color: #7037bb;");
                    }
                });

                box.setOnAction(e -> showQuestionInputPopup(owner, r, c, box, QuestionBank.hardModeQuestions));
                grid.add(box, col, row);
            }
        }

        VBox content = new VBox(30, grid); // extra spacing from title+save
        content.setAlignment(Pos.CENTER);

        content.setAlignment(Pos.CENTER);

        BorderPane popupRoot = new BorderPane();
        popupRoot.setStyle("-fx-background-color: #2e005f;");
        popupRoot.setCenter(content);

        Button saveBtn = new Button("save");
        saveBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        saveBtn.setStyle("-fx-background-color: #a879e7; -fx-text-fill: white; -fx-background-radius: 20;");
        saveBtn.setCursor(Cursor.HAND);
        saveBtn.setOnAction(e -> {
            // Placeholder for save logic
            System.out.println("Questions saved:");
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    if (questions[i][j] != null)
                        System.out.printf("Q[%d][%d]: %s%n", i, j, questions[i][j]);
                }
            }
        });



        BorderPane.setMargin(content, new Insets(20));

        Label title = new Label("enter questions\nfor level hard");
        // Centered title + top-right save button layout
        StackPane titleAndSave = new StackPane();
        titleAndSave.setPadding(new Insets(20, 20, 0, 20)); // top, right, bottom, left

        StackPane.setAlignment(title, Pos.CENTER);

        saveBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        saveBtn.setStyle("-fx-background-color: #a879e7; -fx-text-fill: white; -fx-background-radius: 20;");
        saveBtn.setCursor(Cursor.HAND);
        StackPane.setAlignment(saveBtn, Pos.TOP_RIGHT);

        titleAndSave.getChildren().addAll(title, saveBtn);
        popupRoot.setTop(titleAndSave);



        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);



        BorderPane.setAlignment(title, Pos.CENTER);

        Scene scene = new Scene(popupRoot, 600, 500);
        popup.setScene(scene);
        popup.show();
    }

    private static void showQuestionInputPopup(Stage owner, int row, int col, Button cell, String[][] questions) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);
        popup.setTitle("Question and Answer for Cell " + (row * 7 + col + 1));

        Label indexLabel = new Label((row * 7 + col + 1) + ".");
        indexLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        indexLabel.setTextFill(Color.web("#2e005f"));

        Label questionPrompt = new Label("Enter question " + (row * 7 + col + 1) + ":");
        questionPrompt.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        questionPrompt.setTextFill(Color.web("#2e005f"));

        javafx.scene.control.TextField questionInput = new javafx.scene.control.TextField();
        questionInput.setText(questions[row][col] != null ? questions[row][col] : "");
        questionInput.setPrefWidth(300);
        questionInput.setStyle("-fx-font-size: 16px;");

        Label answerPrompt = new Label("Enter answer:");
        answerPrompt.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        answerPrompt.setTextFill(Color.web("#2e005f"));

        javafx.scene.control.TextField answerInput = new javafx.scene.control.TextField();
        answerInput.setText(QuestionBank.hardModeAnswers[row][col] != null ? QuestionBank.hardModeAnswers[row][col] : "");
        answerInput.setPrefWidth(300);
        answerInput.setStyle("-fx-font-size: 16px;");

        Button save = new Button("save");
        save.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        save.setCursor(Cursor.HAND);
        save.setStyle("-fx-background-color: #a879e7; -fx-background-radius: 20;");
        save.setTextFill(Color.WHITE);
        save.setOnAction(e -> {
            questions[row][col] = questionInput.getText();
            QuestionBank.hardModeAnswers[row][col] = answerInput.getText();  // ✅ Save the answer
            if (!questionInput.getText().trim().isEmpty() && !answerInput.getText().trim().isEmpty()) {
                cell.setText("✔");
                cell.setStyle("-fx-background-color: #7037bb; -fx-text-fill: white;");
            } else {
                cell.setText("");
                cell.setStyle("-fx-background-color: #fdf7ff; -fx-border-color: #7037bb;");
            }
            popup.close();
        });

        VBox content = new VBox(20, indexLabel, questionPrompt, questionInput, answerPrompt, answerInput, save);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #fdf7ff; -fx-background-radius: 20;");

        Scene scene = new Scene(content, 500, 350);
        popup.setScene(scene);
        popup.show();
    }

    public static String getSelectedTheme() {
        return selectedTheme;
    }


    public static String getSelectedDifficulty() {
        return selectedDifficulty;
    }



}