//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.*;
//import java.util.function.Consumer;
//
//public class Server {
//
//    private List<ClientThread> clients = new ArrayList<>();
//    private Map<String, ClientThread> usernameToClient = new HashMap<>();
//    private Map<String, List<String>> pendingFriendRequests = new HashMap<>();
//    private Consumer<Message> callback;
//
//    public Server(Consumer<Message> callback) {
//        this.callback = callback;
//        new ServerThread().start();
//    }
//
//    class ServerThread extends Thread {
//        public void run() {
//            try (ServerSocket serverSocket = new ServerSocket(5555)) {
//                System.out.println("Server is running...");
//                while (true) {
//                    Socket clientSocket = serverSocket.accept();
//                    ClientThread clientThread = new ClientThread(clientSocket);
//                    clients.add(clientThread);
//                    clientThread.start();
//                }
//            } catch (Exception e) {
//                System.out.println("Server error: " + e.getMessage());
//            }
//        }
//    }
//
//    class ClientThread extends Thread {
//        private Socket connection;
//        private ObjectInputStream in;
//        private ObjectOutputStream out;
//        private String username;
//
//        ClientThread(Socket connection) {
//            this.connection = connection;
//        }
//
//        public void run() {
//            try {
//                out = new ObjectOutputStream(connection.getOutputStream());
//                in = new ObjectInputStream(connection.getInputStream());
//                connection.setTcpNoDelay(true);
//            } catch (Exception e) {
//                System.out.println("Streams not open.");
//            }
//
//            while (true) {
//                try {
//                    Message message = (Message) in.readObject();
//                    switch (message.getType()) {
//                        case LOGIN:
//                            username = message.getSenderUsername();
//                            usernameToClient.put(username, this);
//                            System.out.println(username + " logged in.");
//                            break;
//                        case FRIEND_REQUEST:
//                            handleFriendRequest(message);
//                            break;
//                        default:
//                            System.out.println("Unhandled server message: " + message.getType());
//                    }
//                } catch (Exception e) {
//                    System.out.println("Server read error: " + e.getMessage());
//                    clients.remove(this);
//                    break;
//                }
//            }
//        }
//
//        private void handleFriendRequest(Message message) {
//            String toUser = message.getRecipientUsername();
//            ClientThread recipientClient = usernameToClient.get(toUser);
//
//            if (recipientClient != null) {
//                try {
//                    recipientClient.out.writeObject(message);
//                } catch (Exception e) {
//                    System.out.println("Error forwarding friend request.");
//                }
//            } else {
//                // If offline, store the friend request
//                pendingFriendRequests.computeIfAbsent(toUser, k -> new ArrayList<>()).add(message.getSenderUsername());
//            }
//        }
//    }
//}
