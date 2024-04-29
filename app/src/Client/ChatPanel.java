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
    private List<Player> players;
    

    public ChatPanel() {
        setLayout(new BorderLayout());

        chatArea = new JTextArea(10, 30);
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(200, 30));
        add(inputField, BorderLayout.SOUTH);

        players = new ArrayList<>();

        // Set input map to handle Enter key
        InputMap inputMap = inputField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "sendMessage");
        ActionMap actionMap = inputField.getActionMap();
        actionMap.put("sendMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("HERE IT GOES");
                for (Player player : players) {
                    System.out.println("IM LOOKING FOR THIS " + player.getName());
                }
                
                sendMessage(); // Send message and hide input field
            }
        });
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
            chatArea.append(message + '\n');
            inputField.setText(""); // Clear the input field
            inputField.setVisible(false); 
        }
    }

    
}