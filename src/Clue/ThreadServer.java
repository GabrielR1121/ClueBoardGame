package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.*;

//Runs the actual program. In order to understand what needs to be done here i need to understand how the remote server works (ask Christian).
public class ThreadServer implements Runnable {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isPlayerTurn;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private int turn;
    public volatile int amountofPlayers = 0;
    int count = 0;
    public static int playerTurn = 0;
    // Proximo mensaje

    String startingPos = "";

    // Permited moves Hashmap
    static HashMap<Integer, ArrayList<Integer>> permitedCoordinates = new HashMap<Integer, ArrayList<Integer>>();

    // Characters hashmap
    public static HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

    // Doors positions hashmap
    static HashMap<Integer, ArrayList<Integer>> doors = new HashMap<Integer, ArrayList<Integer>>();

    // Has the string info of all of the cards on the cardDeck array.
    static HashMap<Integer, String> cardDeckMap = new HashMap<Integer, String>();

    public static ArrayList<Integer> playerX = new ArrayList<Integer>();
    public static ArrayList<Integer> playerY = new ArrayList<Integer>();
    public static ArrayList<String> playerColor = new ArrayList<String>();

    public ThreadServer(Socket soc, LinkedList users, int turno) {

        try {

            this.users = users;
            socket = soc;
            this.turn = turno;
            System.out.println("Player " + (turn + 1) + " has joined the game");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            System.out.println("Error in ThreadServer creating thread constructor. Error message: " + e.getMessage());
        }

    }

    @Override
    public void run() {

        System.out.println("Starting ThreadServer... \n -------------------");

        try {

            // First message to be sent is: availableColors, turn, and isPlayerTurn
            String outMsg = "";

            for (int i = 0; i < Server.availableColors.size(); i++) {

                if (i < Server.availableColors.size() - 1)
                    outMsg += Server.availableColors.get(i) + ",";
                else
                    outMsg += Server.availableColors.get(i) + ";";
            }

            outMsg += turn + ";";

            if (turn == 0)
                isPlayerTurn = true;

            outMsg += isPlayerTurn + ";";

            // System.out.println("first messaage: " + outMsg);

            out.writeUTF(outMsg);

        } catch (Exception e) {
            System.out.println("Error in ThreadServer file sending first message. Error message: " + e.getMessage());
        }

        try {
            // Will receive the color chosen and the amountofPLayers (for the first Client).
            // System.out.println("-------");
            String inMsg = in.readUTF();

            String[] strMsg = inMsg.split(";");

            // System.out.println("Array:" + Arrays.toString(strMsg));
            // System.out.println("Available colors: " + Server.availableColors);

            if (turn == 0) {
                amountofPlayers = Integer.parseInt(strMsg[0]);
                strMsg = Arrays.copyOfRange(strMsg, 1, strMsg.length);
                Server.sb.append(amountofPlayers + ";");
            }

            newPlayer(Server.availableColors.get(Integer.parseInt(strMsg[0])));

            playerColor.add(Server.availableColors.get(Integer.parseInt(strMsg[0])));
            Server.availableColors.remove(Integer.parseInt(strMsg[0]));

            // System.out.println("starting pos: " + Server.sb.toString());

            // System.out.println(Server.sb.toString());

            // The message being sent contains the amount of players, and the starting
            // position of each player.
            // System.out.println(
            // "Colors and X Y" + playerColor.get(turn) + ";" + playerX.get(turn) + ";" +
            // playerY.get(turn));

            out.writeUTF(
                    amountofPlayers + ";" + playerColor.get(turn) + ";" + playerX.get(turn) + ";" + playerY.get(turn));

            // System.out.println("Player X size atm: " + playerX.size());
            // System.out.println(Server.availableColors.toString());

        } catch (Exception e) {
            System.out.println(
                    "Error in ThreadServer file receiving amount of players and color player wants. Error message: "
                            + e.getMessage());
        }

        try {

            while (true) {

                if (playerX.size() == amountofPlayers) {

                    String inMsg = in.readUTF();
                    System.out.println(inMsg);

                    String[] strMsg = inMsg.split(";");

                    System.out.println("In: " + Arrays.toString(strMsg));

                    if (Boolean.parseBoolean(strMsg[2]) == true) {
                        playerTurn++;
                        if (playerTurn == 5)
                            playerTurn = 0;
                    }

                    playerX.set(Integer.parseInt(strMsg[3]), Integer.parseInt(strMsg[0]));
                    playerY.set(Integer.parseInt(strMsg[3]), Integer.parseInt(strMsg[1]));

                    String outMsg = playerColor.toString() + ";" + playerX.toString() + ";" + playerY.toString() + ";"
                            + playerTurn;
                    out.writeUTF(outMsg);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in ThreadServer file receiving amount of x and y coordinates. Error message: "
                    + e.getMessage());
        }

    }

    public static void getStartingCoordinates(String[] charArr) {

        // System.out.println("STARTING COORDS..." + Arrays.toString(charArr));

        playerX.add(Integer.parseInt((charArr[0].replace('[', ' ')).trim()));
        playerY.add(Integer.parseInt((charArr[1].replace(']', ' ')).trim()));

    }

    // As an example cause there is not enough data.
    // THIS WILL BE EREASED
    String newColor[] = { "Green", "Mustard", "Orchid", "Peacock", "Plum", "Scarlett" };
    String[] charArr;

    // JUST FOR TEST
    // EL HASHMAP DE CHARACTERS LO ESTA SACANDO DIRECTO DE SERVER.
    // FIX THIS!!!!!
    // Sets all players in their respective start positions.
    public void newPlayer(String color) {

        // System.out.println("EN NEW PLAYER..." + color);

        switch (color) {
        case "Green":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        case "Mustard":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        case "Orchid":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        case "Peacock":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        case "Plum":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        case "Scarlett":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
            break;
        }

    }

    public static void main() {

        // Adding the starting coordinates for each of the characters.
        characters.put("Green", new Integer[] { 327, 807 });
        characters.put("Mustard", new Integer[] { 775, 263 });
        characters.put("Orchid", new Integer[] { 487, 807 });// to do
        characters.put("Peacock", new Integer[] { 39, 615 });
        characters.put("Plum", new Integer[] { 39, 199 });
        characters.put("Scarlett", new Integer[] { 551, 39 });

        // Adding the starting coordinates for each of the doors.
        doors.put(487, new ArrayList<>());
        doors.get(487).add(551); // ball room
        doors.put(231, new ArrayList<>());
        doors.get(231).add(519); // billiard room
        doors.put(199, new ArrayList<>());
        doors.get(199).add(647); // conservatory
        doors.put(519, new ArrayList<>());
        doors.get(519).add(423); // dining room
        doors.put(295, new ArrayList<>());
        doors.get(295).add(167); // hall
        doors.put(647, new ArrayList<>());
        doors.get(647).add(583); // kitchen
        doors.put(263, new ArrayList<>());
        doors.get(263).add(295); // library
        doors.put(583, new ArrayList<>());
        doors.get(583).add(231); // lounge
        // doors.put(231, new ArrayList<>());
        doors.get(231).add(167); // study
        doors.put(391, new ArrayList<>());
        doors.get(391).add(263); // decision

        // Hashmap of the info on the cardDeck elements (dudas)
        cardDeckMap.put(0, "Green");
        cardDeckMap.put(1, "Mustard");
        cardDeckMap.put(2, "Orchid");
        cardDeckMap.put(3, "Peacock");
        cardDeckMap.put(4, "Plum");
        cardDeckMap.put(5, "Scarlett");
        cardDeckMap.put(6, "BallRoom");
        cardDeckMap.put(7, "BilliardRoom");
        cardDeckMap.put(8, "Conservatory");
        cardDeckMap.put(9, "DiningRoom");
        cardDeckMap.put(10, "Hall");
        cardDeckMap.put(11, "Kitchen");
        cardDeckMap.put(12, "Library");
        cardDeckMap.put(13, "Lounge");
        cardDeckMap.put(14, "Study");
        cardDeckMap.put(15, "Candlestick");
        cardDeckMap.put(16, "Dagger");
        cardDeckMap.put(17, "LeadPipe");
        cardDeckMap.put(18, "Revolver");
        cardDeckMap.put(19, "Rope");
        cardDeckMap.put(20, "Wrench");

        permitedCoordinates.put(263, new ArrayList<Integer>());
        permitedCoordinates.get(263).add(71);
        permitedCoordinates.get(263).add(135);
        permitedCoordinates.get(263).add(103);
        permitedCoordinates.get(263).add(167);
        permitedCoordinates.get(263).add(199);
        permitedCoordinates.get(263).add(231);
        permitedCoordinates.get(263).add(263);
        permitedCoordinates.get(263).add(295);
        permitedCoordinates.get(263).add(327);
        permitedCoordinates.get(263).add(359);
        permitedCoordinates.get(263).add(391);
        permitedCoordinates.get(263).add(423);
        permitedCoordinates.get(263).add(455);
        permitedCoordinates.get(263).add(487);
        permitedCoordinates.get(263).add(519);
        permitedCoordinates.get(263).add(551);
        permitedCoordinates.get(263).add(583);
        permitedCoordinates.get(263).add(615);
        permitedCoordinates.get(263).add(647);
        permitedCoordinates.get(263).add(679);
        permitedCoordinates.get(263).add(711);
        permitedCoordinates.get(263).add(743);
        permitedCoordinates.get(263).add(775);
        /////
        permitedCoordinates.put(295, new ArrayList<Integer>());
        permitedCoordinates.get(295).add(71);
        permitedCoordinates.get(295).add(103);
        permitedCoordinates.get(295).add(135);
        permitedCoordinates.get(295).add(167);
        permitedCoordinates.get(295).add(199);
        permitedCoordinates.get(295).add(231);
        permitedCoordinates.get(295).add(263);
        permitedCoordinates.get(295).add(295);
        permitedCoordinates.get(295).add(327);
        permitedCoordinates.get(295).add(359);
        permitedCoordinates.get(295).add(391);
        permitedCoordinates.get(295).add(423);
        permitedCoordinates.get(295).add(455);
        permitedCoordinates.get(295).add(487);
        permitedCoordinates.get(295).add(519);
        permitedCoordinates.get(295).add(551);
        permitedCoordinates.get(295).add(775);
        ///
        permitedCoordinates.put(231, new ArrayList<Integer>());
        permitedCoordinates.get(231).add(199);
        permitedCoordinates.get(231).add(231);
        permitedCoordinates.get(231).add(359);
        permitedCoordinates.get(231).add(391);
        permitedCoordinates.get(231).add(423);
        permitedCoordinates.get(231).add(455);
        permitedCoordinates.get(231).add(487);
        permitedCoordinates.get(231).add(519);
        permitedCoordinates.get(231).add(551);
        permitedCoordinates.get(231).add(583);
        permitedCoordinates.get(231).add(615);
        permitedCoordinates.get(231).add(647);
        permitedCoordinates.get(231).add(679);
        permitedCoordinates.get(231).add(711);
        permitedCoordinates.get(231).add(743);
        permitedCoordinates.get(231).add(167);
        //////
        permitedCoordinates.put(199, new ArrayList<Integer>());
        permitedCoordinates.get(199).add(167);
        permitedCoordinates.get(199).add(199);
        permitedCoordinates.get(199).add(391);
        permitedCoordinates.get(199).add(583);
        permitedCoordinates.get(199).add(615);
        permitedCoordinates.get(199).add(647);
        /////
        permitedCoordinates.put(167, new ArrayList<Integer>());
        permitedCoordinates.get(167).add(167);
        permitedCoordinates.get(167).add(199);
        permitedCoordinates.get(167).add(391);
        permitedCoordinates.get(167).add(583);
        permitedCoordinates.get(167).add(615);
        ////////
        permitedCoordinates.put(135, new ArrayList<Integer>());
        permitedCoordinates.get(135).add(167);
        permitedCoordinates.get(135).add(199);
        permitedCoordinates.get(135).add(391);
        permitedCoordinates.get(135).add(583);
        permitedCoordinates.get(135).add(615);
        //
        permitedCoordinates.put(103, new ArrayList<Integer>());
        permitedCoordinates.get(103).add(167);
        permitedCoordinates.get(103).add(199);
        permitedCoordinates.get(103).add(391);
        permitedCoordinates.get(103).add(583);
        permitedCoordinates.get(103).add(615);
        ////
        permitedCoordinates.put(71, new ArrayList<Integer>());
        permitedCoordinates.get(71).add(167);
        permitedCoordinates.get(71).add(199);
        permitedCoordinates.get(71).add(391);
        permitedCoordinates.get(71).add(583);
        permitedCoordinates.get(71).add(615);
        //
        permitedCoordinates.put(327, new ArrayList<Integer>());
        permitedCoordinates.get(327).add(263);
        permitedCoordinates.get(327).add(519);
        permitedCoordinates.get(327).add(551);
        permitedCoordinates.get(327).add(775);
        //
        permitedCoordinates.put(359, new ArrayList<Integer>());
        permitedCoordinates.get(359).add(263);
        permitedCoordinates.get(359).add(519);
        permitedCoordinates.get(359).add(551);
        //
        permitedCoordinates.put(391, new ArrayList<Integer>());
        permitedCoordinates.get(391).add(263);
        permitedCoordinates.get(391).add(519);
        permitedCoordinates.get(391).add(551);
        //
        permitedCoordinates.put(423, new ArrayList<Integer>());
        permitedCoordinates.get(423).add(263);
        permitedCoordinates.get(423).add(519);
        permitedCoordinates.get(423).add(551);
        //
        permitedCoordinates.put(455, new ArrayList<Integer>());
        permitedCoordinates.get(455).add(263);
        permitedCoordinates.get(455).add(519);
        permitedCoordinates.get(455).add(551);
        //
        permitedCoordinates.put(487, new ArrayList<Integer>());
        permitedCoordinates.get(487).add(263);
        permitedCoordinates.get(487).add(295);
        permitedCoordinates.get(487).add(327);
        permitedCoordinates.get(487).add(359);
        permitedCoordinates.get(487).add(391);
        permitedCoordinates.get(487).add(423);
        permitedCoordinates.get(487).add(455);
        permitedCoordinates.get(487).add(487);
        permitedCoordinates.get(487).add(519);
        permitedCoordinates.get(487).add(551);
        permitedCoordinates.get(487).add(775);
        /////
        permitedCoordinates.put(519, new ArrayList<Integer>());
        permitedCoordinates.get(519).add(71);
        permitedCoordinates.get(519).add(103);
        permitedCoordinates.get(519).add(135);
        permitedCoordinates.get(519).add(167);
        permitedCoordinates.get(519).add(199);
        permitedCoordinates.get(519).add(231);
        permitedCoordinates.get(519).add(263);
        permitedCoordinates.get(519).add(295);
        permitedCoordinates.get(519).add(327);
        permitedCoordinates.get(519).add(359);
        permitedCoordinates.get(519).add(391);
        permitedCoordinates.get(519).add(423);
        permitedCoordinates.get(519).add(455);
        permitedCoordinates.get(519).add(487);
        permitedCoordinates.get(519).add(519);
        permitedCoordinates.get(519).add(551);
        permitedCoordinates.get(519).add(775);
        ///
        permitedCoordinates.put(551, new ArrayList<Integer>());
        permitedCoordinates.get(551).add(71);
        permitedCoordinates.get(551).add(103);
        permitedCoordinates.get(551).add(135);
        permitedCoordinates.get(551).add(167);
        permitedCoordinates.get(551).add(199);
        permitedCoordinates.get(551).add(231);
        permitedCoordinates.get(551).add(263);
        permitedCoordinates.get(551).add(295);
        permitedCoordinates.get(551).add(519);
        permitedCoordinates.get(551).add(551);
        permitedCoordinates.get(551).add(583);
        permitedCoordinates.get(551).add(615);
        permitedCoordinates.get(551).add(647);
        permitedCoordinates.get(551).add(679);
        permitedCoordinates.get(551).add(711);
        permitedCoordinates.get(551).add(743);
        permitedCoordinates.get(551).add(775);

        permitedCoordinates.put(583, new ArrayList<Integer>());
        permitedCoordinates.get(583).add(231);
        permitedCoordinates.get(583).add(263);
        permitedCoordinates.get(583).add(295);
        permitedCoordinates.get(583).add(519);
        permitedCoordinates.get(583).add(551);
        permitedCoordinates.get(583).add(583);
        permitedCoordinates.get(583).add(615);
        permitedCoordinates.get(583).add(647);
        permitedCoordinates.get(583).add(679);
        permitedCoordinates.get(583).add(711);
        permitedCoordinates.get(583).add(743);
        ///
        permitedCoordinates.put(615, new ArrayList<Integer>());
        permitedCoordinates.get(615).add(231);
        permitedCoordinates.get(615).add(263);
        permitedCoordinates.get(615).add(295);
        permitedCoordinates.get(615).add(519);
        permitedCoordinates.get(615).add(551);
        permitedCoordinates.get(615).add(583);
        //
        permitedCoordinates.put(647, new ArrayList<Integer>());
        permitedCoordinates.get(647).add(231);
        permitedCoordinates.get(647).add(263);
        permitedCoordinates.get(647).add(295);
        permitedCoordinates.get(647).add(551);
        permitedCoordinates.get(647).add(583);
        //
        permitedCoordinates.put(679, new ArrayList<Integer>());
        permitedCoordinates.get(679).add(231);
        permitedCoordinates.get(679).add(263);
        permitedCoordinates.get(679).add(295);
        permitedCoordinates.get(679).add(551);
        permitedCoordinates.get(679).add(583);
        //
        permitedCoordinates.put(711, new ArrayList<Integer>());
        permitedCoordinates.get(711).add(231);
        permitedCoordinates.get(711).add(263);
        permitedCoordinates.get(711).add(295);
        permitedCoordinates.get(711).add(551);
        permitedCoordinates.get(711).add(583);
        //
        permitedCoordinates.put(743, new ArrayList<Integer>());
        permitedCoordinates.get(743).add(231);
        permitedCoordinates.get(743).add(263);
        permitedCoordinates.get(743).add(295);
        permitedCoordinates.get(743).add(551);
        permitedCoordinates.get(743).add(583);

        // System.out.println("Coordinates:" + permitedCoordinates.get(263));

        // Build : Check this later.
    }

}
