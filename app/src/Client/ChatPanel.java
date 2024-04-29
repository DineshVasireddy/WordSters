package Client;

import Server.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    public JTextField inputField;
<<<<<<< HEAD
    private List<Player> players;
    
=======
    private static List<Player> players = new ArrayList<>();
    private static ChatPanel instance;
>>>>>>> a29278b514cd480708f3970e76eff51c48eeba95

    public ChatPanel() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 30));
        add(inputField, BorderLayout.SOUTH);

<<<<<<< HEAD
        players = new ArrayList<>();

=======
>>>>>>> a29278b514cd480708f3970e76eff51c48eeba95
        // Set input map to handle Enter key
        InputMap inputMap = inputField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
        ActionMap actionMap = inputField.getActionMap();
        actionMap.put("sendMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
<<<<<<< HEAD
                System.out.println("HERE IT GOES");
                for (Player player : players) {
                    System.out.println("IM LOOKING FOR THIS " + player.getName());
                }
                
                sendMessage(); // Send message and hide input field
=======
                sendMessageAndHideInputField(); // Send message and hide input field
>>>>>>> a29278b514cd480708f3970e76eff51c48eeba95
            }
        });
    }

<<<<<<< HEAD
=======
    public static ChatPanel getInstance() {
        if (instance == null) {
            instance = new ChatPanel();
        }
        return instance;
    }

>>>>>>> a29278b514cd480708f3970e76eff51c48eeba95
    public void setPlayers(Player player) {
        System.out.println("added player to list");
        players.add(player);
        System.out.println(players);
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        System.out.println(players);
        if (!message.isEmpty()) {
<<<<<<< HEAD
            chatArea.append(message + '\n');
            inputField.setText(""); // Clear the input field
            inputField.setVisible(false); 
        }
    }

    
=======
            for (Player player : players) {
                player.sendChatMessage(message);
            }
            inputField.setText("");
        }
    }

    private void sendMessageAndHideInputField() {
        sendMessage(); // Send the message
        inputField.setText(""); // Clear the input field
        inputField.setVisible(false); // Hide the input field
    }
>>>>>>> a29278b514cd480708f3970e76eff51c48eeba95
}