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
    // String color;
    Random rand = new Random();
    volatile static int x;
    volatile static int y;
    volatile static String color;

    volatile static int lastX = 0;
    volatile static int lastY = 0;

    public static int amountofPlayers = 0;

    public static ArrayList<String> playerColor = new ArrayList<String>();
    public static boolean isPlayerTurn;
    public static int currTurn;
    public static boolean turnEnded = false;

    public static boolean isGameRunning = false;

    public String outMsg;
    public String inMsg;
    public String character;

    // ColorIdx Global

    public Client() {
        try {

            System.out.println("Client created");

            cliente = new Socket(host, puerto);

            this.in = new DataInputStream(cliente.getInputStream());

            this.out = new DataOutputStream(cliente.getOutputStream());

        } catch (Exception e) {
            System.out.println("Error in Client file creating client constructor. Error message: " + e.getMessage());
        }

    }

    // public void sendMessage() {
    // try {
    //
    // while(cliente.isConnected()) {
    //
    // out.writeUTF(outMsg);
    // out.flush();
    //
    //
    // }
    //
    //
    //
    // }catch(Exception e) {
    //
    // }
    // }
    //
    //
    //
    //
    // public void receiveMessage() {
    //
    // new Thread(new Runnable() {
    // public void run() {
    // try {
    // inMsg = in.readUTF();
    //
    //
    //
    //
    // } catch(Exception e) {
    //
    // }
    // }
    // }).start();
    // }

    @Override
    public void run() {
        try {

            // We should receive availableColors, turn, and isPlayerTurn
            String inMsg = in.readUTF();

            String[] strMsg = inMsg.split(";");

            String[] availableColors = strMsg[0].split(",");

            currTurn = Integer.parseInt(strMsg[1]); // Will be used as an index for the coordinates.

            isPlayerTurn = Boolean.parseBoolean(strMsg[2]);

            int colorIdx = rand.nextInt(availableColors.length); // Replace with while(colorIdn != -1) {break;}

            // System.out.println("Color index: " + colorIdx);

            // We should send the index of the amount of players, and the colorIdx to
            // join.
            // This is for the first player, the rest send only the color chosen.
            String outMsg = "";

            // amountofPlayers = Integer.parseInt(JOptionPane.showInputDialog("Enter the
            // amount of players:"));

            if (currTurn == 0)
                outMsg += amountofPlayers + ";" + colorIdx;
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

            // positions = Arrays.copyOfRange(positions, 1, positions.length);
            System.out.println("New Color and Positions: " + Arrays.toString(positions));

            if (currTurn == 0) {
                amountofPlayers = Integer.parseInt(positions[0]);
            }

            positions = Arrays.copyOfRange(positions, 1, positions.length);
            // System.out.println("after if " + Arrays.toString(positions));
            // playerColor.add(positions[0]);

            color = positions[0].trim();
            x = Integer.parseInt(positions[1]);
            y = Integer.parseInt(positions[2]);

            for (int i = 0; i < amountofPlayers; i++) {

                playerColor.add(null);
                Build.playerX.add(0);
                Build.playerY.add(0);

            }

            playerColor.set(currTurn, color);
            Build.playerX.set(currTurn, x);
            Build.playerY.set(currTurn, y);

            System.out.println("Player colors: " + playerColor.toString());
            System.out.println("X-coords: " + x);
            System.out.print("Y-coords: " + y + "\n");

        } catch (Exception e) {
            System.out.println(
                    "Error in Client file receiving and distributing colors and coords for players. Error message: "
                            + e.getMessage());
        }
        try {

            String inMsg = in.readUTF();
            System.out.println("Recieved everyones coords V");
            System.out.println(inMsg);

            inMsg = inMsg.replace('[', ' ').trim();
            inMsg = inMsg.replace(']', ' ').trim();
            String[] strMsg = inMsg.split(";");

            // System.out.println(Arrays.toString(strMsg));

            strMsg[0] = strMsg[0].replace('[', ' ').trim();
            strMsg[0] = strMsg[0].replace(']', ' ').trim();
            String[] cP = strMsg[0].split(",");

            // System.out.println("cP " + Arrays.toString(cP));

            strMsg[1] = strMsg[1].replace('[', ' ').trim();
            strMsg[1] = strMsg[1].replace(']', ' ').trim();
            String[] xP = strMsg[1].split(",");
            // System.out.println("xP " + Arrays.toString(xP));

            strMsg[2] = strMsg[2].replace('[', ' ').trim();
            strMsg[2] = strMsg[2].replace(']', ' ').trim();
            String[] yP = strMsg[2].split(",");
            // System.out.println("yP " + Arrays.toString(yP));

            // System.out.println(Arrays.toString(yP));

            // System.out.println("Amount of players: " + amountofPlayers);

            // System.out.println("Build Player X " + Build.playerX);
            // System.out.println("Build Player Y " + Build.playerY);

            for (int i = 0; i < amountofPlayers; i++) {
                playerColor.set(i, cP[i]);
                Build.playerX.set(i, Integer.parseInt(xP[i].trim()));
                Build.playerY.set(i, Integer.parseInt(yP[i].trim()));

            }

            // System.out.println("------------------------------------");
            // System.out.println("Player Color : " + playerColor);
            // System.out.println("Player X : " + Build.playerX);
            // System.out.println("Player Y : " + Build.playerY);
            // System.out.println("------------------------------------");

        } catch (Exception e) {
            System.out.println("Error in coords: " + e.getMessage());
        }

        // lastX = x;
        // lastY =y;

        try {
            while (true) {

                // System.out.println("Starting out message Client: ");

                // Forces while loop to online run when the values of x and y change.
                if (!isGameRunning && !Build.playerX.contains(0)) {
                    isGameRunning = true;
                    new Build();

                } else if (isPlayerTurn) {
                    // String outMsg = "";
                    // outMsg += x + ";";
                    // outMsg += y + ";";
                    // outMsg += turnEnded + ";";
                    // outMsg += currTurn + ";";

                    Build.playerX.set(currTurn, x);
                    Build.playerY.set(currTurn, y);

                    if ((lastX != x || lastY != y)) {
                        lastX = x;
                        lastY = y;
                        String outMsg = "";
                        outMsg += x + ";";
                        outMsg += y + ";";
                        outMsg += turnEnded + ";";
                        outMsg += currTurn + ";";
                        System.out.println("out: " + outMsg);
                        out.writeUTF(outMsg);
                        out.flush();

                    }
                    Build.playerX.set(currTurn, x);
                    Build.playerY.set(currTurn, y);

                    // Forces the client to wait until all X and Y Values are ready.

                } else {
                    String inMsg = in.readUTF();
                    System.out.println("in: " + inMsg);
                    changeXYValues(inMsg);
                }
            }

        } catch (Exception e) {
            System.out.println("Error in Client file sending constant x and y coordinates. Error message: "
                    + e.getMessage() + "\n" + "Line number: " + e.getStackTrace()[0].getLineNumber());

        }

    }

    // Starts the game
    public static void changeXYValues(String inMsg) {

        inMsg = inMsg.replace('[', ' ').trim();
        inMsg = inMsg.replace(']', ' ').trim();
        String[] strMsg = inMsg.split(";");

        // System.out.println(Arrays.toString(strMsg));

        strMsg[0] = strMsg[0].replace('[', ' ').trim();
        strMsg[0] = strMsg[0].replace(']', ' ').trim();
        String[] cP = strMsg[0].split(",");

        // System.out.println("cP " + Arrays.toString(cP));

        strMsg[1] = strMsg[1].replace('[', ' ').trim();
        strMsg[1] = strMsg[1].replace(']', ' ').trim();
        String[] xP = strMsg[1].split(",");
        // System.out.println("xP " + Arrays.toString(xP));

        strMsg[2] = strMsg[2].replace('[', ' ').trim();
        strMsg[2] = strMsg[2].replace(']', ' ').trim();
        String[] yP = strMsg[2].split(",");
        // System.out.println("yP " + Arrays.toString(yP));

        // System.out.println(Arrays.toString(yP));

        // System.out.println("Amount of players: " + amountofPlayers);

        // System.out.println("Build Player X " + Build.playerX);
        // System.out.println("Build Player Y " + Build.playerY);

        for (int i = 0; i < amountofPlayers; i++) {
            playerColor.set(i, cP[i]);
            Build.playerX.set(i, Integer.parseInt(xP[i].trim()));
            Build.playerY.set(i, Integer.parseInt(yP[i].trim()));

        }

        // System.out.println("------------------------------------");
        // System.out.println("Player Color : " + playerColor);
        // System.out.println("Player X : " + Build.playerX);
        // System.out.println("Player Y : " + Build.playerY);
        // System.out.println("------------------------------------");

        // if (currTurn == Integer.parseInt(strMsg[3]))
        // isPlayerTurn = true;

        // If the game is not running this will start the build for everyone with
        // required values.S

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
