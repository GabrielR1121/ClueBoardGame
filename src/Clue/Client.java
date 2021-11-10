package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

import javax.swing.JOptionPane;

//Client side.
//Methods in this class will be exclusively for the player and nobody else. 
//Ask doubts abouut if a draw should be here too. 
public class Client {
    public final int SCREEN_WIDTH = 842;
    public final int SCREEN_HEIGHT = 872;
    public final int UNIT_SIZE = 32;
    public  int diceRoll = 0;
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
    private int puerto = 2027;
    private String host = "localhost";

    public Client() {
        try {

            cliente = new Socket(host,puerto);

            in = new DataInputStream(cliente.getInputStream());

            out = new DataOutputStream(cliente.getOutputStream());
    
        } catch (Exception e) {
        }
      
    }

    // Starts the game
    public void startGame() {

    }

    // Gives the player a new dice roll if its their turn.
    public void newDiceRoll() {

        Random rand = new Random();

        diceRoll = rand.nextInt(6) + 1;

    }

    //run method
    /* Debe recibir con el readUTF():
    *   x and y
    *   Color
    *   isEliminated
    *   isPlayerTurn
    */ 

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to send accusation cards to next player.
    public String startRumor() {

        String room = JOptionPane.showInputDialog( "Enter the room you think the murder was in: ");
        String character = JOptionPane.showInputDialog( "Enter the character you think was the murderer: ");
        String weapon = JOptionPane.showInputDialog( "Enter the weapon you think was used by the murderer: ");

        HashMap<String, Integer> cardDeckMap = new HashMap<String, Integer>();
        cardDeckMap.put("Green", 0);
        cardDeckMap.put("Mustard", 1);
        cardDeckMap.put("Orchid", 2);
        cardDeckMap.put("Peacock", 3);
        cardDeckMap.put("Plum", 4);
        cardDeckMap.put("Scarlett", 5);
        cardDeckMap.put("BallRoom", 6);
        cardDeckMap.put("BilliardRoom", 7);
        cardDeckMap.put("Conservatory", 8);
        cardDeckMap.put("DiningRoom", 9);
        cardDeckMap.put("Hall", 10);
        cardDeckMap.put("Kitchen", 11);
        cardDeckMap.put("Library", 12);
        cardDeckMap.put("Lounge", 13);
        cardDeckMap.put("Study", 14);
        cardDeckMap.put("Candlestick", 15);
        cardDeckMap.put("Dagger", 16);
        cardDeckMap.put("LeadPipe", 17);
        cardDeckMap.put("Revolver", 18);
        cardDeckMap.put("Rope", 19);
        cardDeckMap.put("Wrench", 20);

        return cardDeckMap.get(weapon).toString() + ", "+ cardDeckMap.get(character).toString() + ", " + cardDeckMap.get(room).toString();
        
    }

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to receive accusation cards from last player and
    // respond if needed.
    public String disputeRumor() {
        return null;
    }
}
