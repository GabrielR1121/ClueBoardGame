package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

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
    public void startRumor() {

    }

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to receive accusation cards from last player and
    // respond if needed.
    public String disputeRumor() {

        return null;
    }
}
