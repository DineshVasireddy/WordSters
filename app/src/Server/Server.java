package Server;

import javax.swing.*;

import Client.ChatPanel;
import java.util.Collections;
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
    private ArrayList<Player> connectedPlayers = new ArrayList<>();
    private Scanner console;
    private String code;
    private String lobbySize;
    private Player playerOne;
    private Player playerTwo;
    private Player playerThree;
    private Player playerFour;
    private Random random;
    private Scanner scanner;
    private int i;
    private HashMap<Integer, String> words;
    static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_GREEN = "\n\u001B[32m";

    public Server(String lobbyCode, String lobbySize) throws Exception {
        serverSocket = new ServerSocket(6666);
        code = lobbyCode;
        this.lobbySize = lobbySize;
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
            ChatPanel chatPanelPlayerOne = new ChatPanel(); // Create a new chat panel for player one
            ChatPanel chatPanelPlayerTwo = new ChatPanel(); 
            ChatPanel chatPanelPlayerThree = new ChatPanel();
            ChatPanel chatPanelPlayerFour = new ChatPanel();

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
            System.out.println("Lobby Code = " + ANSI_RED + code + ANSI_RESET);

            // Checking if code is a quit event
            if (code.equals("q")) {
                break;
            }

            // Getting lobby size from user (either 1 or 2)
            // System.out.println("Enter Lobby Size");
            // lobbySize = console.nextLine();
            System.out.println("Lobby Size: " + ANSI_RED + lobbySize + ANSI_RESET);

            if (lobbySize.equals("2")) {
                // Create a single chat panel
                playerOne = getPlayer(chatPanelPlayerOne);
                playerOne.setChatPanel(chatPanelPlayerOne);

                playerTwo = getPlayer(chatPanelPlayerTwo);
                playerTwo.setChatPanel(chatPanelPlayerTwo);

                
                System.out.println(playerOne.getName() + " Joined");
                
                connectedPlayers.add(playerOne);
                connectedPlayers.add(playerTwo);

                System.out.println(playerTwo.getName() + " Joined");
                
                
                
                // Creating new game with both players
                
                new TwoPlayerGame(playerOne, playerTwo, this, chatPanelPlayerOne, chatPanelPlayerTwo);
            } else if (lobbySize.equals("3")) {
                
                //Getting first player
                playerOne = getPlayer(chatPanelPlayerOne);
                System.out.println(playerOne.getName() + " Joined");

                //Getting second player
                playerTwo = getPlayer(chatPanelPlayerTwo);
                System.out.println(playerTwo.getName() + " Joined");

                //Getting third player
                playerThree = getPlayer(chatPanelPlayerThree);
                connectedPlayers.add(playerThree);
                System.out.println(playerThree.getName() + " Joined");

                //Creating new game with three players
                new ThreePlayerGame(playerOne, playerTwo, playerThree, this);
            }
            if (lobbySize.equals("4")) {
                playerOne = getPlayer(chatPanelPlayerOne);
                playerOne.setChatPanel(chatPanelPlayerOne);

                playerTwo = getPlayer(chatPanelPlayerTwo);
                playerTwo.setChatPanel(chatPanelPlayerTwo);

                playerThree = getPlayer(chatPanelPlayerThree);
                playerThree.setChatPanel(chatPanelPlayerThree);

                playerFour = getPlayer(chatPanelPlayerFour);
                playerFour.setChatPanel(chatPanelPlayerFour);

                
                System.out.println(playerOne.getName() + " Joined");
                
                connectedPlayers.add(playerOne);
                connectedPlayers.add(playerTwo);
                connectedPlayers.add(playerThree);
                connectedPlayers.add(playerFour);

                System.out.println(playerTwo.getName() + " Joined");
                System.out.println(playerThree.getName() + " Joined");
                System.out.println(playerFour.getName() + " Joined");

                new TwoPlayerGame(playerOne, playerTwo, this, chatPanelPlayerOne, chatPanelPlayerTwo);
                new TwoPlayerGame(playerThree, playerFour, this, chatPanelPlayerThree, chatPanelPlayerFour);
                
            }{
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
    public Player getPlayer(ChatPanel chatPanel) throws Exception {
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
                    return new Player(s, chatPanel);

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
        String lobbySize = args[1];
        new Server(lobbyCode, lobbySize);
    }

    public void addPlayer(Player player) {
        connectedPlayers.add(player);
    }

    // Method to remove a player from the connected players list
    public void removePlayer(Player player) {
        connectedPlayers.remove(player);
    }
}