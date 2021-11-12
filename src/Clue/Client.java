package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;
import javax.swing.JOptionPane;

//Client side.
//Methods in this class will be exclusively for the player and nobody else. 
//Ask doubts abouut if a draw should be here too. 
public class Client implements Runnable {
    public final int SCREEN_WIDTH = 842;
    public final int SCREEN_HEIGHT = 872;
    public final int UNIT_SIZE = 32;
    public  int diceRoll = 0;
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
    private int puerto = 2027;
    private String host = "localhost";
    private boolean isPlayerTurn;
    String color;
    Random rand = new Random();
    static int x;
    static int y;

    public Client() {
        try {

            System.out.println("Client created");

            cliente = new Socket(host,puerto);

            in = new DataInputStream(cliente.getInputStream());

            out = new DataOutputStream(cliente.getOutputStream());
    
        } catch (Exception e) {
        }
      
    }
    
    @Override
    public void run(){
        try {
            
            String msg = in.readUTF();

            System.out.println("" + msg + " " + isPlayerTurn);
            

            // if(Integer.parseInt(msg) == 0){
                
            //     isPlayerTurn = true;

            //     
            // }
        
            
        } catch (Exception e) {

        }


    }

    // Starts the game
    public void startClient() {

    }
    
    public static void getStartingCoordinates(String[] charArr) {

        x = Integer.parseInt((charArr[0].replace('[', ' ')).trim());
        y = Integer.parseInt((charArr[1].replace(']', ' ')).trim());

    }
    
    // As an example cause there is not enough data.
    // THIS WILL BE EREASED
    String newColor[] = { "Green", "Mustard", "Orchid", "Peacock", "Plum", "Scarlett" };
    String[] charArr;
    
    // JUST FOR TEST
    //EL HASHMAP DE CHARACTERS LO ESTA SACANDO DIRECTO DE SERVER.
    //FIX THIS!!!!!
    // Sets all players in their respective start positions.
    public void newPlayer() {
        color = newColor[rand.nextInt(5)];

        switch (color) {
        case "Green":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Mustard":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Orchid":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Peacock":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Plum":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Scarlett":
            charArr = (Arrays.toString(Build.characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        }

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

        //This may be removed later on
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
