import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private String senderUsername;
    private String recipientUsername;
    private String selectedClient;
    private String player1;
    private String player2;
    private Integer playerTurn;
    Integer col;
    Integer row;
    Boolean enable;

    private String messageContent; // Optional message text
    private boolean success;        // For things like login success/failure

    private ArrayList<String> friendsList;
    private ArrayList<String> inboxList;
    private ArrayList<String> allUsernames;
    String selectedTheme;
    String gameStatus;
    String createdGamesGameMode;
    String message;
    String gameCreatorsTokenColor;
    private List<Object[]> leaderBoard;

    //CHANGES
    private String[][] hardModeQuestions;
    private String[][] hardModeAnswers;
    //


    // Constructors
    public Message(MessageType type) {
        this.type = type;
    }

    // Here for leaderboard
    public Message(String sender){
        this.senderUsername = sender;
        this.type = MessageType.VIEW_LEADERBOARD;
    }

    // Constructor for reset game
    public Message(String sender, String recipient, int playerTurn) {
        this.senderUsername = sender;
        this.recipientUsername = recipient;
        this.type = MessageType.RESET_GAME;
        this.playerTurn = playerTurn;
    }

    public Message(String sender, String recipient, String player, Boolean setGameStatus, boolean status) { // player is the id of the player who won
        this.senderUsername = sender;
        this.recipientUsername = recipient;
        this.type = MessageType.GAME_STATUS;
        this.gameStatus = player;
    }

    public Message(String player1, String player2, int playerTurn, boolean changeTurn) {
        this.senderUsername = player1;
        this.recipientUsername = player2;
        this.playerTurn = playerTurn;
        this.type = MessageType.TURN_CHANGE;
//        this.enable = changeTurn;
    }

    public Message(String sender, String recipient, Integer turn, Integer col, Integer row, Boolean isMakeMove) {
        this.senderUsername = sender;
        this.recipientUsername = recipient;
        this.type = MessageType.MAKE_MOVE;
        this.col = col;
        this.row = row;
        this.playerTurn = turn;
        this.enable = isMakeMove;
    }

    public Message(String player1, String player2, boolean isExitGame) {
        this.senderUsername = player1;
        this.recipientUsername = player2;
        this.type = MessageType.EXIT_GAME;
    }

    // Constructor for text messages
    public Message(String sender, String recipient, String message) {
        this.senderUsername = sender;
        this.recipientUsername = recipient;
        this.message = message;
        this.type = MessageType.MESSAGE;
    }

    // CHANGES
    // Constructor for scene change and turn change messages
    public Message(String sender, String recipient,
                   String selectedClient, Integer turn,
                   boolean isSceneChange, String selectedTheme,
                   String[][] hardModeQues, String[][] hardModeAns,
                   String createdGamesGameMode, String gameCreatorsTokenColor) {
        this.senderUsername = sender;
        this.recipientUsername = recipient;

        this.player1 = sender;
        this.player2 = recipient;
        System.out.println("P1: " + sender + " P2: " + recipient);

        this.selectedClient = selectedClient;

        this.type = MessageType.SCENE_CHANGE;
        this.playerTurn = turn;
        this.gameCreatorsTokenColor = gameCreatorsTokenColor;

        this.selectedTheme = selectedTheme;
        this.createdGamesGameMode = createdGamesGameMode;

        // CHANGES
        setHardModeQuestions(hardModeQues);
        setHardModeAnswers(hardModeAns);
        //

    }
    //

    public Message(MessageType type, String sender) {
        this.type = type;
        this.senderUsername = sender;
    }

    public Message(MessageType type, String sender, String recipient) {
        this.type = type;
        this.senderUsername = sender;
        this.recipientUsername = recipient;
    }

    public Message(MessageType type, String sender, String recipient, String content) {
        this.type = type;
        this.senderUsername = sender;
        this.recipientUsername = recipient;
        this.messageContent = content;
    }


    // Getters and Setters


    public String getGameCreatorsTokenColor() {
        return gameCreatorsTokenColor;
    }

    public void setGameCreatorsTokenColor(String gameCreatorsTokenColor) {
        this.gameCreatorsTokenColor = gameCreatorsTokenColor;
    }

    public String getCreatedGamesGameMode() {
        return createdGamesGameMode;
    }

    public void setCreatedGamesGameMode(String createdGamesGameMode) {
        this.createdGamesGameMode = createdGamesGameMode;
    }

    public String[][] getHardModeQuestions() {
        return hardModeQuestions;
    }

    public void setHardModeQuestions(String[][] hardModeQuestions) {
        this.hardModeQuestions = hardModeQuestions;
    }

    public String[][] getHardModeAnswers() {
        return hardModeAnswers;
    }

    public void setHardModeAnswers(String[][] hardModeAnswers) {
        this.hardModeAnswers = hardModeAnswers;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public List<Object[]> getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(List<Object[]> leaderBoard) {
        this.leaderBoard = leaderBoard;
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(String selectedClient) {
        this.selectedClient = selectedClient;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public Integer getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Integer playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(String selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public ArrayList<String> getAllUsernames() {
        return allUsernames;
    }

    public void setAllUsernames(ArrayList<String> allUsernames) {
        this.allUsernames = allUsernames;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }

    public ArrayList<String> getInboxList() {
        return inboxList;
    }

    public void setInboxList(ArrayList<String> inboxList) {
        this.inboxList = inboxList;
    }
}

