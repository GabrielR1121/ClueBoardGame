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
    private Socket cliente;
    private DataOutputStream out;
    private DataInputStream in;
    private int puerto = 2027;
    private String host = "localhost";
    // private boolean isPlayerTurn;
    String color;
    Random rand = new Random();
    static int x;
    static int y;
    private int amountofPlayers = 0;
    public static ArrayList<Integer> playerX = new ArrayList<Integer>();
    public static ArrayList<Integer> playerY = new ArrayList<Integer>();
    public static ArrayList<String> playerColor = new ArrayList<String>();
    public static boolean isPlayerTurn;
    public static int currTurn;

    public Client() {
        try {

            System.out.println("Client created");

            cliente = new Socket(host, puerto);

            in = new DataInputStream(cliente.getInputStream());

            out = new DataOutputStream(cliente.getOutputStream());

        } catch (Exception e) {
            System.out.println("Error in Client file creating client constructor. Error message: " + e.getMessage());
        }

    }

    @Override
    public void run() {
        try {

            // We should receive availableColors, turn, and isPlayerTurn
            String inMsg = in.readUTF();

            // System.out.println(inMsg);

            String[] strMsg = inMsg.split(";");

            String[] availableColors = strMsg[0].split(",");

            currTurn = Integer.parseInt(strMsg[1]); // Will be used as an index for the coordinates.

            isPlayerTurn = Boolean.parseBoolean(strMsg[2]);

            int colorIdx = rand.nextInt(availableColors.length); // The error in threadServer is happening because
                                                                 // of this. When we make the
                                                                 // player pick the color the error will go away.

            // System.out.println("Color index: " + colorIdx);

            // We should send the index of the color chosen, and the amount of players to
            // join.
            // This is for the first player, the rest send only the color chosen.
            String outMsg = "";

            amountofPlayers = 6;

            if (currTurn == 0)
                outMsg += colorIdx + ";" + amountofPlayers;
            else
                outMsg += colorIdx + ";";

            out.writeUTF(outMsg);
        } catch (Exception e) {
            System.out.println(
                    "Error in Client file receiving and sending of first message. Error message: " + e.getMessage());
        }

        try {
            String inMsg = "";
            // Message to be received: Amount of players, and the starting position of each
            // player.
            inMsg = in.readUTF();

            // The first thing being sent is the amount of players, then the positions.
            String[] positions = inMsg.split(";");

            positions = Arrays.copyOfRange(positions, 1, positions.length);
            System.out.println("New Color and Positions: " + Arrays.toString(positions));

            for (int i = 0; i < positions.length; i += 3) {
                playerColor.add(positions[i]);
                playerX.add(Integer.parseInt(positions[i + 1]));
                playerY.add(Integer.parseInt(positions[i + 2]));
            }

            System.out.println("Player colors: " + playerColor.toString());
            System.out.println("X-coords: " + playerX.toString());
            System.out.print("Y-coords: " + playerY.toString() + "\n");

        } catch (Exception e) {
            System.out.println(
                    "Error in Client file receiving and distributing colors and coords for players. Error message: "
                            + e.getMessage());
        }

    }

    // Starts the game
    public void startClient() {

    }

    // run method
    /*
     * Debe recibir con el readUTF(): x and y Color isEliminated isPlayerTurn
     */

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to send accusation cards to next player.
    public String startRumor() {

        String room = JOptionPane.showInputDialog("Enter the room you think the murder was in: ");
        String character = JOptionPane.showInputDialog("Enter the character you think was the murderer: ");
        String weapon = JOptionPane.showInputDialog("Enter the weapon you think was used by the murderer: ");

        // This may be removed later on
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

        return cardDeckMap.get(weapon).toString() + ", " + cardDeckMap.get(character).toString() + ", "
                + cardDeckMap.get(room).toString();

    }

    // Makes refrence to gameframe in order to create the window needed. Also needs
    // to connect to server inorder to receive accusation cards from last player and
    // respond if needed.
    public String disputeRumor() {
        return null;
    }
}
