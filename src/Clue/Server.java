package Clue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.net.ServerSocket;
import java.util.List;
import java.util.Iterator;

public class Server extends JPanel implements ActionListener {

    private final int port = 2027;
    // Could change if pop-up added to ask amount of players.
    private final int numberOfConnections = 6;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private boolean isPlayerTurn = false;
    private int changeTurn = 0;

    // Not in UML yet
    private int turn;
    static final int SCREEN_WIDTH = 842;
    static final int SCREEN_HEIGHT = 872;
    static final int UNIT_SIZE = 32;
    int align = 6;
    char direction = 'p';
    static final int DELAY = 75;
    String color;

    int amountofPlayer = 4;

    int playersX[] = new int[amountofPlayer];
    int playersY[] = new int[amountofPlayer];
    Timer timer;
    static int x;
    static int y;
    boolean running = false;
    int amountCards = 21;
    Random rand = new Random();

    // try to auto generate.
    private ArrayList<Integer> secretFolder = new ArrayList<Integer>();
    public List<ArrayList<Integer>> playerCards = new ArrayList<ArrayList<Integer>>();

    // Permited moves Hashmap
    HashMap<Integer, ArrayList<Integer>> permitedCoordinates = new HashMap<Integer, ArrayList<Integer>>();

    // Characters hashmap
    HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

    // Doors positions hashmap
    HashMap<String, Integer[]> doors = new HashMap<String, Integer[]>();

    // Has the string info of all of the cards on the cardDeck array.
    HashMap<Integer, String> cardDeckMap = new HashMap<Integer, String>();

    private enum colors {
        Scarlett("255,36,0"), Plum("142,69,133"), Orchid("218,112,214"), Green("0,125,0"), Mustard("255,204,102"),
        Peacock("51,161,201");

        private Color clr;

        private colors(String rgb) {

            String[] strRGB = rgb.split(",");

            clr = new Color(Integer.parseInt(strRGB[0]), Integer.parseInt(strRGB[1]), Integer.parseInt(strRGB[2]));
        }

        public Color getColor() {
            return clr;
        }
    }

    private ArrayList<Integer> CardDeck = new ArrayList<Integer>();

    private boolean isEliminated = false;

    ServerSocket server;

    // Server constructor
    // Initalizes the panel for the jFrome with the determined Width and Height.
    // Adds the action Listeners
    // Starts the server
    Server() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        // Fills and Refills the Deck
        fillCardDeck();

        // Selects 3 cards at random from Character, Weapon and Room and sets them aside
        secretFolderDistribute();

        // Shuffles the remainning
        Collections.shuffle(CardDeck);

        distributeCards();

        main();
        // startServer(); --> se esta llamando en el main.
    }

    // Opens the sockets and waits for a connection from the client.
    // Sets the program to running and allows the timer thread to start
    // Eventually a pop up with the color will be displayed here.
    public void startServer() {
        try {

            server = new ServerSocket(port, numberOfConnections);

            // Test methods need clean up
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();

            // System.out.println("Pick your color");

            newPlayer(); // test

            while (true) {
                break;
                // Socket client = server.accept();

                // users.add(client);

                // turn = changeTurn++;

                // Runnable run = new HiloServidor(cliente,usuarios,xo,G);
                // Thread hilo = new Thread(run);
                // hilo.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void main() {

        // Adding the starting coordinates for each of the characters.
        characters.put("Green", new Integer[] { 327, 807 });
        characters.put("Mustard", new Integer[] { 775, 263 });
        characters.put("Orchid", new Integer[] { 487, 807 });// to do
        characters.put("Peacock", new Integer[] { 39, 615 });
        characters.put("Plum", new Integer[] { 39, 199 });
        characters.put("Scarlett", new Integer[] { 551, 39 });

        // Adding the starting coordinates for each of the doors.
        doors.put("BallRoom", new Integer[] { 583, 199 });
        doors.put("BilliardRoom", new Integer[] { 551, 423 });
        doors.put("Conservatory", new Integer[] { 647, 583 });
        doors.put("DiningRoom", new Integer[] { 487, 583 });
        doors.put("Hall", new Integer[] { 327, 167 });
        doors.put("Kitchen", new Integer[] { 167, 647 });
        doors.put("Library", new Integer[] { 231, 135 });
        doors.put("Lounge", new Integer[] { 231, 295 });
        doors.put("Study", new Integer[] { 199, 519 });
        doors.put("DecisionRoom", new Integer[] { 391, 295 });

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

        startServer();
    }

    // Initializes the paint for the whole game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draws all the componets of the game onto the gameframe which include:
    // * GameBoard
    // * Players / Player Movement.
    public void draw(Graphics g) {

        Image img = Toolkit.getDefaultToolkit().getImage(".\\Assets\\GameBoard\\ClueGameBoard(Updated).jpg");

        g.drawImage(img, 0, 0, null);

        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE + align, 0, i * UNIT_SIZE + align, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE + align, SCREEN_WIDTH, i * UNIT_SIZE + align);
        }

        switch (color) {
        case "Green":
            g.setColor(colors.Green.getColor());
        case "Mustard":
            g.setColor(colors.Mustard.getColor());
        case "Orchid":
            g.setColor(colors.Orchid.getColor());
        case "Peacock":
            g.setColor(colors.Peacock.getColor());
        case "Plum":
            g.setColor(colors.Plum.getColor());
        case "Scarlett":
            // g.setColor(colors.Scarlett.getColor());
        }

        g.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);

        // Redraws the board each time something happens.
        repaint();

    }

    // Starts the Final Rumor
    public void finalRumor() {

    }

    // Uses the global variable direction in order to move a player N,E,S,W with
    // respect to the Unit_Size.
    // After player moves direction is switched to s in order to prevent cosntant
    // movement.
    // For now print (X,Y) of the player.
    public void move() {

        // int extra = (checkBounds()) ? UNIT_SIZE : 0;
        switch (direction) {

        case 'U':
            if (checkBounds(x, y - UNIT_SIZE))
                y = y - UNIT_SIZE;
            direction = 's';

            break;

        case 'D':
            if (checkBounds(x, y + UNIT_SIZE))
                y = y + UNIT_SIZE;

            direction = 's';
            break;

        case 'L':
            if (checkBounds(x - UNIT_SIZE, y))
                x = x - UNIT_SIZE;

            direction = 's';
            break;

        case 'R':
            if (checkBounds(x + UNIT_SIZE, y))
                x = x + UNIT_SIZE;

            direction = 's';
            break;
        }
    }

    public void fillCardDeck() {
        CardDeck.clear();
        for (int i = 0; i < amountCards; i++)
            CardDeck.add(i);
    }

    public void secretFolderDistribute() {
        secretFolder.clear();

        int character = rand.nextInt(6);
        secretFolder.add(character);

        int room = rand.nextInt(14 - 6 + 1) + 6;
        secretFolder.add(room);

        int weapon = rand.nextInt(20 - 15 + 1) + 15;
        secretFolder.add(weapon);

        CardDeck.remove(weapon);
        CardDeck.remove(room);
        CardDeck.remove(character);
        amountCards -= 3;
        // System.out.println("Secret Card: " + secretFolder.toString());

    }

    public void distributeCards() {
        int distribute = ((amountCards) / amountofPlayer);
        int rem = amountCards % amountofPlayer;

        int start = 0;
        int end = distribute;
        List<List<Integer>> list = new ArrayList<List<Integer>>();

        for (int i = 1; i <= amountofPlayer; i++) {

            int extra = (i <= rem) ? 1 : 0;

            list.add(CardDeck.subList(start, end));

            start = end;
            end += distribute + extra;

        }

        // System.out.println(list.toString());

    }

    // As an example cause there is not enough data.
    // THIS WILL BE EREASED
    String newColor[] = { "Green", "Mustard", "Orchid", "Peacock", "Plum", "Scarlett" };
    String[] charArr;

    // JUST FOR TEST
    // Sets all players in their respective start positions.
    public void newPlayer() {
        color = newColor[rand.nextInt(5)];

        switch (color) {
        case "Green":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Mustard":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Orchid":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Peacock":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Plum":

            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        case "Scarlett":
            charArr = (Arrays.toString(characters.get(color))).split(",");
            getStartingCoordinates(charArr);
        }

    }

    public static void getStartingCoordinates(String[] charArr) {

        x = Integer.parseInt((charArr[0].replace('[', ' ')).trim());
        y = Integer.parseInt((charArr[1].replace(']', ' ')).trim());

    }

    // Checks to see if players are not on top of each other.
    public void checkPlayer() {

    }

    // Checks to see if players are within the game bounds
    public Boolean checkBounds(int xCoord, int yCoord) {

        if (permitedCoordinates.containsKey(xCoord)) {
            if (permitedCoordinates.get(xCoord).contains(yCoord))
                return true;
        }
        return false;
    }

    // Checks to see if players are entering a room through the door.
    public void checkRoom() {

    }

    // Checks to see if player has remaining moves.
    public void checkMoves(int roll) {

    }

    // Displays GameOver screen when a player wins or all players get eliminated.
    // Will display winning cards and player who one if any.
    public void gameOver() {

    }

    // activates when an action is preformed in order to run previus methods.
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkPlayer();
            checkRoom();
        }
        repaint();
    }

    // Checks to see if a key has been pressed and changes direction based on arrow
    // keys.
    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                direction = 'L';
                break;

            case KeyEvent.VK_RIGHT:
                direction = 'R';
                break;

            case KeyEvent.VK_UP:
                direction = 'U';
                break;

            case KeyEvent.VK_DOWN:
                direction = 'D';
                break;
            }
        }
    }
}