package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private List<Player> players;

    public ChatPanel() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        players = new ArrayList<>();

        // Set input map to handle TAB key
        InputMap inputMap = inputField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("TAB"), "focusInputField");
        ActionMap actionMap = inputField.getActionMap();
        actionMap.put("focusInputField", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputField.requestFocusInWindow();
            }
        });
        
        // Set input map to handle Enter key
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
        actionMap.put("sendMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Add action listener to the send button
        ((AbstractButton) sendButton).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    // Method to set the associated players
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }

    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            // Send the message to all associated players
            for (Player player : players) {
                player.sendChatMessage(message);
            }
            inputField.setText("");
        }
    }
}