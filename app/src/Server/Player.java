package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import Client.ChatPanel;

// Player class used to receive data from a socket
public class Player implements Runnable {
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread thread;
    private String name;
    private Socket socket;
    private Game game;
    private boolean active;
    private ChatPanel chatPanel; // Declare chatPanel variable

    // Constructor takes only a connected socket
    public Player(Socket s, ChatPanel chatPanel) throws Exception {
        active = true;
        socket = s;
        game = null;
        s.setSoTimeout(0);
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        writer = new PrintWriter(s.getOutputStream(), true);

        // First piece of data received is always the player's name
        name = reader.readLine();
        this.chatPanel = chatPanel;
        // Starting new thread to infinitely wait for data from socket
        thread = new Thread(this);
        thread.start();
    }

    // Method for setting the chat panel after the player object is created
    public void setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;
    }

    // Method for closing connection to socket
    public void quit() {
        // If player is active, sending them the quit event
        if (active) {
            write("Q");
        }

        writer.close();

        try {
            reader.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to process data from socket
    @Override
    public void run() {
        try {
            // Infinitely reads data as long as player is active
            while (active) {
                String input = reader.readLine();

                // Handle different types of incoming messages
                if (input.startsWith("Q")) {
                    active = false;
                    game.quit();
                } else if (input.startsWith("E")) {
                    game.setEnter(name);
                } else if (input.startsWith("C")) {
                    // Process chat message
                    processChatMessage(input.substring(1)); // Remove "C" prefix
                } else {
                    game.setGuess(name, input);
                }
            }
        } catch (Exception e) {
            // Handle exceptions
        }
    }

    // Method to send data to player
    public void write(String s) {
        writer.println(s);
    }

    // Getter to get player's name
    public String getName() {
        return name;
    }

    // Setter to set player's game variable
    public void setGame(Game game) {
        this.game = game;
    }

    // Method to send a chat message to the other player
    public void sendChatMessage(String message) {
        System.out.println("GETNF");
        if (writer != null) {
            writer.println("C" + message); // Prefix "C" to indicate a chat message
        }
        chatPanel.appendMessage(message);
    }

    // Method to process and display chat messages received from the server
    private void processChatMessage(String message) {
        chatPanel.appendMessage(message);
    }

    
    // Method to send a chat message to the server

}