import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {

    private Socket socketClient;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Consumer<Message> callback;

    public Client(Consumer<Message> callback) {
        this.callback = callback;
    }

    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
            System.out.println("✅ Client successfully connected to Server!");

        } catch (Exception e) {
            System.out.println("Client connection error: " + e.getMessage());
        }

        while (true) {
            try {
                Message message = (Message) in.readObject();
                callback.accept(message);
            } catch (Exception e) {
                System.out.println("Client read error: " + e.getMessage());
                break;
            }
        }
    }

    public void send(Message message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            System.out.println("Client send error: " + e.getMessage());
        }
    }
}

