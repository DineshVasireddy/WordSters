package Server;

//Interface for game classes
public interface Game {

    //Method used when terminating a game
    public void quit();

    //Method for processing guess event
    public void setGuess(String playerName, String guess);

    //Method for processing VK_ENTER event
    public void setEnter(String playerName);

    // Method for sending a chat message to the other player(s)
    public void sendChatMessage(String playerName, String message);

}