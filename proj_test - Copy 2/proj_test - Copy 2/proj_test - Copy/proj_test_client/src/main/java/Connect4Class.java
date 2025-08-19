import java.io.*;
import java.util.ArrayList;

public class Connect4Class {
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private final String FILE_NAME = "users.txt";

    public Connect4Class() {
        loadUsers();
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    allPlayers.add(new Player(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("No users yet. A new file will be created on first signup.");
        }
    }

    private void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Player p : allPlayers) {
                bw.write(p.getUsername() + "," + p.getPassword());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    public boolean validateUser(String username, String password) {
        for (Player p : allPlayers) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean createUser(String username, String password) {
        for (Player p : allPlayers) {
            if (p.getUsername().equals(username)) {
                return false; // duplicate username
            }
        }
        allPlayers.add(new Player(username, password));
        saveUsers();
        return true;
    }
}