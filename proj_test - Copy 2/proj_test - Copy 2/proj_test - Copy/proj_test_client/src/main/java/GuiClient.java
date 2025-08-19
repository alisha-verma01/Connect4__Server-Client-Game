import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiClient {

    public static Client clientConnection;
    private static String loggedInUsername;

    private static ArrayList<String> inboxRequests = new ArrayList<>();
    private static ArrayList<String> friendsList = new ArrayList<>();
    private static ArrayList<String> allUsernames = new ArrayList<>();

    private static Scene1 scene1;

    public static Client startConnection() {
        clientConnection = new Client(data -> {
            Platform.runLater(() -> {
                switch (data.getType()) {
                    // CHANGES
                    case VIEW_LEADERBOARD:
                        System.out.println("Here in gui client for leaderboard");
                        VBox leaderBoard = new VBox();
                        ScrollPane leaderBoardScroll = new ScrollPane();
                        leaderBoard.getChildren().clear();
                        leaderBoardScroll.setContent(null);

                        System.out.println("b: " + data.getLeaderBoard());

                        List<Object[]> leaderBoardInp = data.getLeaderBoard();
                        for (Object[] entry : leaderBoardInp) {
                            String playerID = (String) entry[0];  // Cast to String
                            int score = (Integer) entry[1];       // Cast to Integer

                            // Use this data for your popup

                            Label userID = new Label("Player: " + playerID);
                            userID.setStyle("-fx-text-fill: #ffffff");
                            Label rank = new Label(" with a score of " + score);
                            rank.setStyle("-fx-text-fill: #ffffff");

                            HBox user = new HBox(userID, rank);
                            user.setSpacing(20);
                            user.setAlignment(Pos.CENTER);
                            user.setMaxWidth(300);
                            user.setMinWidth(300);
                            user.setMaxHeight(50);
                            user.setMinHeight(50);
                            user.setStyle("-fx-border-radius: 20px; " +
                                    "-fx-background-radius: 20px; -fx-background-color: #8D7CF6; -fx-padding: 10px");

                            leaderBoard.getChildren().add(user);
                            leaderBoard.setSpacing(10);
                            leaderBoardScroll.setContent(leaderBoard);
                            // Hide scrollbars
                            leaderBoardScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                            leaderBoardScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                            // Set the background color for ScrollPane and its viewport
                            leaderBoardScroll.setStyle("-fx-background: #630D5F; -fx-background-color: #630D5F; -fx-border-color: #630D5F");


                            // Apply style to the viewport
                            leaderBoardScroll.lookupAll(".viewport").forEach(node -> {
                                node.setStyle("-fx-background-color: #630D5F;");
                            });

                            System.out.println(playerID + ": " + score);
                        }

                        DashboardScreen.setAllLeaderboardPlayers(leaderBoardScroll);
                        break;
                    //
                    case RESET_GAME:
                        System.out.println("Here in gui client for reset");
                        scene1.setPlayerTurn(data.getPlayerTurn());
                        scene1.triggerNewGame();
                        break;
                    case TURN_CHANGE:
                        System.out.println("Here in gui client");
                        scene1.setPlayerTurn(data.getPlayerTurn());
                        scene1.updatePlayerTurnBackgrounds(data.getPlayerTurn());
                        break;
                    case MAKE_MOVE:
                        System.out.println("Here in gui client for make move: " + data.getCol() + " : " + data.getRow());
                        scene1.setPlayerTurn(data.getPlayerTurn());
                        scene1.makeReceivingGameBoardChanges(data.getCol(), data.getPlayerTurn(), data.getEnable());
                        break;
                    case MESSAGE:
                        System.out.println("MESSAGE inp");
                        scene1.addReceivedMessageToContent(data.getMessage());
                        break;
                    case EXIT_GAME:
                        System.out.println("EXIT_GAME change");
                        if (scene1 != null) {
                            System.out.println("In here");
                            DashboardScreen.show(scene1.getPrimaryStage());
                        }
                        break;
                    case SCENE_CHANGE:
                        //CHANGES
                        System.out.println("Scene change");
                        scene1 = new Scene1(DashboardScreen.getDashboardStage(), GuiClient.clientConnection,
                                data, data.getSenderUsername(), data.getRecipientUsername(), 1, data.getSelectedTheme());
                        DashboardScreen.getDashboardStage().setScene(scene1.getScene());
                        //
                        break;
                    case NEWUSER:
                        System.out.println("in new user");
                        break;
                    case DISCONNECT:
                        System.out.println("in disconnect");
                        break;
                    case GET_ALL_PLAYERS:
                        setAllUsernames(data.getAllUsernames());
                        System.out.println("Get all users in GUI Client: " + getAllUsernames());
                        break;
                    case FRIEND_REQUEST:
                        if (!inboxRequests.contains(data.getSenderUsername())) {
                            inboxRequests.add(data.getSenderUsername());
                            DashboardScreen.updateNotificationBadge();
                        }
                        break;
                    case FRIEND_ACCEPTED:
                        System.out.println("Friend request accepted by: " + data.getSenderUsername());
                        if (!friendsList.contains(data.getSenderUsername())) {
                            friendsList.add(data.getSenderUsername());
                        }
                        break;
                    case FRIEND_REMOVED:
                        System.out.println("Friend removed: " + data.getSenderUsername());
                        friendsList.remove(data.getSenderUsername());
                        break;
                    case LOGIN:
                        if (data.isSuccess()) {
                            System.out.println("Login successful on server!");
                            friendsList = data.getFriendsList();
                            inboxRequests = data.getInboxList();
                            DashboardScreen.updateNotificationBadge();
                            requestAllUsernames();
                        } else {
                            System.out.println("Login failed on server!");
                        }
                        break;
                    default:
                        System.out.println("Received message of type: " + data.getType());
                }
            });
        });
        clientConnection.start();
        return clientConnection;
    }

    public static void requestAllUsernames() {
        if (clientConnection != null) {
            Message request = new Message(MessageType.GET_ALL_PLAYERS);
            request.setSenderUsername(loggedInUsername);
            sendMessage(request);
            System.out.println("Sent request for all usernames");
        } else {
            System.err.println("Client not connected!");
        }
    }

    public static void sendMessage(Message message) {
        clientConnection.send(message);
    }

    public static ArrayList<String> getAllUsernames() {
        return allUsernames;
    }

    public static void setAllUsernames(ArrayList<String> allUsernames) {
        GuiClient.allUsernames = allUsernames;
    }

    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static ArrayList<String> getInboxRequests() {
        return inboxRequests;
    }

    public static void acceptFriendRequest(String sender) {
        Message acceptMsg = new Message(MessageType.FRIEND_ACCEPTED);
        acceptMsg.setSenderUsername(loggedInUsername);
        acceptMsg.setRecipientUsername(sender);
        sendMessage(acceptMsg);

        friendsList.add(sender);
        inboxRequests.remove(sender);
    }

    public static void deleteFriendRequest(String sender) {
        inboxRequests.remove(sender);
        DashboardScreen.updateNotificationBadge();
    }

    public static ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public static void removeFriend(String friendUsername) {
        Message removeMsg = new Message(MessageType.REMOVE_FRIEND);
        removeMsg.setSenderUsername(loggedInUsername);
        removeMsg.setRecipientUsername(friendUsername);
        sendMessage(removeMsg);

        friendsList.remove(friendUsername);
    }
}
