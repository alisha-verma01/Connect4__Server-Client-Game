import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;
// Server class for client handling
public class Server {

    private List<ClientThread> clients = new ArrayList<>();
    private Map<String, ClientThread> usernameToClient = new HashMap<>();
    private ArrayList<String> usernames = new ArrayList<>();
    private Map<String, List<String>> friendsDatabase = new HashMap<>();
    private Consumer<Message> callback;

    // CHANGES
    HashMap<String, Integer> leaderBoard = new HashMap<>(); // username mapped to score
    HashMap<String, Integer> loadedLeaderBoard; // read in from file
    //

    private final String PENDING_REQUESTS_FILE = "pending_requests.txt";

    public Server(Consumer<Message> callback) {
        this.callback = callback;
        loadPendingFriendRequests();
        System.out.println("Server boots up");

        // CHANGES
        loadedLeaderBoard = readLeaderBoardFromFile("leaderboard.txt");
        leaderBoard = loadedLeaderBoard;
        //

        new ServerThread().start();
    }

    // CHANGES
    public static HashMap<String, Integer> readLeaderBoardFromFile(String filename) {
        HashMap<String, Integer> leaderBoard = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    int value = Integer.parseInt(parts[1]);
                    leaderBoard.put(key, value);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the leaderboard: " + e.getMessage());
        }
        return leaderBoard;
    }
    //

    class ServerThread extends Thread {
        public void run() {
            try (ServerSocket serverSocket = new ServerSocket(5555)) {
                System.out.println("Server is running...");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientThread clientThread = new ClientThread(clientSocket);
                    System.out.println("✅ Server accepted a new client connection!");
                    clients.add(clientThread);
                    clientThread.start();
                }
            } catch (Exception e) {
                System.out.println("Server error: " + e.getMessage());
            }
        }
    }

    class ClientThread extends Thread {
        private Socket connection;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private String username;

        ClientThread(Socket connection) {
            this.connection = connection;
        }

        public void run() {
            try {
                out = new ObjectOutputStream(connection.getOutputStream());
                in = new ObjectInputStream(connection.getInputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open.");
            }

            while (true) {
                try {
                    Message message = (Message) in.readObject();
                    switch (message.getType()) {
                        // CHANGES
                        case VIEW_LEADERBOARD:
                            System.out.println("Get Leaderboard");
                            ClientThread thisSenderThread = usernameToClient.get(message.getSenderUsername());
                            if(thisSenderThread != null){
                                try {
                                    // Sort the list by values descending
                                    List<Map.Entry<String, Integer>> list = new ArrayList<>(leaderBoard.entrySet());
                                    list.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

                                    List<Object[]> serializableList = new ArrayList<>();
                                    for (Map.Entry<String, Integer> entry : list) {
                                        serializableList.add(new Object[]{entry.getKey(), entry.getValue()});  // String and Integer
                                    }

                                    System.out.println(serializableList);

                                    Message responseMsg = new Message(message.getSenderUsername());
                                    responseMsg.setLeaderBoard(serializableList);  // Make sure setLeaderBoard accepts List<Object[]>
                                    responseMsg.setType(MessageType.VIEW_LEADERBOARD);

                                    thisSenderThread.out.writeObject(responseMsg);
                                } catch (Exception e) {
                                    System.err.println("Scene Change Error for client " + message.getSenderUsername());
                                }
                            }
                            break;
                        //
                        case RESET_GAME:
                            System.out.println("Here in RESET_GAME: " + message.getSenderUsername());
                            redirectMessageToRecipient(message);
                            break;
                        case GAME_STATUS:
                            System.out.println("Here in GAME_STATUS: " + message.getSenderUsername() + " winner: " + message.getGameStatus());
                            if(message.getSenderUsername().equals(message.getGameStatus())) {
                                try {

                                    leaderBoard.put(message.getSenderUsername(), leaderBoard.get(message.getSenderUsername()) + 1); // increment wins

                                    // Write leaderboard to file
                                    System.out.println("Here in game status rewrite");
                                    writeLeaderBoardToFile(leaderBoard, "leaderboard.txt");

                                } catch (Exception e) {
                                    System.err.println("Scene Change Error for client " + message.getSenderUsername());
                                }
                            }
                            break;
                        case TURN_CHANGE:
                            System.out.println("Here in TURN_CHANGE: " + message.getRecipientUsername());
                            redirectMessageToRecipient(message);
                            break;
                        case MAKE_MOVE:
                            System.out.println("Here in MAKE MOVE: " + message.getRecipientUsername());
                            redirectMessageToRecipient(message);
                            break;
                        case MESSAGE:
                            System.out.println("Here in message: " + message.getRecipientUsername());
                            redirectMessageToRecipient(message);
                            break;
                        case EXIT_GAME:
                            System.out.println("Exit game for: " + message.getSenderUsername() + " and " + message.getRecipientUsername());
                            // Send scene/turn change only to the sender and the specified recipient
                            redirectMessageToRecipient(message);
                            break;
                        //CHANGE
                        case SCENE_CHANGE:
                            System.out.println("SCENE_CHANGE");

                            ClientThread recipientThread = usernameToClient.get(message.getRecipientUsername());
                            ClientThread senderThread = usernameToClient.get(message.getSenderUsername());

                            System.out.println();

                            if (recipientThread != null && senderThread != null) {
                                try {
                                    writeLeaderBoardToFile(leaderBoard, "leaderboard.txt");

                                    recipientThread.out.writeObject(message);
                                    senderThread.out.writeObject(message);
                                    System.out.println("Started game between: " + message.getSenderUsername() + " and " + message.getRecipientUsername());
                                } catch (Exception e) {
                                    System.err.println("Scene Change/Game play Error for client " + recipientThread.username);
                                }
                            }
                            break;
                        //
                        case NEWUSER:
                            System.out.println("in new user");
                            break;
                        case DISCONNECT:
                            System.out.println("in disconnect");
                            break;
//                        case GET_ALL_PLAYERS:
//                            try {
//                                Message response = new Message(MessageType.GET_ALL_PLAYERS);
//                                response.setAllUsernames(new ArrayList<>(usernames));
//                                System.out.println("Sending all usernames to " + message.getSenderUsername() + ": " + usernames);
//                                out.writeObject(response);
//                            } catch (Exception e) {
//                                System.err.println("Get all Users Error: " + e.getMessage());
//                                e.printStackTrace();
//                            }
//                            break;
                        case LOGIN:
                            handleLogin(message);
                            break;
                        case FRIEND_REQUEST:
                            handleFriendRequest(message);
                            break;
                        case FRIEND_ACCEPTED:
                            handleFriendAccepted(message);
                            break;
                        case REMOVE_FRIEND:
                            handleFriendRemoval(message);
                            break;
                        case GET_ALL_PLAYERS:
                            handleGetAllPlayers();
                            break;
                        default:
                            redirectMessageToRecipient(message);
                            // System.out.println("Unhandled server message: " + message.getType());

                    }
                } catch (Exception e) {
                    System.out.println("Server read error: " + e.getMessage());
                    clients.remove(this);
                    if (username != null) {
                        usernameToClient.remove(username);
                        usernames.remove(username);

                        // System.out.println("removed clients here is updated all usernames: ");
                        // System.out.println(usernames);
                    }
                    break;
                }
            }
        }

        // CHANGES
        public void writeLeaderBoardToFile(HashMap<String, Integer> leaderBoard, String filename) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                System.out.println("Writing updated leaderboard");
                for (Map.Entry<String, Integer> entry : leaderBoard.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while writing the leaderboard: " + e.getMessage());
            }
        }

        //

        private void handleLogin(Message message) {
            username = message.getSenderUsername();
            usernameToClient.put(username, this);
            usernames.add(username);
            System.out.println("List: " + usernames);
            System.out.println(username + " logged in.");

            // CHANGES
            //Add to users to leaderboard if they don't exist already
            if (!loadedLeaderBoard.containsKey(username)){
                leaderBoard.put(username, 0); // initialize leaderboard to have all clients with 0 wins
            }
            //

            try {
                // Send pending friend requests
                List<Message> pendingForUser = loadPendingRequestsForUser(username);
                ArrayList<String> inboxSenders = new ArrayList<>();

                for (Message pending : pendingForUser) {
                    out.writeObject(pending); // Send the friend request itself
                    inboxSenders.add(pending.getSenderUsername()); // Also collect sender usernames
                }

                removePendingRequestsForUser(username);

                // Send login success with friend list + inbox
                Message loginSuccess = new Message(MessageType.LOGIN);
                loginSuccess.setSuccess(true);
                loginSuccess.setFriendsList(new ArrayList<>(friendsDatabase.getOrDefault(username, new ArrayList<>())));
                loginSuccess.setInboxList(inboxSenders); // now set real pending inbox
                out.writeObject(loginSuccess);

            } catch (Exception e) {
                System.out.println("Error during login setup: " + e.getMessage());
            }
        }


        private void handleFriendRequest(Message message) {
            String toUser = message.getRecipientUsername();
            ClientThread recipientClient = usernameToClient.get(toUser);

            if (recipientClient != null) {
                try {
                    recipientClient.out.writeObject(message);
                    System.out.println("Friend request sent live to " + toUser);
                } catch (IOException e) {
                    System.out.println("Error sending live friend request.");
                }
            } else {
                savePendingRequest(message);
                System.out.println("Stored friend request for offline user " + toUser);
            }
        }

        private void handleFriendAccepted(Message message) {
            String accepter = message.getSenderUsername();
            String requester = message.getRecipientUsername();

            friendsDatabase.computeIfAbsent(accepter, k -> new ArrayList<>());
            friendsDatabase.computeIfAbsent(requester, k -> new ArrayList<>());

            if (!friendsDatabase.get(accepter).contains(requester)) {
                friendsDatabase.get(accepter).add(requester);
            }
            if (!friendsDatabase.get(requester).contains(accepter)) {
                friendsDatabase.get(requester).add(accepter);
            }

            try {
                if (usernameToClient.containsKey(accepter)) {
                    Message accepted = new Message(MessageType.FRIEND_ACCEPTED);
                    accepted.setSenderUsername(requester);
                    accepted.setFriendsList(new ArrayList<>(friendsDatabase.get(accepter)));
                    usernameToClient.get(accepter).out.writeObject(accepted);
                }
                if (usernameToClient.containsKey(requester)) {
                    Message accepted = new Message(MessageType.FRIEND_ACCEPTED);
                    accepted.setSenderUsername(accepter);
                    accepted.setFriendsList(new ArrayList<>(friendsDatabase.get(requester)));
                    usernameToClient.get(requester).out.writeObject(accepted);
                }
            } catch (IOException e) {
                System.out.println("Error sending accepted friend update.");
            }
        }

        private void handleFriendRemoval(Message message) {
            String remover = message.getSenderUsername();
            String removed = message.getRecipientUsername();

            friendsDatabase.getOrDefault(remover, new ArrayList<>()).remove(removed);
            friendsDatabase.getOrDefault(removed, new ArrayList<>()).remove(remover);

            try {
                if (usernameToClient.containsKey(remover)) {
                    Message update = new Message(MessageType.FRIEND_REMOVED);
                    update.setSenderUsername(removed);
                    update.setFriendsList(new ArrayList<>(friendsDatabase.getOrDefault(remover, new ArrayList<>())));
                    usernameToClient.get(remover).out.writeObject(update);
                }
                if (usernameToClient.containsKey(removed)) {
                    Message update = new Message(MessageType.FRIEND_REMOVED);
                    update.setSenderUsername(remover);
                    update.setFriendsList(new ArrayList<>(friendsDatabase.getOrDefault(removed, new ArrayList<>())));
                    usernameToClient.get(removed).out.writeObject(update);
                }
            } catch (IOException e) {
                System.out.println("Error sending friend removal update.");
            }
        }

        private void handleGetAllPlayers() {
            try {
                Message response = new Message(MessageType.GET_ALL_PLAYERS);
                response.setAllUsernames(new ArrayList<>(usernames));
                out.writeObject(response);
            } catch (IOException e) {
                System.out.println("Error sending all players list.");
            }
        }

        private void redirectMessageToRecipient(Message message) {
            ClientThread recipient = usernameToClient.get(message.getRecipientUsername());
            if (recipient != null) {
                try {
                    recipient.out.writeObject(message);
                    //System.out.println("FOUND and sent to recipient!");
                } catch (IOException e) {
                    System.out.println("Error redirecting message.");
                    //System.err.println("Scene Change Error for client " + recipientThread.username);
                }
            }
        }

        private void savePendingRequest(Message message) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(PENDING_REQUESTS_FILE, true))) {
                bw.write(message.getSenderUsername() + "," + message.getRecipientUsername());
                bw.newLine();
            } catch (IOException e) {
                System.out.println("Error saving pending request.");
            }
        }

        private List<Message> loadPendingRequestsForUser(String username) {
            List<Message> requests = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(PENDING_REQUESTS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2 && parts[1].equals(username)) {
                        Message pending = new Message(MessageType.FRIEND_REQUEST);
                        pending.setSenderUsername(parts[0]);
                        pending.setRecipientUsername(parts[1]);
                        requests.add(pending);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading pending requests.");
            }
            return requests;
        }

        private void removePendingRequestsForUser(String username) {
            try {
                File inputFile = new File(PENDING_REQUESTS_FILE);
                File tempFile = new File("pending_requests_temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    String[] parts = currentLine.split(",");
                    if (parts.length == 2 && !parts[1].equals(username)) {
                        writer.write(currentLine);
                        writer.newLine();
                    }
                }

                writer.close();
                reader.close();

                if (!inputFile.delete()) {
                    System.out.println("Could not delete old pending request file.");
                }
                if (!tempFile.renameTo(inputFile)) {
                    System.out.println("Could not rename temp file to pending_requests.txt");
                }

            } catch (IOException e) {
                System.out.println("Error cleaning pending requests file.");
            }
        }
    }

    private void loadPendingFriendRequests() {
        File file = new File(PENDING_REQUESTS_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create pending_requests.txt");
            }
        }
    }
}
