package Server;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

// Server main class used for starting games
public class Server {
    private ServerSocket serverSocket;
    private Scanner console;
    private String code;
    private String lobbySize;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;
    private Random random;
    private Scanner scanner;
    private int i;
    private HashMap<Integer, String> words;
    static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\n\u001B[32m";

    public Server(String lobbyCode) throws Exception {
        serverSocket = new ServerSocket(6666);
        code = lobbyCode;
        console = new Scanner(System.in);
        random = new Random();
        File file = new File("src/Resources/words.txt");
        scanner = new Scanner(file);
        words = new HashMap<>();
        i = 0;

        // Adding all words from words.txt into words' hashmap
        while (scanner.hasNextLine()) {
            words.put(i, scanner.nextLine());
            i++;
        }
        String input = "";
        // Infinite loop to start a new game after one ends
        while (true) {
            System.out.println(ANSI_GREEN + "Creating New Lobby" + ANSI_RESET);

            // Getting lobby code from user
            // while (true) {
            //     System.out.println("Enter Lobby Code or \"q\" to close server");
            //     if (console.hasNextLine()) {
            //         input = console.nextLine().trim();
            //         if (!input.isEmpty()) {
            //             break; // Break out of the loop if input is not empty
            //         } else {
            //             System.out.println("Please enter a valid lobby code.");
            //         }
            //     } else {
            //         // Handle case where there's no input available
            //         System.out.println("No input received. Exiting...");
            //         return; // Exiting the program
            //     }
            // }
            
            // code = "OurRoom";
            System.out.println("Room = " + ANSI_RED + code + ANSI_RESET);

            // Checking if code is a quit event
            if (code.equals("q")) {
                break;
            }

            // Getting lobby size from user (either 1 or 2)
            // System.out.println("Enter Lobby Size");
            // lobbySize = console.nextLine();
            lobbySize = "2";
            System.out.println("Default Lobby Size: " + ANSI_RED + lobbySize + ANSI_RESET);

            if (lobbySize.equals("2")) {
                // Create a single chat panel
                ChatPanel chatPanel = new ChatPanel();
                
                // Getting first player
                playerOne = getPlayer();
                System.out.println(playerOne.getName() + " Joined");
                
                // Getting second player
                playerTwo = getPlayer();
                System.out.println(playerTwo.getName() + " Joined");
                
                // Associate both players with the same chat panel
                List<Player> players = new ArrayList<>();
                players.add(playerOne);
                players.add(playerTwo);
                chatPanel.setPlayers(players);
                
                // Set the chat panel for each player
                playerOne.setChatPanel(chatPanel);
                playerTwo.setChatPanel(chatPanel);
                
                // Creating new game with both players
                
                new TwoPlayerGame(playerOne, playerTwo, this, chatPanel, chatPanel);
            } else if (lobbySize.equals("3")) {
                // Handle lobby size of 3
            } else {
                System.out.println(ANSI_RED + "Invalid Lobby Size" + ANSI_RESET);
            }
        }

        // Closing server after receiving quit event from user
        System.out.println(ANSI_RED + "Closing Server" + ANSI_RESET);
        // console.close();
        // scanner.close();
        // serverSocket.close();
    }

    // Method that waits from a socket to make a connection then creates a Player using socket
    public Player getPlayer() throws Exception {
        Socket s;
        BufferedReader reader;
        PrintWriter writer;
        String input;

        // Infinite loop until successful connection with a socket
        while (true) {

            // Waiting for a socket to connect
            s = serverSocket.accept();

            // Setting timeout for connection reads
            s.setSoTimeout(2000);
            System.out.println("Accepted socket");
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new PrintWriter(s.getOutputStream(), true);

            try {
                // Waiting 2 seconds for socket to send lobby code
                input = reader.readLine();

                // Making sure lobby code is correct
                if (code.equals(input)) {
                    writer.println(lobbySize);
                    return new Player(s);

                    // If lobby code is incorrect, closing connection and telling socket they sent incorrect code
                } else {
                    writer.println("F");
                    s.close();
                }

                // Catching exception if didn't send lobby code within timeout time or if socket closed its connection
            } catch (Exception e) {
                writer.println("F");
                s.close();
                reader.close();
                writer.close();
            }
        }
    }

    // Method to get random word
    public String getWord() {
        return words.get(random.nextInt(i)).toUpperCase();
    }

    // Main method, creates and starts a server
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Server <lobby_code>");
            return;
        }
        String lobbyCode = args[0];
        new Server(lobbyCode);
    }
}