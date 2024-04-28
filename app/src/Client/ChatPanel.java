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
    private static List<Player> players = new ArrayList<>();
    private static ChatPanel instance;

    public ChatPanel() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 30));
        add(inputField, BorderLayout.SOUTH);

        // Set input map to handle Enter key
        InputMap inputMap = inputField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
        ActionMap actionMap = inputField.getActionMap();
        actionMap.put("sendMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageAndHideInputField(); // Send message and hide input field
            }
        });
    }

    public static ChatPanel getInstance() {
        if (instance == null) {
            instance = new ChatPanel();
        }
        return instance;
    }

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
}