package Clue;

//Client side.
//Methods in this class will be exclusively for the player and nobody else. 
//Ask doubts abouut if a draw should be here too. 
public class Client {
    public final int SCREEN_WIDTH = 842;
    public final int SCREEN_HEIGHT = 872;
    public final int UNIT_SIZE = 32;
    public final int diceRoll = 0;

    public Client() {

    }

    // Starts the game
    public void startGame() {

    }

    // Gives the player a new dice roll if its their turn.
    public int newDiceRoll() {

        return 0;
    }

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to send accusation cards to next player.
    public void startRumor() {

    }

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to receive accusation cards from last player and
    // respond if needed.
    public String disputeRumor() {

        return null;
    }
}
