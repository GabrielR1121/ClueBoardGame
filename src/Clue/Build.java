package Clue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.text.Position;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.net.ServerSocket;
import java.util.List;
import java.util.Iterator;
import javax.swing.JFrame;

public class Build extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 842;
    static final int SCREEN_HEIGHT = 872;
    static final int UNIT_SIZE = 32;
    int align = 6;
    static int x;
    static int y;
    char direction = 'p';
    public static String color;
    Random rand = new Random();
    JFrame frame = new JFrame();

    // Permited moves Hashmap
    HashMap<Integer, ArrayList<Integer>> permitedCoordinates = new HashMap<Integer, ArrayList<Integer>>();

    // Characters hashmap
    public static HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

    // Doors positions hashmap
    HashMap<Integer, ArrayList<Integer>> doors = new HashMap<Integer, ArrayList<Integer>>();

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

    // Constructor
    public Build() {

        System.out.println("Starting build...");
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        System.out.println("Staring gameframe...");
        frame.add(this);
        frame.setTitle("Clue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        main();

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
        newPlayer();
        // Build : Check this later.
    }

    public void newPlayer() {
        // Gets the starting position for each players and draws it into the window.
        for (int i = 0; i < Client.playerColor.size(); i++) {

            if (Client.playerColor.get(i) != null) {
                switch (Client.playerColor.get(i)) {
                case "Green":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                case "Mustard":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                case "Orchid":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                case "Peacock":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                case "Plum":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                case "Scarlett":
                    getStartingCoordinates((Arrays.toString(characters.get(Client.playerColor.get(i)))).split(","), i);
                    break;
                default:
                    break;
                }// switch()
            } // if()
        }

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
            // g.fillOval(x, y, UNIT_SIZE, UNIT_SIZE);
            // Redraws the board each time something happens.
            repaint();
        }

        // Gets the starting position for each players and draws it into the window.
        for (int i = 0; i < Client.playerColor.size(); i++) {

            if (Client.playerColor.get(i) != null) {
                switch (Client.playerColor.get(i)) {
                case "Green":
                    g.setColor(colors.Green.getColor());
                    break;
                case "Mustard":
                    g.setColor(colors.Mustard.getColor());
                    break;
                case "Orchid":
                    g.setColor(colors.Orchid.getColor());
                    break;
                case "Peacock":
                    g.setColor(colors.Peacock.getColor());
                    break;
                case "Plum":
                    g.setColor(colors.Plum.getColor());
                    break;
                case "Scarlett":
                    g.setColor(colors.Scarlett.getColor());
                    break;
                default:
                    break;
                }// switch()
                g.fillOval(Client.playerX.get(i), Client.playerY.get(i), UNIT_SIZE, UNIT_SIZE);
                repaint();
            } // if()
        }
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
    public Boolean checkRoom(int xCoord, int yCoord) {

        if (doors.containsKey(xCoord)) {
            if (doors.get(xCoord).contains(yCoord))
                return true;
        }
        return false;

    }

    public static void getStartingCoordinates(String[] charArr, int idx) {

        Client.playerX.set(idx, Integer.parseInt((charArr[0].replace('[', ' ')).trim()));
        Client.playerY.set(idx, Integer.parseInt((charArr[1].replace(']', ' ')).trim()));

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
            System.out.println(Client.playerY.get(Client.currTurn));
            if (checkBounds(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn) - UNIT_SIZE)) {
                Client.playerY.set(Client.currTurn, Client.playerY.get(Client.currTurn) - UNIT_SIZE);
            }
            if (checkRoom(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn))) {
                System.out.println("Want to enter this room?");
                // System.out.println(Client.startRumor());
            }
            direction = 's';
            break;

        case 'D':
            System.out.println(Client.playerY.get(Client.currTurn));
            if (checkBounds(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn) + UNIT_SIZE)) {
                Client.playerY.set(Client.currTurn, Client.playerY.get(Client.currTurn) + UNIT_SIZE);
                System.out.println(Client.playerY.get(Client.currTurn));
            }
            if (checkRoom(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn))) {
                System.out.println("Want to enter this room?");
                // System.out.println(Client.startRumor());
            }
            direction = 's';
            break;

        case 'L':
            if (checkBounds(Client.playerX.get(Client.currTurn) - UNIT_SIZE, Client.playerY.get(Client.currTurn)))
                Client.playerX.set(Client.currTurn, Client.playerX.get(Client.currTurn) - UNIT_SIZE);
            if (checkRoom(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn))) {
                System.out.println("Want to enter this room?");
                // System.out.println(Client.startRumor());
            }
            direction = 's';
            break;

        case 'R':
            if (checkBounds(Client.playerX.get(Client.currTurn) + UNIT_SIZE, Client.playerY.get(Client.currTurn)))
                Client.playerX.set(Client.currTurn, Client.playerX.get(Client.currTurn) + UNIT_SIZE);
            if (checkRoom(Client.playerX.get(Client.currTurn), Client.playerY.get(Client.currTurn))) {
                System.out.println("Want to enter this room?");
                // System.out.println(Client.startRumor());
            }
            direction = 's';
            break;
        }
        System.out.println(Client.playerY.get(Client.currTurn));
    }

    // activates when an action is preformed in order to run previus methods.
    // Useless method right now
    public void actionPerformed(ActionEvent e) {
        System.out.println(direction);

    }

    // Checks to see if a key has been pressed and changes direction based on arrow
    // keys.
    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            System.out.println(Client.isPlayerTurn);

            if (Client.isPlayerTurn) {
                switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    direction = 'L';
                    move();
                    repaint();
                    break;

                case KeyEvent.VK_RIGHT:
                    direction = 'R';
                    move();
                    repaint();
                    break;

                case KeyEvent.VK_UP:
                    direction = 'U';
                    move();
                    repaint();
                    break;

                case KeyEvent.VK_DOWN:
                    direction = 'D';
                    move();
                    System.out.println(Client.playerY.get(Client.currTurn));
                    repaint();
                    System.out.println(Client.playerY.get(Client.currTurn));
                    break;
                }
            } // if()
        }
    }

}
